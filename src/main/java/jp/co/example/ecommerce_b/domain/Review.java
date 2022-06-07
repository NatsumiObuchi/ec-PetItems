package jp.co.example.ecommerce_b.domain;

public class Review {
	private Integer id;
	private Integer item_id;// value
	private Integer user_id;// value
	private Integer stars;// value
	private String content;// text

	public Integer getId() {
		return id;
	}

	public Integer getItem_id() {
		return item_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public Integer getStars() {
		return stars;
	}
	public String getContent() {
		return content;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Review [id=" + id + ", item_id=" + item_id + ", user_id=" + user_id + ", stars=" + stars + ", content="
				+ content + "]";
	}


}
