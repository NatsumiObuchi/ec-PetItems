package jp.co.example.ecommerce_b.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderHistory;
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
	
	/**
	 * 注文履歴を挿入する
	 *
	 */
	public void insertHistory(OrderHistory orderHistory) {
		repository.insertHistory(orderHistory);
	}
	
}
	