package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderHistory;




@Repository
public class OrderRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	
	private static final RowMapper<OrderHistory> HIS_ROW_MAPPER=(rs,i)->{
		OrderHistory orderHistory =new OrderHistory();
		orderHistory.setId(rs.getInt("id"));
		orderHistory.setOrderId(rs.getInt("order_id"));
		orderHistory.setItemName(rs.getString("item_name"));
		orderHistory.setItemPrice(rs.getInt("item_price"));
		orderHistory.setQueantity(rs.getInt("quantity"));
		orderHistory.setTotalPrice(rs.getInt("total_price"));
		orderHistory.setOrderDate(rs.getDate("order_date"));
		orderHistory.setDestinationName(rs.getString("destination_name"));
		orderHistory.setDestinationEmail(rs.getString("destination_email"));
		orderHistory.setDestinationzipCode(rs.getString("destinationzip_Code"));
		orderHistory.setDestinationAddress(rs.getString("destination_address"));
		orderHistory.setDestinationTell(rs.getString("destination_tell"));
		orderHistory.setDeliveryTime(rs.getTimestamp("delivery_time"));
		orderHistory.setPaymentMethod(rs.getInt("payment_method"));

		return orderHistory;
	};
	

	/**
	 * 注文する
	 *
	 */
	public void update(Order order){
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		
//		if(order.getStatus() == 0 && ) {
//			String sql= "UPDATE orders SET destination_name = :destinationName, destination_email = :destinationEmail,"
//					+ "destinationzip_code = :destinationzipCode, destination_address = :destinationAddress, "
//					+ "destination_tell = :destinationTell, delivery_time = :deliveryTime,"
//					+ "payment_method = :paymentMethod WHERE order_id = :orderId";
//			
//			template.update(sql, param);
//		}
	
	}
	
	/**
	 * 注文履歴テーブルにインサートする
	 *
	 */
	public void insertHistory(OrderHistory orderHistory) {
		String sql="insert into order_histories (order_id,image_path,item_name,item_price,quantity,total_price,order_date,"
				+ "destination_name,destination_email,destinationzip_Code,destination_address,destination_tell,"
				+ "delivery_time,delivery_time,payment_method)"
				+ "VALUES (:orderId,:imagePath,:itemName,:itemPrice,:quantity,:totalPrice,:orderDate,:destinationName"
				+ ":destinationEmail,:destinationZipcode,:destinationAddress,:destinationTell,"
				+ ":deliveryTime,:paymentMethod);";
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderHistory);
		
		template.update(sql, param);
	}
	
	/**
	 * 注文履歴をorderIdで取り出す
	 *
	 */
	public List<OrderHistory> findOrderHistory(Integer orderId){
		String sql="SELECT * FROM order_histories WHERE order_id=:orderId";
		
		SqlParameterSource param=new MapSqlParameterSource().addValue("orderId", orderId);	
		List<OrderHistory> historyList=template.query(sql, param,HIS_ROW_MAPPER);
		
		return historyList;
		
	}
	
	
	
	
	
	
	
	
	
}
