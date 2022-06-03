package jp.co.example.ecommerce_b.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderHistory;




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
		
		
		String sql = "INSERT INTO orders(user_id,status,total_price,order_date,destination_name, "
					+ "destination_email,destinationzip_code,destination_address,destination_tell, "
					+ "delivery_time,payment_method)"
					+ "VALUES(:userId, :status, :totalPrice, :orderDate, :destinationName, :destinationEmail, "
					+ ":destinationzipCode, :destinationAddress, :destinationTell, :deliveryTime, :paymentMethod)";
			
		template.update(sql, param);
	}
	
	/**
	 * 注文履歴テーブルにインサートする
	 *
	 */
	public void insertHistory(OrderHistory orderHistory) {
		String sql="insert into orderhistorys (order_id,item_name,item_price,quantity)"
				+ " VALUES (:order,:itemName,:itemPrice,:quantity);";
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderHistory);
		
		template.update(sql, param);
	}
	
}
