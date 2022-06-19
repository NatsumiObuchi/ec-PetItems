package jp.co.example.ecommerce_b.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class InsertAddresseeForm {

	/** id */
	private Integer id;
	/** ユーザid */
	private Integer userId;
	/** お届け先id */
	private Integer addresseeId;
	/** 郵便番号 */
	@NotBlank(message = "郵便番号を入力してください")
	@Pattern(regexp = "^[0-9]{3}[0-9]{4}$", message = "郵便番号はハイフンなしの形式で入力してください")
	private String zipCode;
	/** お届け先住所 */
	@NotBlank(message = "住所を入力してください")
	private String address;

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

	public Integer getAddresseeId() {
		return addresseeId;
	}

	public void setAddresseeId(Integer addresseeId) {
		this.addresseeId = addresseeId;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "InsertAdressee [id=" + id + ", userId=" + userId + ", addresseeId=" + addresseeId + ", zipCode="
				+ zipCode + ", address=" + address + "]";
	}

}
