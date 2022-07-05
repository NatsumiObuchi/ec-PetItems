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
	/** 画像パス2 */
	private String imagePath2;
	/** アニマルID */
	private Integer animalId;
	/** カテゴリID */
	private Integer categoryId;
	/** 削除フラグ */
	private Boolean deleted;
	/** 平均評価 */
	private String avgStar;
	/** 評価件数 */
	private Integer countReview;
	/** 評価件数 */
	private String avgstar2;

	/** アニマルメッセージ */
	private String animalMessage;
	/** カテゴリメッセージ */
	private String categoryMessage;
	
	
	
	public String getAnimalMessage() {
		return animalMessage;
	}

	public void setAnimalMessage(String animalMessage) {
		this.animalMessage = animalMessage;
	}

	public String getCategoryMessage() {
		return categoryMessage;
	}

	public void setCategoryMessage(String categoryMessage) {
		this.categoryMessage = categoryMessage;
	}

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
	
	public String getImagePath2() {
		return imagePath2;
	}

	public void setImagePath2(String imagePath2) {
		this.imagePath2 = imagePath2;
	}
	
	public Integer getAnimalId() {
		return animalId;
	}

	public void setAnimalId(Integer animalId) {
		this.animalId = animalId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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



	public String getAvgstar2() {
		return avgstar2;
	}

	public void setAvgstar2(String avgstar2) {
		this.avgstar2 = avgstar2;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", imagePath=" + imagePath + ", imagePath2=" + imagePath2 + ", animalId=" + animalId + ", categoryId="
				+ categoryId + ", deleted=" + deleted + ", avgStar=" + avgStar + ", countReview=" + countReview
				+ ", avgstar2=" + avgstar2 + ", animalMessage=" + animalMessage + ", categoryMessage=" + categoryMessage
				+ "]";
	}

	
	
}
