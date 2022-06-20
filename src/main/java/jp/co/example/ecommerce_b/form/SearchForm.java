package jp.co.example.ecommerce_b.form;

public class SearchForm {
	
	private String code;
	private Integer genre;
	private Integer sortId;
	
	public SearchForm() {}
	
	public SearchForm(String code, Integer genre, Integer sortId) {
		super();
		this.code = code;
		this.genre = genre;
		this.sortId = sortId;
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
	

	@Override
	public String toString() {
		return "SearchForm [code=" + code + ", genre=" + genre + ", sortId=" + sortId + "]";
	}
}
