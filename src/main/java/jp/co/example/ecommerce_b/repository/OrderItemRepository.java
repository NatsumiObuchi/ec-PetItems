package jp.co.example.ecommerce_b.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.OrderItem;

@Repository
public class OrderItemRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * @param orderItem
	 * @return OrderItemをDBに格納し、シリアルidを格納したOrderItemを返す
	 */
	public OrderItem insert(OrderItem orderItem) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);

		String sql = "INSERT INTO orderitems(item_id, order_id, quantity)"
				+ "VALUES(:itemId, :orderId, :quantity) returning id";

		KeyHolder keyholder = new GeneratedKeyHolder();
		template.update(sql, param, keyholder);
		Integer id = (Integer) keyholder.getKey();
		orderItem.setId(id);
		return orderItem;
	}

	/**
	 * @param id 削除したいOrderItemのidを引数として、orderitemsテーブルから一致するOrderItemを削除する。
	 */
	public void delete(int id) {
		String sql = "DELETE FROM orderitems WHERE id=:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}
}
