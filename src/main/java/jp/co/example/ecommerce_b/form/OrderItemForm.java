package jp.co.example.ecommerce_b.form;

import jp.co.example.ecommerce_b.domain.Item;

public class OrderItemForm {
	/** id */
	private Integer id;
	/** 商品id */
	private Integer itemId;
	/** 注文id */
	private Integer orderId;
	/** 数量 */
	private Integer quantity;
	/** 商品 */
	private Item item;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	@Override
	public String toString() {
		return "OrderItemForm [id=" + id + ", itemId=" + itemId + ", orderId=" + orderId + ", quantity=" + quantity
				+ ", item=" + item + "]";
	}
}
