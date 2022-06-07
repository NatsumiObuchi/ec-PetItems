package jp.co.example.ecommerce_b.domain;

public class Information {
	
	private Integer id;
	private Integer userId;
	private String nickName;
	private String email;
	private Integer age;
	private String comment;
	
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
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "Information [id=" + id + ", userId=" + userId + ", nickName=" + nickName + ", email=" + email + ", age="
				+ age + ", comment=" + comment + "]";
	}
}
