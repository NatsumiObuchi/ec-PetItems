package jp.co.example.ecommerce_b.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Order;




@Repository
public class OrderRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
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
	
}
