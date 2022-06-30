package jp.co.example.ecommerce_b.domain;

public class DiscountedHistory {

	private Integer id;
	private Integer orderId;
	private Integer discountPrice;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(Integer discountPrice) {
		this.discountPrice = discountPrice;
	}
	@Override
	public String toString() {
		return "DiscountedHistory [id=" + id + ", orderId=" + orderId + ", discountPrice=" + discountPrice + "]";
	}


}
