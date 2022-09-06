package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.OrderItem;

@Repository
public class OrderItemRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<OrderItem> ORDERITEM_ROW_MAPPER = (rs, i) -> {
		OrderItem orderItem = new OrderItem();
		orderItem.setId(rs.getInt("id"));
		orderItem.setItemId(rs.getInt("item_id"));
		orderItem.setOrderId(rs.getInt("order_id"));
		orderItem.setQuantity(rs.getInt("quantity"));
		Item item = new Item();
		item.setId(rs.getInt("item_id"));
		item.setName(rs.getString("name"));
		item.setDescription(rs.getString("description"));
		item.setPrice(rs.getInt("price"));
		item.setImagePath(rs.getString("image_path"));
		item.setImagePath2(rs.getString("image_path2"));
		item.setDeleted(rs.getBoolean("deleted"));
		orderItem.setItem(item);
		return orderItem;
	};

	/**
	 * orderのidからorderItemの情報を取得する
	 * 
	 * @param id
	 * @return
	 */
	public List<OrderItem> findByOrderId(Integer id) {
		String sql = "select o.id, o.item_id, o.order_id, o.quantity,"
				+ " i.id as item_id, i.name, i.description, i.price, i.image_path, i.image_path2, i.deleted"
				+ " from orderitems as o" + " inner join items as i" + " on o.item_id = i.id"
				+ " where o.order_id = :id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<OrderItem> orderItemList = template.query(sql, param, ORDERITEM_ROW_MAPPER);
		if (orderItemList.size() == 0) {
			return null;
		}
		return orderItemList;
	}

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

	/**
	 * 同じ商品をカートに追加した場合、orderItemsの個数(quantity)を更新する
	 * 
	 * @param orderItem
	 */
	public void update(OrderItem orderItem) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);
		String updateSql = "UPDATE orderItems SET quantity = :quantity WHERE item_id = :itemId";
		template.update(updateSql, param);
	}
}
