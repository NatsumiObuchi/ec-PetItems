package jp.co.example.ecommerce_b.domain;

import java.sql.Timestamp;

public class UsersCouponHistory {

	// UsersCouponHistoryテーブルにおける一意のid
	private Integer id;
	// ユーザーid
	private Integer userId;
	// 注文履歴のid
	private Integer orderId;
	// クーポンid
	private Integer couponId;
	// クーポン取得日
	private Timestamp couponGetDate;
	// クーポン期限終了日
	private Timestamp couponExpirationDate;

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

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public Timestamp getCouponGetDate() {
		return couponGetDate;
	}

	public void setCouponGetDate(Timestamp couponGetDate) {
		this.couponGetDate = couponGetDate;
	}

	public Timestamp getCouponExpirationDate() {
		return couponExpirationDate;
	}

	public void setCouponExpirationDate(Timestamp couponExpirationDate) {
		this.couponExpirationDate = couponExpirationDate;
	}

	@Override
	public String toString() {
		return "UsersCouponHistory [id=" + id + ", userId=" + userId + ", orderId=" + orderId + ", couponId=" + couponId
				+ ", couponGetDate=" + couponGetDate + ", couponExpirationDate=" + couponExpirationDate + "]";
	}

}
