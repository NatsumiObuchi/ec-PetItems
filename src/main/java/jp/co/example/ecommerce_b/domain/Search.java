package jp.co.example.ecommerce_b.domain;

public class Search {

	// 検索欄に入力された文字列
	private String code;
	// カテゴリーのid
	private Integer genre;
	// アニマルのid
	private Integer animalId;
	//　カテゴリーのid
	private Integer categoryId;
	// 並び替えのid
	private Integer sortId;
	// 検索結果画面に返す文字列
	private String searchMessage;

	
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getGenre() {
		return genre;
	}

	public void setGenre(Integer genre) {
		this.genre = genre;
	}

	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}

	public String getSearchMessage() {
		return searchMessage;
	}

	public void setSearchMessage(String searchMessage) {
		this.searchMessage = searchMessage;
	}

	@Override
	public String toString() {
		return "Search [code=" + code + ", genre=" + genre + ", animalId=" + animalId + ", categoryId=" + categoryId
				+ ", sortId=" + sortId + ", searchMessage=" + searchMessage + "]";
	}

	
}
