package jp.co.example.ecommerce_b.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.OrderItem;

@Repository
public class OrderItemRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;

	public void insert(OrderItem orderItem) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);

		String sql = "INSERT INTO orderitems(item_id, order_id, quantity)" + "VALUES(:itemId, :orderId, :quantity)";

		template.update(sql, param);
	}
}
