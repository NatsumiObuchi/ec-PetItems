package jp.co.example.ecommerce_b.domain;

public class UsersPointHistory {

	/** id */
	private Integer id;
	/** ユーザーid */
	private Integer userId;
	/** オーダーid */
	private Integer orderId;
	/** 使用ポイント */
	private Integer usedPoint;

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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getUsedPoint() {
		return usedPoint;
	}

	public void setUsedPoint(Integer usedPoint) {
		this.usedPoint = usedPoint;
	}

	@Override
	public String toString() {
		return "UsersPointHistory [id=" + id + ", userId=" + userId + ", orderId=" + orderId + ", usedPoint="
				+ usedPoint + "]";
	}

}
