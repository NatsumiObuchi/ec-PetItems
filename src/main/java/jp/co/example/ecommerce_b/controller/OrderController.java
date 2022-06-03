package jp.co.example.ecommerce_b.controller;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
		
		return "order_confirm";
	}
	
	/**
	 * 注文をする（orderHistoryテーブルに注文履歴を格納）
	 *
	 */
	@RequestMapping("/orderSent")
	public String orderSent(OrderForm orderForm,OrderItemForm orderItemForm,Model model) {
		Order order = new Order();
		BeanUtils.copyProperties(orderForm, order);
		
//		orderservice.update(order);
		System.out.println(orderForm);
		
		OrderHistory orderHistory = new OrderHistory();
		List<OrderItem> orderItemList = orderForm.getOrderItemList();

		for (OrderItem orderItem : orderItemList) {

			orderHistory.setOrderId(orderItem.getOrderId());

			Item item = orderItem.getItem();
			orderHistory.setImagePath(item.getImagePath());
			orderHistory.setItemName(item.getName());
			orderHistory.setItemPrice(item.getPrice());
			orderHistory.setQueantity(orderItem.getQuantity());
			BeanUtils.copyProperties(order, orderHistory);

			orderservice.insertHistory(orderHistory);
		}
		
		return "order_finished";
	}
	
	/**
	 * 購入履歴を表示する
	 */
	@RequestMapping("/orderHistory")
	public String findOrderHistory(Integer orderId,Model model) {
		if(session.getAttribute("user")!=null) {
			List<OrderHistory> historyList=orderservice.findOrderHistory(orderId);
			session.setAttribute("historyList", historyList);
			
			for(OrderHistory list:historyList) {
				int shokei=list.getItemPrice()*list.getQueantity();	
				model.addAttribute("shokei",shokei);
			}
			return "order_history";
		}else {
			return "redirect:/user/toLogin";
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
