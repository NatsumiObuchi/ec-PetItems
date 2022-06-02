package jp.co.example.ecommerce_b.controller;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.form.OrderForm;
import jp.co.example.ecommerce_b.form.OrderItemForm;
import jp.co.example.ecommerce_b.service.OrderItemService;
import jp.co.example.ecommerce_b.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderservice;
	
	@Autowired
	private OrderItemService orderitemservice;

	
	@ModelAttribute
	public OrderForm setUpForm() {
		return new OrderForm();
	}
	
	@RequestMapping("")
	public String index(Model model) {
		Map<Integer, String> statusMap = new LinkedHashMap<>();
		statusMap.put(0, "注文前");
		statusMap.put(1, "未入金");
		statusMap.put(2, "入金済");
		statusMap.put(3, "発送済");
		statusMap.put(4, "配送完了");
		statusMap.put(9, "キャンセル");
		
		model.addAttribute("statusMap", statusMap);
		
		Map<Integer, String> paymentMap = new LinkedHashMap<>();
		paymentMap.put(1, "代金引換");
		paymentMap.put(2, "クレジットカード");
		
		model.addAttribute("paymentMap", paymentMap);
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
		
		List<Integer> orderList = new ArrayList<>();
		
//		for(Integer orderInfo : orderForm.getPaymentMap()) {
//			switch(orderInfo) {
//			case 1:
//				orderList.add(1);
//				break;
//			case 2:
//				orderList.add(2);
//				break;
//			}
//		}
		
		orderservice.insert(order);
		
		OrderItem orderItem = new OrderItem();
		BeanUtils.copyProperties(orderItemForm, orderItem);
		
//		orderitemservice.insert(orderItem);
		
		return "order_finished";
	}
}
