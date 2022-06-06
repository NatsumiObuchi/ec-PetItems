package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.repository.OrderItemRepository;

@Service
public class OrderItemService {
	@Autowired
	private OrderItemRepository repository;
	
	public OrderItem insert(OrderItem orderItem) {
		return repository.insert(orderItem);
	}

	public void delete(int id) {
		repository.delete(id);
	}

	public List<OrderItem> findByOrderId(Integer id) {
		return repository.findByOrderId(id);
	}
}
