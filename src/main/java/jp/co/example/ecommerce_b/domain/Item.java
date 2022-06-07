package jp.co.example.ecommerce_b.domain;

public class Item {

	/** id */
	private Integer id;
	/** 商品名 */
	private String name;
	/** 商品説明 */
	private String description;
	/** 商品価格 */
	private Integer price;
	/** 画像パス */
	private String imagePath;
	private String imagePath2;

	private Integer animal_id;
	private Integer category_id;
	/** 削除フラグ */
	private Boolean deleted;
	/** 平均評価 */
	private String avgStar;
	/** 評価件数 */
	private Integer countReview;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", imagePath=" + imagePath + ", deleted=" + deleted + ", avgStar=" + avgStar + ", countReview="
				+ countReview + "]";
	}



	public Integer getCountReview() {
		return countReview;
	}



	public void setCountReview(Integer countReview) {
		this.countReview = countReview;
	}

	public String getAvgStar() {
		return avgStar;
	}

	public void setAvgStar(String avgStar) {
		this.avgStar = avgStar;
	}

	public Integer getAnimal_id() {
		return animal_id;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setAnimal_id(Integer animal_id) {
		this.animal_id = animal_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public String getImagePath2() {
		return imagePath2;
	}

	public void setImagePath2(String imagePath2) {
		this.imagePath2 = imagePath2;
	}

}
