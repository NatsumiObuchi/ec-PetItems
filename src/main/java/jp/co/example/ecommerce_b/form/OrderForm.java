package jp.co.example.ecommerce_b.form;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class OrderForm {

	/** 注文日 */
	private Date orderDate;
	
	/** 宛先氏名 */
	@NotBlank(message="名前を入力してください")
	private String destinationName;

	/** 宛先Eメール */
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "メールアドレスの形式が不正です")
	private String destinationEmail;
	
	/** 宛先郵便番号 */
	@Pattern(regexp = "^[0-9]{3}[0-9]{4}$", message = "郵便番号はハイフンなしの形式で入力してください")
	private String destinationzipCode;
	
	/** 宛先住所 */
	@NotBlank(message="住所を入力してください")
	private String destinationAddress;
	
	/** 宛先TEL */
	@Pattern(regexp = "^[0-9]{2,4}[0-9]{2,4}[0-9]{4}$", message = "電話番号はハイフンなしの形式で入力してください")
	private String destinationTell;
	
	/** 配達時間 */
	@NotEmpty(message="配達時間を選択してください")
	private String deliveryTime;
	
	/** 配達日 */
	@NotEmpty(message="配達日を選択してください")
	private String deliveryDate;

	/** 支払い方法 */
	@NotNull(message="支払方法を選択してください")
	private Integer paymentMethod;
	
	/** カードブランド */
	private Integer cardBrand;
	
	/** カード番号 */
//	@Pattern(regexp = "^[0-9]{4}[0-9]{4}[0-9]{4}[0-9]{4}$", message = "カード番号は「xxxx xxxx xxxx xxxx」の形式で入力してください")
	private String cardNumber;
	
	/** セキュリティコード */
//	@Pattern(regexp = "^[0-9]{3}$", message = "セキュリティコードを入力してください")
	private String securityCode;

	/** カード名義 */
//	@NotBlank(message = "カード名義を入力してください")
	private String cardName;

	/** 有効期限 */
//	@Pattern(regexp = "^[0-9]{2}/[0-9]{2}$", message = "有効期限を入力してください")
	private String dateOfExpiry;

	/** ポイントの使い方 */
	private String usePoint;

	/** 一部のポイントを使用した場合のポイント */
	private String usePartPoint;

	public Timestamp getDeliveryTimestamp() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String deliveryDateTime = deliveryDate + " " +deliveryTime;
		return Timestamp.valueOf(LocalDateTime.parse(deliveryDateTime, formatter));
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

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Integer getCardBrand() {
		return cardBrand;
	}

	public void setCardBrand(Integer cardBrand) {
		this.cardBrand = cardBrand;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getDateOfExpiry() {
		return dateOfExpiry;
	}

	public void setDateOfExpiry(String dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry;
	}

	public String getUsePoint() {
		return usePoint;
	}

	public void setUsePoint(String usePoint) {
		this.usePoint = usePoint;
	}

	public String getUsePartPoint() {
		return usePartPoint;
	}

	public void setUsePartPoint(String usePartPoint) {
		this.usePartPoint = usePartPoint;
	}

	@Override
	public String toString() {
		return "OrderForm [orderDate=" + orderDate + ", destinationName=" + destinationName + ", destinationEmail="
				+ destinationEmail + ", destinationzipCode=" + destinationzipCode + ", destinationAddress="
				+ destinationAddress + ", destinationTell=" + destinationTell + ", deliveryTime=" + deliveryTime
				+ ", deliveryDate=" + deliveryDate + ", paymentMethod=" + paymentMethod + ", cardBrand=" + cardBrand
				+ ", cardNumber=" + cardNumber + ", securityCode=" + securityCode + ", cardName=" + cardName
				+ ", dateOfExpiry=" + dateOfExpiry + ", usePoint=" + usePoint + ", usePartPoint=" + usePartPoint + "]";
	}


}
