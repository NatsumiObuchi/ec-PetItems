package jp.co.example.ecommerce_b.controller;

import java.sql.Timestamp;
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
import jp.co.example.ecommerce_b.domain.DiscountedHistory;
import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderHistory;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.Point;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.domain.UsersCoupon;
import jp.co.example.ecommerce_b.domain.UsersCouponHistory;
import jp.co.example.ecommerce_b.domain.UsersPointHistory;
import jp.co.example.ecommerce_b.form.OrderForm;
import jp.co.example.ecommerce_b.form.OrderItemForm;
import jp.co.example.ecommerce_b.service.AddresseeService;
import jp.co.example.ecommerce_b.service.CouponServise;
import jp.co.example.ecommerce_b.service.DiscountedHistoryService;
import jp.co.example.ecommerce_b.service.OrderService;
import jp.co.example.ecommerce_b.service.PointService;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private HttpSession session;

	@Autowired
	private OrderService orderservice;

	@Autowired
	private AddresseeService addresseeService;

	@Autowired
	private CouponServise couponService;
	
	@Autowired
	private PointService pointService;

	@Autowired
	private DiscountedHistoryService discountedHistoryService;

	@Autowired
	private MailSender sender;

	@ModelAttribute
	public OrderForm setUpForm() {
		return new OrderForm();
	}

	/**
	 *  「注文へ進む」を押したときの挙動
	 * @param orderForm
	 * @param model
	 * @return　注文確認画面へ
	 */
	@SuppressWarnings("null")
	@RequestMapping("")
	public String index(OrderForm orderForm, Model model) {
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

		// デフォルトのユーザ情報を反映
		if (orderForm.getDestinationName() == null && orderForm.getDestinationEmail() == null
				&& orderForm.getDestinationTell() == null && orderForm.getDestinationzipCode() == null
				&& orderForm.getDestinationAddress() == null && orderForm.getDeliveryTime() == null
				&& orderForm.getDeliveryDate() == null && orderForm.getPaymentMethod() == null) {// 1回目でこのページに来たとき（Validationでここに帰ってきたときじゃない方）
			orderForm.setDestinationName(user.getName());
			orderForm.setDestinationEmail(user.getEmail());
			orderForm.setDestinationTell(user.getTelephone());

			// デフォルトで出力されるお届け先を検索
			Addressee addressee = addresseeService.findByUserIdandSettingAddresseeTrue(user.getId());
			if (addressee != null) {
				orderForm.setDestinationzipCode(addressee.getZipCode());
				orderForm.setDestinationAddress(addressee.getAddress());
			}

			model.addAttribute("orderForm", orderForm);
		} else {
			model.addAttribute("orderForm", orderForm);
		}

		//ユーザーが利用可能なクーポンを表示
		List<UsersCoupon> usersCoupon = couponService.findAllUsersCoupon(user.getId());
		session.setAttribute("usersCoupon", usersCoupon);
		
		return "order_confirm";
	}

	
	/**
	 * 注文をする（orderHistoryテーブルに注文履歴を格納）
	 * @param orderForm
	 * @param rs
	 * @param usersCouponId　ユーザーが使用するクーポンのid
	 * @param orderItemForm
	 * @param model
	 * @param stripeToken
	 * @param stripeTokenType
	 * @param stripeEmail
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@RequestMapping("/orderSent")
	public String orderSent(@Validated OrderForm orderForm, BindingResult rs, Integer usersCouponId,
			OrderItemForm orderItemForm, Model model,
//			@RequestParam("stripeToken")
			String stripeToken,
//	        @RequestParam("stripeTokenType")
			String stripeTokenType,
//	        @RequestParam("stripeEmial")
			String stripeEmail
			) {
		if(rs.hasErrors()) {
			return index(orderForm, model);
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
		
		// discounted_historiesにインサートするようの合計値引き額の変数を用意
		Integer totalDiscountPrice = 0;

		//クーポンを使用する場合、orderに詰める合計金額情報を変更（割引）する
		List<UsersCoupon> usersCouponList = (List<UsersCoupon>) session.getAttribute("usersCoupon");
		//　↓ この変数はメソッドの最後の方で履歴のテーブルに格納するデータだよ
		Integer discountCouponPrice = 0;
		Timestamp couponGetDate = null;
		Timestamp couponExpirationDate = null;
		Integer couponId = 0;
		for(UsersCoupon usersCoupon : usersCouponList) {
			if(usersCoupon.getId()==usersCouponId) {
				discountCouponPrice = usersCoupon.getCoupon().getDiscountPrice();
				couponGetDate = usersCoupon.getCouponGetDate();
				couponExpirationDate = usersCoupon.getCouponExpirationDate();
				couponId = usersCoupon.getCouponId();
			}
		}
		
	/*	Integer price = 0;
		if(usersCouponId==0) {
			System.out.println("クーポン使ってないよ");
			price = order.getTotalPrice();
		}else {
			System.out.println("割引したよ");
			price = order.getTotalPrice() - discountPrice; 
		}
		*/
		Integer price = order.getTotalPrice();
		order.setTotalPrice(price);
		
		//使用したクーポンのdeletedをtrueにアップデートする
		couponService.usedUsersCoupon(usersCouponId);		
		
		
		//	代金引換orクレジットカード
		if(order.getPaymentMethod() == 1) {
			order.setStatus(1);
		} else if (order.getPaymentMethod() == 2) {
			order.setStatus(2);
			order.setCardNumber(order.getCardNumber());
			order.setCardBrand(order.getCardBrand());

			// 以下クレジットカードメソッド
			Stripe.apiKey = "sk_test_51LA6lgEM1Eja88iZuJf4dKIA2zP1aXLbUnjHNVd013Tgob9l5SPMcbGZhkeRFiQ9z3qJgr8crduiKOMcwuBtyU1E00DikrfI8S";

			Map<String, Object> chargeMap = new HashMap<String, Object>();
			chargeMap.put("aomunt", price);
			chargeMap.put("description", "合計金額");
			chargeMap.put("currency", "jpy");
			chargeMap.put("source", stripeToken);

			try {
				Charge charge = Charge.create(chargeMap);
			} catch (StripeException e) {
				e.printStackTrace();
			}
			ResponseEntity response = ResponseEntity.ok().build();
		}
		
		//　orderテーブルに格納
		orderservice.update(order);
		System.out.println(order);
		Integer orderId = order.getId();
		
		// メール送信用のメソッド
		sendEmail(orderForm.getDestinationEmail());

		//　orderHistoryテーブルに格納
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
			
			BeanUtils.copyProperties(order, orderHistory);
			orderHistory= orderservice.insertHistory(orderHistory);
		}
		
		// ポイントを使用した場合
		Point point = (Point) session.getAttribute("point");
		Integer discountPointPrice = 0;// ポイントによる値引き額箱の変数に代入する
		Integer usePoint = Integer.parseInt(orderForm.getUsePoint());// ラジオボタン
		Integer newGetPoint = (int) (price * 0.01);// 獲得予定ポイント
		
		// users_point_historiesテーブルに格納
		UsersPointHistory usersPointHistory = new UsersPointHistory();
		usersPointHistory.setOrderId(orderId);
		usersPointHistory.setUserId(userId);		
		
		// 以下、ポイントの使い方によって条件分岐する
		// (ポイントを使用しない場合は、users_points_historiesテーブルにはインサートされない)
		if (usePoint == 0) {
			Integer newPointTotal = newGetPoint + point.getPoint();
			point.setPoint(newPointTotal);
			pointService.update(point);
		} else if (usePoint == 1) {// 「全てのポイントを使用する」を押したとき
			if (point.getPoint() > price) {// ポイント残高が合計金額より高い時
				discountPointPrice = price;
				Integer resultPoint = point.getPoint() - discountPointPrice;
				Integer result = resultPoint + newGetPoint;// 獲得予定ポイントと合算
				point.setPoint(result);
				usersPointHistory.setUsedPoint(discountPointPrice);
			} else {// ポイントを全て使い切る
				discountPointPrice = point.getPoint();
				usersPointHistory.setUsedPoint(discountPointPrice);// 先に使用した全てのポイントをhistoryにインサート
				point.setPoint(newGetPoint);// 獲得予定ポイントが付与される
			}
			pointService.update(point);// ポイントテーブルのポイントを更新
			pointService.insertPointHistory(usersPointHistory);
		} else if (usePoint == 2) {// 「一部のポイントを使用する」を押したとき
			if (orderForm.getUsePartPoint() != null) {
				discountPointPrice = Integer.parseInt(orderForm.getUsePartPoint());
				usersPointHistory.setUsedPoint(discountPointPrice);// 使用したポイントを先にインサート
				Integer resultPoint = point.getPoint() - discountPointPrice + newGetPoint;
				point.setPoint(resultPoint);
			}
			pointService.insertPointHistory(usersPointHistory);
			pointService.update(point);
		}

		//users_coupon_historysテーブルに格納
		UsersCouponHistory userCouponHistory = new UsersCouponHistory();
		userCouponHistory.setUserId(user.getId());
		userCouponHistory.setOrderId(orderId);
		userCouponHistory.setCouponId(couponId);
		userCouponHistory.setCouponGetDate(couponGetDate);
		userCouponHistory.setCouponExpirationDate(couponExpirationDate);
		couponService.insertUsersCouponHistorys(userCouponHistory);
		System.out.println(userCouponHistory);

		// ポイント、もしくはクーポンを使用した際にdiscounted_historiesにインサートされる
		if (discountCouponPrice != 0 || discountPointPrice != 0) {
			totalDiscountPrice = discountCouponPrice + discountPointPrice;
			DiscountedHistory discountedHistory = new DiscountedHistory();
			discountedHistory.setOrderId(orderId);
			discountedHistory.setDiscountPrice(totalDiscountPrice);
			discountedHistoryService.insert(discountedHistory);
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
}
