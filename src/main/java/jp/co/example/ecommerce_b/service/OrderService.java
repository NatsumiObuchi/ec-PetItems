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
	 * orderテーブルに情報を追加する（user_id, status）
	 */
	public Order insertOrder(Order order) {
		return repository.insertOrder(order);
	}
	
	/**
	 * 注文する
	 *
	 */
	public void update(Order order) {
		repository.update(order);
	}

	/**
	 * @param order カートから商品を削除する時
	 */
	public void updateOrdersWhenDeleteOrderItemFromCart(Order order) {
		repository.updateOrdersWhenDeleteOrderItemFromCart(order);
	}
	

	/**
	 * 注文履歴を挿入する
	 * @return 
	 *
	 */
	public OrderHistory insertHistory(OrderHistory orderHistory) {
		  return repository.insertHistory(orderHistory);
	}
	
	/**
	 * @param user
	 * @return 当該ユーザーが支払い前のオーダーを持っているか確認する。
	 */
	public Order findOrderBeforePayment(Integer userId) {
		return repository.findByIdAndStatusIs0(userId);
	}
	
	/**
	 * orderIdを指定して注文履歴リストを返す
	 *
	 */
	public List<List<OrderHistory>> findOrderHistory(Integer userId){
		List<List<OrderHistory>> historyList = repository.findOrderHistory(userId);
		return historyList; 
	}
	
}
	