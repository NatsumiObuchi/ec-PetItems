package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.DiscountedHistory;
import jp.co.example.ecommerce_b.repository.DiscountedHistoryRepository;

@Service
public class DiscountedHistoryService {

	@Autowired
	private DiscountedHistoryRepository repository;

	/**
	 * ポイントもしくはクーポンを使用した際に履歴がインサートされる処理
	 * 
	 * @param discountedHistory
	 */
	public void insert(DiscountedHistory discountedHistory) {
		repository.insert(discountedHistory);
	}

}
