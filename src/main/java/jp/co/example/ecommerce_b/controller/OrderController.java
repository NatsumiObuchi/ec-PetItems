package jp.co.example.ecommerce_b.controller;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.OrderForm;
import jp.co.example.ecommerce_b.form.OrderItemForm;
import jp.co.example.ecommerce_b.service.ItemService;
import jp.co.example.ecommerce_b.service.OrderItemService;
import jp.co.example.ecommerce_b.service.OrderService;

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
	 * 注文する
	 *
	 */
	
	@RequestMapping("/orderSent")
	public String orderSent(OrderForm orderForm,OrderItemForm orderItemForm,Model model) {
		Order order = new Order();
		BeanUtils.copyProperties(orderForm, order);
		
//		orderservice.update(order);
		System.out.println(orderForm);
		
		return "order_finished";
	}

	/**
	 * @param user ログイン中のユーザーの、支払い前のオーダーをセッションスコープに格納する処理。
	 */
//	public void checkOrderBeforePayment(User user) {
//		// 存在すればそのorderが入り、存在しなければnullがはいる。
//		Order order = orderservice.findOrderBeforePayment(user);
//		if (order == null) {
//			// Orderを新たにインスタンス化
//			order = new Order();
//		}
//		session.setAttribute("order", order);
//	}
	
	
}
