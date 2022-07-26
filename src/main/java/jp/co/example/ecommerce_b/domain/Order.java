package jp.co.example.ecommerce_b.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author erika
 *
 */
public class Order {

	/** 注文id */
	private Integer id;
	/** ユーザーid */
	private Integer userId;
	/** 状態 */
	private Integer status;
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
	private Timestamp deliveryTimestamp;
	/** 支払い方法 */
	private Integer paymentMethod;
	/** カード番号 */
	private String cardNumber;
	/** カードブランド */
	private Integer cardBrand;
	/** ユーザー */
	private User user;
	/** 注文商品リスト */
	private List<OrderItem> orderItemList;

	/**
	 * 消費税を計算するメソッド
	 * 
	 * @return tax
	 */
//	public int getTax() {
//		int tax = (int) (totalPrice * 0.1);
//		return tax;
//	}

	/**
	 * 注文金額合計を計算するメソッド
	 * 
	 * @return calcTotal
	 */
	public int getCalcTotal(OrderItem orderItem) {
//		totalPrice = orderItem.getSubTotal();
		return totalPrice;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Timestamp getDeliveryTimestamp() {
		return deliveryTimestamp;
	}

	public void setDeliveryTimestamp(Timestamp deliveryTimestamp) {
		this.deliveryTimestamp = deliveryTimestamp;
	}

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public Integer getCardBrand() {
		return cardBrand;
	}

	public void setCardBrand(Integer cardBrand) {
		this.cardBrand = cardBrand;
	}
	
	public int calcTotalPrice() {
		int total = 0;
		List<OrderItem> orderItemList = this.getOrderItemList();
		for (OrderItem orderItem : orderItemList) {
			total += orderItem.getItem().getPrice() * orderItem.getQuantity();
		}
		return (int) (total * 1.1);// この合計を代入する。
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", userId=" + userId + ", status=" + status + ", totalPrice=" + totalPrice
				+ ", orderDate=" + orderDate + ", destinationName=" + destinationName + ", destinationEmail="
				+ destinationEmail + ", destinationzipCode=" + destinationzipCode + ", destinationAddress="
				+ destinationAddress + ", destinationTell=" + destinationTell + ", deliveryTimestamp="
				+ deliveryTimestamp + ", paymentMethod=" + paymentMethod + ", cardNumber=" + cardNumber + ", cardBrand="
				+ cardBrand + ", user=" + user + ", orderItemList=" + orderItemList + "]";
	}

}
