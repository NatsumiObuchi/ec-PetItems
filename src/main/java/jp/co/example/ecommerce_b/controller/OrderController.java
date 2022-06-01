package jp.co.example.ecommerce_b.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.form.OrderForm;
import jp.co.example.ecommerce_b.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService service;

	
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
	public String orderSent(OrderForm orderForm) {
		Order order = new Order();
		BeanUtils.copyProperties(orderForm, order);
		
		service.insert(order);
		return "order_finished";
	}
}
