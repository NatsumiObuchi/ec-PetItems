package jp.co.example.ecommerce_b.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class InformationForm {
	
	private String id;
	private String userId;
	@NotBlank(message="お名前をご入力ください")
	private String nickName;
	@NotBlank(message="メールアドレスをご入力ください")
	@Email(message="正しい形式でご入力ください")
	private String email;
	@NotEmpty(message="性別を選択してください")
	private String gender;
	@NotBlank(message="お問い合わせ内容をご入力ください")
	private String comment;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "InformationForm [id=" + id + ", userId=" + userId + ", nickName=" + nickName + ", email=" + email
				+ ", gender=" + gender + ", comment=" + comment + "]";
	}
	

}
