package jp.co.example.ecommerce_b.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.OrderForm;
import jp.co.example.ecommerce_b.form.OrderItemForm;
import jp.co.example.ecommerce_b.service.ItemService;
import jp.co.example.ecommerce_b.service.OrderItemService;
import jp.co.example.ecommerce_b.service.OrderService;
import jp.co.example.ecommerce_b.domain.OrderHistory;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderservice;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private OrderItemService orderitemservice;
	
	@Autowired
	private ItemService itemService;

	@Autowired
	private OrderService orderService;

	
	@ModelAttribute
	public OrderForm setUpForm() {
		return new OrderForm();
	}
	
	@RequestMapping("")
	public String index() {
		
		Integer totalPrice = (Integer) session.getAttribute("totalPrice");
		session.setAttribute("totalPrice", totalPrice);
		
		Integer totalTax = (Integer) session.getAttribute("totalTax");
		session.setAttribute("totalTax", totalTax);
		
		return "order_confirm";
	}
	
	
	/**
	 * 注文をする（orderHistoryテーブルに注文履歴を格納）
	 *
	 */

	
	@RequestMapping("/orderSent")
	public String orderSent(OrderForm orderForm,OrderItemForm orderItemForm,Model model) {
		
//		注文する
		Order order = new Order();
		BeanUtils.copyProperties(orderForm, order);
		
		LocalDate localdate = LocalDate.now();	
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = simpleDateFormat.parse(localdate.toString());
			order.setOrderDate(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		orderservice.update(order);
		System.out.println(order);
		
		
		
//		orderHistoryテーブルに格納
		OrderHistory orderHistory = new OrderHistory();
		List<OrderItem> orderItemList = orderForm.getOrderItemList();

		for (OrderItem orderItem : orderItemList) {

			orderHistory.setOrderId(orderItem.getOrderId());
			orderHistory.setUserId(order.getUser().getId()); 

			Item item = orderItem.getItem();
			orderHistory.setImagePath(item.getImagePath());
			orderHistory.setItemName(item.getName());
			orderHistory.setItemPrice(item.getPrice());
			orderHistory.setQueantity(orderItem.getQuantity());
			orderHistory.setSubTotalPrice(orderItem.getSubTotal());
			BeanUtils.copyProperties(order, orderHistory);

			orderservice.insertHistory(orderHistory);
		}
		
		return "order_finished";
	}
	
	/**
	 * 購入履歴を表示する
	 */
	@RequestMapping("/orderHistory")
	public String findOrderHistory(Model model) {
		if(session.getAttribute("user")!=null) {
			User user = (User) session.getAttribute("user");
			List<List<OrderHistory>> historyList=orderservice.findOrderHistory(user.getId());
			session.setAttribute("historyList", historyList);
			if(historyList.size()==0) {
				model.addAttribute("alert","注文履歴はありません。");
			}
			return "order_history";
		}else {
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
