package jp.co.example.ecommerce_b.form;

import org.springframework.ui.Model;

public class SearchForm {
	
	//検索欄に入力された文字列
	private String code;
	//カテゴリーのid
	private Integer genre;
	//並び替えのid
	private Integer sortId;

	//並び替えのリンクの分岐処理
	public void setLink(Model model) {
		String setLink = null;
		if (code==null || code.isEmpty()) {
			setLink = "&genre=" + genre;
		} else {
			setLink = "&genre=" + genre + "&code="+ code; 
		} 
		System.out.println(setLink);
		model.addAttribute("setLink", setLink);
	}

	public SearchForm() {
	}

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
