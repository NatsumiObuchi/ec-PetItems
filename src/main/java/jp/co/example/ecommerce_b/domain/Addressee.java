package jp.co.example.ecommerce_b.domain;

public class Addressee {

	/** id */
	private Integer id;
	/** ユーザid */
	private Integer userId;
	/** お届け先id */
	private Integer addresseeId;
	/** 郵便番号 */
	private String zipCode;
	/** お届け先住所 */
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
		return "Addressee [id=" + id + ", userId=" + userId + ", addresseeId=" + addresseeId + ", zipCode=" + zipCode
				+ ", address=" + address + "]";
	}

}
