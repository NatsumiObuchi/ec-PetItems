package jp.co.example.ecommerce_b.domain;


public class OrderHistory {
	
	private Integer historyId;
	private Integer orderId;
	private String itemName;
	private Integer itemPrice;
	private Integer queantity;
	
	public Integer getHistoryId() {
		return historyId;
	}
	public void setHistoryId(Integer historyId) {
		this.historyId = historyId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Integer getQueantity() {
		return queantity;
	}
	public void setQueantity(Integer queantity) {
		this.queantity = queantity;
	}
	@Override
	public String toString() {
		return "OrderHistory [historyId=" + historyId + ", orderId=" + orderId + ", itemName=" + itemName
				+ ", itemPrice=" + itemPrice + ", queantity=" + queantity + "]";
	}
	

}
