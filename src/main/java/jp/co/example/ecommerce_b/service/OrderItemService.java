package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
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

	public void update(OrderItem orderItem) {
		repository.update(orderItem);
	}

	/**
	 * カートリストに同じ商品があれば合算し、新しい商品であれば追加する
	 * 
	 * @return orderItemのList(cartList)
	 */
	public List<OrderItem> insertOrUpdateCartList(List<OrderItem> cartList, Integer itemId, Integer quantity, Item item,
			Order order) {
		boolean existSameItem = false;
		for (int i = 0; i < cartList.size(); i++) {
			OrderItem oi = cartList.get(i);
			if (oi.getItemId() == itemId) {
				Integer newQuantity = oi.getQuantity() + quantity;
				oi.setQuantity(newQuantity);
				oi.setSubTotal(item.getPrice() * newQuantity);
				repository.update(oi);// updateを実行
				cartList.set(i, oi);// setメソッドで置き換える
				existSameItem = true;
				break;// 一致したアイテムがあればその時点でループを終了
			}
		}

		if (!existSameItem) {
			// OrderItemを生成→ショッピングカートにセットする
			OrderItem orderItem = new OrderItem();
			orderItem.setItemId(itemId);// itemIdの格納
			orderItem.setOrderId(order.getId());// orderIdの格納
			orderItem.setQuantity(quantity);// quantityの格納
			orderItem.setItem(item);// itemの格納
			Integer subtotal = item.getPrice() * orderItem.getQuantity();// subTotalの格納
			orderItem.setSubTotal(subtotal);
			orderItem = repository.insert(orderItem);// orderItemテーブルにインサートかつidの取得(idが詰まったorderItem)
			cartList.add(orderItem);
		}
		return cartList;
	}
}
