package jp.co.example.ecommerce_b.domain;

import java.sql.Timestamp;
import java.util.Date;

public class OrderHistory {
	
	
	/** 注文履歴ID */
	private Integer Id;
	/** 注文ID */
	private Integer orderId;
	/** 購入者ID */
	private Integer userId;
	/** 商品画像 */
	private String imagePath;
	/** アイテム名 */
	private String itemName;
	/** 金額 */
	private Integer itemPrice;
	/** 数量 */
	private Integer queantity;
	/** 小計 */
	private Integer subTotalPrice;
	/** 合計金額 */
	private Integer totalPrice;
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
	private Integer paymentMethod;
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
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
	public Integer getSubTotalPrice() {
		return subTotalPrice;
	}
	public void setSubTotalPrice(Integer subTotalPrice) {
		this.subTotalPrice = subTotalPrice;
	}
	public Integer getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Integer totalPrice) {
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
	public Integer getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	@Override
	public String toString() {
		return "OrderHistory [Id=" + Id + ", orderId=" + orderId + ", userId=" + userId + ", imagePath=" + imagePath
				+ ", itemName=" + itemName + ", itemPrice=" + itemPrice + ", queantity=" + queantity
				+ ", subTotalPrice=" + subTotalPrice + ", totalPrice=" + totalPrice + ", orderDate=" + orderDate
				+ ", destinationName=" + destinationName + ", destinationEmail=" + destinationEmail
				+ ", destinationzipCode=" + destinationzipCode + ", destinationAddress=" + destinationAddress
				+ ", destinationTell=" + destinationTell + ", deliveryTime=" + deliveryTime + ", paymentMethod="
				+ paymentMethod + "]";
	}
	

}
