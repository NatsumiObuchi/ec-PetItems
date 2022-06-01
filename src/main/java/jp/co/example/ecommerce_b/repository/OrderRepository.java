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
	public void insert(Order order){
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		
		
		String insertSql = "INSERT INTO orders(user_id,status,total_price,order_date,destination_name, "
					+ "destination_email,destinationzip_code,destination_address,destination_tell, "
					+ "delivery_time,payment_method)"
					+ "VALUES(:userId, :status, :totalPrice, :orderDate, :destinationName, :destinationEmail, "
					+ ":destinationzipCode, :destinationAddress, :destinationTell, :deliveryTime, :paymentMethod)";
			
		template.update(insertSql, param);
	}
	
}
