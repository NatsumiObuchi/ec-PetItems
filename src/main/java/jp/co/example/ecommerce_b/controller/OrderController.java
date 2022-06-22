package jp.co.example.ecommerce_b.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import jp.co.example.ecommerce_b.domain.Addressee;
import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderHistory;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.OrderForm;
import jp.co.example.ecommerce_b.form.OrderItemForm;
import jp.co.example.ecommerce_b.service.AddresseeService;
import jp.co.example.ecommerce_b.service.ItemService;
import jp.co.example.ecommerce_b.service.OrderItemService;
import jp.co.example.ecommerce_b.service.OrderService;
import jp.co.example.ecommerce_b.service.UserService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private HttpSession session;

	@Autowired
	private OrderItemService orderitemservice;

	@Autowired
	private ItemService itemService;

	@Autowired
	private OrderService orderservice;

	@Autowired
	private UserService userService;

	@Autowired
	private AddresseeService addresseeService;

	@Autowired
	private MailSender sender;

	@ModelAttribute
	public OrderForm setUpForm() {
		return new OrderForm();
	}

	@RequestMapping("")

	public String index(OrderForm orderForm) {// 「注文へ進む」を押したときに走る処理
		Integer totalPrice = (Integer) session.getAttribute("totalPrice");
		session.setAttribute("totalPrice", totalPrice);

		Integer totalTax = (Integer) session.getAttribute("totalTax");
		session.setAttribute("totalTax", totalTax);

		// ユーザーがログインしていなければログインページへ遷移する
		User user = (User) session.getAttribute("user");
		if (user == null) {
			session.setAttribute("transitionSourcePage", "order");// 遷移元ページの記録
			return "forward:/user/toLogin";
		}

		// 宛先氏名、宛先Eメール、電話番号はデフォルトでユーザ情報を反映
		orderForm.setDestinationName(user.getName());
		orderForm.setDestinationEmail(user.getEmail());
		orderForm.setDestinationTell(user.getTelephone());

		// デフォルトで出力されるお届け先を検索
		Addressee addressee = addresseeService.findByUserIdandSettingAddresseeTrue(user.getId());
		if (addressee != null) {
			orderForm.setDestinationzipCode(addressee.getZipCode());
			orderForm.setDestinationAddress(addressee.getAddress());
		}

		// ユーザが登録済のお届け先一覧を表示(modal表示用)
		List<Addressee> addresseeList = addresseeService.findAddresseeByUserId(user.getId());
		session.setAttribute("addresseeList", addresseeList);


		return "order_confirm";

	}

	/**
	 * 注文をする（orderHistoryテーブルに注文履歴を格納）
	 *
	 */

	@RequestMapping("/orderSent")
	public String orderSent(@Validated OrderForm orderForm, BindingResult rs, OrderItemForm orderItemForm,
//			@RequestParam("stripeToken")
			String stripeToken,
//	        @RequestParam("stripeTokenType")
			String stripeTokenType,
//	        @RequestParam("stripeEmial")
			String stripeEmail
			) {

		if(rs.hasErrors()) {
			return index(orderForm);
		}

//		注文する
		Order order = new Order();
		order = (Order) session.getAttribute("order");
		Integer userId = order.getUserId();

		BeanUtils.copyProperties(orderForm, order);

		order.setUserId(userId);
		List<OrderItem> orderList = (List<OrderItem>) session.getAttribute("cartList");
		order.setOrderItemList(orderList);

		
		// ログイン中の「ユーザーID」「ユーザーインスタンス」をオーダーに格納
		User user = (User) session.getAttribute("user");
		order.setUser(user);
		order.setUserId(user.getId());

//		注文日の実装
		LocalDate localdate = LocalDate.now();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = simpleDateFormat.parse(localdate.toString());
			order.setOrderDate(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		代金引換orクレジットカード
		if(order.getPaymentMethod() == 1) {
			order.setStatus(1);
		}else if(order.getPaymentMethod() == 2) {
			order.setStatus(2);
			order.setCardNumber(order.getCardNumber());
			order.setCardBrand(order.getCardBrand());

			// 以下クレジットカードメソッド
			Stripe.apiKey = "sk_test_51LA6lgEM1Eja88iZuJf4dKIA2zP1aXLbUnjHNVd013Tgob9l5SPMcbGZhkeRFiQ9z3qJgr8crduiKOMcwuBtyU1E00DikrfI8S";
			Integer price = order.getTotalPrice();

			Map<String, Object> chargeMap = new HashMap<String, Object>();
			chargeMap.put("aomunt", price);
			chargeMap.put("description", "合計金額");
			chargeMap.put("currency", "jpy");
			chargeMap.put("source", stripeToken);

			try {
				Charge charge = Charge.create(chargeMap);
				System.out.println(charge);
			} catch (StripeException e) {
				e.printStackTrace();
			}
			ResponseEntity response = ResponseEntity.ok().build();
		}
		
//		orderテーブルに格納
		orderservice.update(order);
		System.out.println(order);
		
		// メール送信用のメソッド
		sendEmail(orderForm.getDestinationEmail());

//		orderHistoryテーブルに格納
		OrderHistory orderHistory = new OrderHistory();
		List<OrderItem> orderItemList = order.getOrderItemList();

		for (OrderItem orderItem : orderItemList) {

			orderHistory.setOrderId(orderItem.getOrderId());
			orderHistory.setUserId(order.getUserId()); 

			Item item = orderItem.getItem();
			orderHistory.setImagePath(item.getImagePath());
			orderHistory.setItemName(item.getName());
			orderHistory.setItemPrice(item.getPrice());
			orderHistory.setQuantity(orderItem.getQuantity());
			orderHistory.setSubTotalPrice(orderItem.getSubTotal());
			
			System.out.println(orderHistory);
			BeanUtils.copyProperties(order, orderHistory);

			orderservice.insertHistory(orderHistory);
			System.out.println(orderHistory);
		}
		session.setAttribute("order", null);
		session.setAttribute("cartList", null);
		return "order_finished";
	}

	/**
	 * 購入履歴を表示する
	 */
	@RequestMapping("/orderHistory")
	public String findOrderHistory(Model model) {
		if (session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			List<List<OrderHistory>> historyList = orderservice.findOrderHistory(user.getId());
			session.setAttribute("historyList", historyList);
			System.out.println("historyList.size:" + historyList.size());
			System.out.println("historyList:" + historyList);
			if (historyList.get(0).size() == 0) {
				model.addAttribute("alert", "注文履歴はありません。");
			}
			return "order_history";
		} else {
			session.setAttribute("transitionSourcePage", "orderHistory");
			return "redirect:/user/toLogin2";
		}
	}

	/**
	 * @param user ログイン中のユーザーの、支払い前のオーダーをセッションスコープに格納する処理。
	 */

	public void checkOrderBeforePayment(Integer userId) {
		// 存在すればそのorderが入り、存在しなければnullがはいる。
		Order order = orderservice.findOrderBeforePayment(userId);
		if (order == null) {
			// Orderを新たにインスタンス化
			order = new Order();
		}
		session.setAttribute("order", order);
	}

	/**
	 * 注文確定用のメールを送信するメソッド
	 * 
	 * @param email
	 */
	public void sendEmail(String email) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setFrom("rakuraku.pet@gmail.com");
		mailMessage.setTo(email);
		mailMessage.setSubject("注文内容の確認");
		mailMessage.setText("" + "　---------------------------------------\n" + "　この度は、らくらくペットをご利用いただきありがとうございました。\n"
				+ "　ご注文番号「XXXX-XXXX-XXXX」で受け付けいたしました。\n" + "　本メール到着後は、商品や本サービスにおけるご注文はキャンセル・変更できません。\n"
				+ "　ご不明な点がございましたら、下記からお問い合わせください。\n" + "　連絡先：XXX-XXXX-XXXX\n"
				+ " ---------------------------------------");

		try {
			sender.send(mailMessage);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

//public void checkOrderBeforePayment(User user) {
//// 存在すればそのorderが入り、存在しなければnullがはいる。
//Order order = orderservice.findOrderBeforePayment(user);
//if (order == null) {
//	// Orderを新たにインスタンス化
//	order = new Order();
//}
//session.setAttribute("order", order);
//}

}
