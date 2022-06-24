package jp.co.example.ecommerce_b.domain;

public class SortLabel {
	
	//並び替え順のid
	private Integer sortId;
	//並び替え順の名前（新着順・レビューが多い順等、、）
	private String sortText;
	//現在選択している並び替え順をtrue、選択していない並び替え順えおfalseにする
	private boolean decisionId;
	
	
	public SortLabel(Integer sortId, String sortText, boolean decisionId) {
		super();
		this.sortId = sortId;
		this.sortText = sortText;
		this.decisionId = decisionId;
	}


	public Integer getSortId() {
		return sortId;
	}


	public void setSortId(Integer sortId) {
		this.sortId = sortId;
	}


	public String getSortText() {
		return sortText;
	}


	public void setSortText(String sortText) {
		this.sortText = sortText;
	}


	public boolean isDecisionId() {
		return decisionId;
	}


	public void setDecisionId(boolean decisionId) {
		this.decisionId = decisionId;
	}


	@Override
	public String toString() {
		return "SortLabel [sortId=" + sortId + ", sortText=" + sortText + ", decisionId=" + decisionId + "]";
	}
	
}
