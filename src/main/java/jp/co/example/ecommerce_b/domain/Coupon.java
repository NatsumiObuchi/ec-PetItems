package jp.co.example.ecommerce_b.domain;

import java.sql.Timestamp;

public class Coupon {

	//クーポンの種類に着けられるID
	private Integer id;
	//割引料（円）
	private Integer discountPrice;
	//取得してから使用できる期間
	private Integer duration;
	//クーポン配布開始日
	private Timestamp startDistributionDate;
	//クーポン配布終了日
	private Timestamp finishDistributionDate;
	//クーポン使用開始日
	private Timestamp startUseDate;
	//クーポン使用終了日
	private Timestamp finishUseDate;
	//クーポンの適用開始金額
	private Integer startingAmount;
	// クーポンのイメージ画像
	private String couponImage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Integer discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Timestamp getStartDistributionDate() {
		return startDistributionDate;
	}

	public void setStartDistributionDate(Timestamp startDistributionDate) {
		this.startDistributionDate = startDistributionDate;
	}

	public Timestamp getFinishDistributionDate() {
		return finishDistributionDate;
	}

	public void setFinishDistributionDate(Timestamp finishDistributionDate) {
		this.finishDistributionDate = finishDistributionDate;
	}

	public Timestamp getStartUseDate() {
		return startUseDate;
	}

	public void setStartUseDate(Timestamp startUseDate) {
		this.startUseDate = startUseDate;
	}

	public Timestamp getFinishUseDate() {
		return finishUseDate;
	}

	public void setFinishUseDate(Timestamp finishUseDate) {
		this.finishUseDate = finishUseDate;
	}

	public Integer getStartingAmount() {
		return startingAmount;
	}

	public void setStartingAmount(Integer startingAmount) {
		this.startingAmount = startingAmount;
	}

	public String getCouponImage() {
		return couponImage;
	}

	public void setCouponImage(String couponImage) {
		this.couponImage = couponImage;
	}

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", discountPrice=" + discountPrice + ", duration=" + duration
				+ ", startDistributionDate=" + startDistributionDate + ", finishDistributionDate="
				+ finishDistributionDate + ", startUseDate=" + startUseDate + ", finishUseDate=" + finishUseDate
				+ ", startingAmount=" + startingAmount + ", couponImage=" + couponImage + "]";
	}


}
