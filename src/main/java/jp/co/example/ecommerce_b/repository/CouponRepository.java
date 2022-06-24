package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Coupon;

@Repository
public class CouponRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Coupon> COUPON_ROW_MAPPER = (rs,i) -> {
		Coupon coupon = new Coupon();
		coupon.setId(rs.getInt("id"));
		coupon.setDiscountPrice(rs.getInt("discount_price"));
		coupon.setDuration(rs.getInt("duration"));
		coupon.setStartDistributionDate(rs.getTimestamp("start_distribution_date"));
		coupon.setFinishDistributionDate(rs.getTimestamp("finish_distribution_date"));
		coupon.setStartUseDate(rs.getTimestamp("start_use_date"));
		coupon.setFinishUseDate(rs.getTimestamp("finish_use_date"));
		coupon.setStartingAmount(rs.getInt("starting_amount"));
		return coupon;
	};
	
	
	/**
	 * 取得可能な全クーポンの一覧を取得する
	 * @return　
	 */
	public List<Coupon> findAll(){
		
		String sql = "SELECT id,discount_price,duration,start_distribution_date,finish_distribution_date,"
				+ "start_use_date,finish_use_date,starting_amount from coupon;";
		
		List<Coupon> couponList = template.query(sql, COUPON_ROW_MAPPER);
		return couponList;
		
	}

}
