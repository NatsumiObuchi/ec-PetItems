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
	
	public void update(Order order){
		repository.update(order);
	}
	
	/**
	 * 注文履歴を挿入する
	 *
	 */
	public void insertHistory(OrderHistory orderHistory) {
		repository.insertHistory(orderHistory);
	}
	
	/**
	 * orderIdを指定して注文履歴リストを返す
	 *
	 */
	public List<OrderHistory> findOrderHistory(Integer orderId){
		List<OrderHistory> historyList = repository.findOrderHistory(orderId);
		return historyList; 
		
		
	}
	
}
	