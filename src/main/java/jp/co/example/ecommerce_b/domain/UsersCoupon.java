package jp.co.example.ecommerce_b.domain;

import java.sql.Timestamp;

public class UsersCoupon {

	//ユーザーの所持するクーポンにつけられるID
	private Integer id;
	//ユーザーID
	private Integer userId;
	//クーポンID
	private Integer couponId;
	//クーポン取得日
	private Timestamp couponGetDate;
	//クーポン期限終了日
	private Timestamp couponExpirationDate;
	//クーポン情報
	private Coupon coupon;
	
	
	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
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
		return "UsersCoupon [id=" + id + ", userId=" + userId + ", couponId=" + couponId + ", couponGetDate="
				+ couponGetDate + ", couponExpirationDate=" + couponExpirationDate + ", coupon=" + coupon + "]";
	}

	

}
