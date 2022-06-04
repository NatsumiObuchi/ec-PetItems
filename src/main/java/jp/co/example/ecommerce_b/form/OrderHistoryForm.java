package jp.co.example.ecommerce_b.form;

import java.sql.Timestamp;
import java.util.Date;

public class OrderHistoryForm {
	
	
	/** 履歴ID */
	private String Id;
	/** 注文ID */
	private String orderId;
	/** 購入者ID */
	private String userId;
	/** 商品画像 */
	private String imagePath;
	/** アイテム名 */
	private String itemName;
	/** 金額 */
	private String itemPrice;
	/** 数量 */
	private String queantity;
	/** 合計金額 */
	private String totalPrice;
	/** 注文日 */
	private Date orderDate;
	/** 宛先氏名 */
	private String destinationName;
	/** 宛先Eメール */
	private String destinationEmail;
	/** 宛先郵便番号 */
	private String destinationzipCode;
	/** 宛先住所 */
	private String destinationAddress;
	/** 宛先TEL */
	private String destinationTell;
	/** 配達時間 */
	private Timestamp deliveryTime;
	/** 支払い方法 */
	private String paymentMethod;
	
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getQueantity() {
		return queantity;
	}
	public void setQueantity(String queantity) {
		this.queantity = queantity;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	public String getDestinationEmail() {
		return destinationEmail;
	}
	public void setDestinationEmail(String destinationEmail) {
		this.destinationEmail = destinationEmail;
	}
	public String getDestinationzipCode() {
		return destinationzipCode;
	}
	public void setDestinationzipCode(String destinationzipCode) {
		this.destinationzipCode = destinationzipCode;
	}
	public String getDestinationAddress() {
		return destinationAddress;
	}
	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	public String getDestinationTell() {
		return destinationTell;
	}
	public void setDestinationTell(String destinationTell) {
		this.destinationTell = destinationTell;
	}
	public Timestamp getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Timestamp deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	@Override
	public String toString() {
		return "OrderHistoryForm [Id=" + Id + ", orderId=" + orderId + ", userId=" + userId + ", imagePath=" + imagePath
				+ ", itemName=" + itemName + ", itemPrice=" + itemPrice + ", queantity=" + queantity + ", totalPrice="
				+ totalPrice + ", orderDate=" + orderDate + ", destinationName=" + destinationName
				+ ", destinationEmail=" + destinationEmail + ", destinationzipCode=" + destinationzipCode
				+ ", destinationAddress=" + destinationAddress + ", destinationTell=" + destinationTell
				+ ", deliveryTime=" + deliveryTime + ", paymentMethod=" + paymentMethod + "]";
	}

	
	

}
