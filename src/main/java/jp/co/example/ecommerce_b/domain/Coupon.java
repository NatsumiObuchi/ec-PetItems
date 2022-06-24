package jp.co.example.ecommerce_b.domain;

import java.sql.Timestamp;

public class Coupon {

	private Integer id;
	private Integer discountPrice;
	private Integer duration;
	private Timestamp startDistributionDate;
	private Timestamp finishDistributionDate;
	private Timestamp startUseDate;
	private Timestamp finishUseDate;
	private Integer startingAmount;
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
	@Override
	public String toString() {
		return "Coupon [id=" + id + ", discountPrice=" + discountPrice + ", duration=" + duration
				+ ", startDistributionDate=" + startDistributionDate + ", finishDistributionDate="
				+ finishDistributionDate + ", startUseDate=" + startUseDate + ", finishUseDate=" + finishUseDate
				+ ", startingAmount=" + startingAmount + "]";
	}

	
}
