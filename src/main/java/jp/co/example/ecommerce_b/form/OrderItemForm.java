package jp.co.example.ecommerce_b.form;

import jp.co.example.ecommerce_b.domain.Item;

public class OrderItemForm {
	

	/** id */
	private String id;
	/** 商品id */
	private String itemId;
	/** 注文id */
	private String orderId;
	/** 数量 */
	private String quantity;
	/** 商品 */
	private Item item;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
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
