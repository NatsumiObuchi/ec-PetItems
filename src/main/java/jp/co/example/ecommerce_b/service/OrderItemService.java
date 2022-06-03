package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.repository.OrderItemRepository;

@Service
public class OrderItemService {
	@Autowired
	private OrderItemRepository repository;
	
	public void insert(OrderItem orderItem) {
		repository.insert(orderItem);
	}
}
