package jp.co.example.ecommerce_b.domain;

public class Point {

	/** id */
	private Integer id;
	/** ユーザーid */
	private Integer userId;
	/** ポイント */
	private Integer point;
	private User user;

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
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Point [id=" + id + ", userId=" + userId + ", point=" + point + ", user=" + user + "]";
	}


}
