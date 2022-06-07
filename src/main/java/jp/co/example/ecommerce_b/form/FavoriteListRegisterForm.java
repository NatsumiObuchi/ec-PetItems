package jp.co.example.ecommerce_b.form;

public class FavoriteListRegisterForm {

	/** id */
	private String id;
	/** ユーザーid */
	private String userId;
	/** 商品id */
	private String itemId;
	/** 注文日 */
	private String favoriteDate;

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

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getFavoriteDate() {
		return favoriteDate;
	}

	public void setFavoriteDate(String favoriteDate) {
		this.favoriteDate = favoriteDate;
	}

	@Override
	public String toString() {
		return "FavoriteListRegisterForm [id=" + id + ", userId=" + userId + ", itemId=" + itemId + ", favoriteDate="
				+ favoriteDate + "]";
	}

}
