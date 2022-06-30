package jp.co.example.ecommerce_b.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.DiscountedHistory;

@Repository
public class DiscountedHistoryRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * ポイントもしくはクーポンを使用した際に履歴がインサートされる処理
	 * 
	 * @param discountedHistory
	 */
	public void insert(DiscountedHistory discountedHistory) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(discountedHistory);
		String sql = "insert into discounted_histories(order_id, discount_price)" + " values(:orderId, :discountPrice)";
		template.update(sql, param);
	}

}
