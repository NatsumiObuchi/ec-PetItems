package jp.co.example.ecommerce_b.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.repository.OrderRepository;


@Service
public class OrderService {
	@Autowired
	private OrderRepository repository;
	
	/**
	 * 注文する
	 *
	 */
	
	public void insert(Order order){
		repository.insert(order);
	}
}
	