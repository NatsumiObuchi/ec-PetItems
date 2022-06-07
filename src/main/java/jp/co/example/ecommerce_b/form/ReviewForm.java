package jp.co.example.ecommerce_b.form;

public class ReviewForm {
	private Integer item_id;// value
	private Integer user_id;// value
	private Integer stars;// value
	private String contents;// text

	public Integer getItem_Id() {
		return item_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public Integer getStars() {
		return stars;
	}

	public String getContents() {
		return contents;
	}

	public void setId(Integer item_id) {
		this.item_id = item_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}
