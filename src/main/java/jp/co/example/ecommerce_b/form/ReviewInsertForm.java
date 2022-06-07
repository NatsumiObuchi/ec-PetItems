package jp.co.example.ecommerce_b.form;

public class ReviewInsertForm {
	private String times;
	private String user_id_min;
	private String user_id_max;
	private String item_id_min;
	private String item_id_max;
	private String star_min;
	private String star_max;

	public String getTimes() {
		return times;
	}

	public String getUser_id_min() {
		return user_id_min;
	}

	public String getUser_id_max() {
		return user_id_max;
	}

	public String getItem_id_min() {
		return item_id_min;
	}

	public String getItem_id_max() {
		return item_id_max;
	}

	public String getStar_min() {
		return star_min;
	}

	public String getStar_max() {
		return star_max;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public void setUser_id_min(String user_id_min) {
		this.user_id_min = user_id_min;
	}

	public void setUser_id_max(String user_id_max) {
		this.user_id_max = user_id_max;
	}

	public void setItem_id_min(String item_id_min) {
		this.item_id_min = item_id_min;
	}

	public void setItem_id_max(String item_id_max) {
		this.item_id_max = item_id_max;
	}

	public void setStar_min(String star_min) {
		this.star_min = star_min;
	}

	public void setStar_max(String star_max) {
		this.star_max = star_max;
	}

	@Override
	public String toString() {
		return "ReviewForm [times=" + times + ", user_id_min=" + user_id_min + ", user_id_max=" + user_id_max
				+ ", item_id_min=" + item_id_min + ", item_id_max=" + item_id_max + ", star_min=" + star_min
				+ ", star_max=" + star_max + "]";
	}
}
