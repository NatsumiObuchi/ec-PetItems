package jp.co.example.ecommerce_b.domain;

import java.util.Date;

public class Favorite {

	/** id */
	private Integer id;
	/** ユーザーid */
	private Integer userId;
	/** 商品id */
	private Integer itemId;
	/** 注文日 */
	private Date favoriteDate;
	private Item item;

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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Date getFavoriteDate() {
		return favoriteDate;
	}

	public void setFavoriteDate(Date favoriteDate) {
		this.favoriteDate = favoriteDate;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Favorite [id=" + id + ", userId=" + userId + ", itemId=" + itemId + ", favoriteDate=" + favoriteDate
				+ ", item=" + item + "]";
	}

}
