package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Coupon;
import jp.co.example.ecommerce_b.domain.UsersCoupon;
import jp.co.example.ecommerce_b.domain.UsersCouponHistory;

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
	
	private static final RowMapper<UsersCoupon> USERS_COUPON_ROW_MAPPER = (rs,i) -> {
		UsersCoupon usersCoupon = new UsersCoupon();
		usersCoupon.setId(rs.getInt("id"));
		usersCoupon.setUserId(rs.getInt("user_id"));
		usersCoupon.setCouponId(rs.getInt("coupon_id"));
		usersCoupon.setCouponGetDate(rs.getTimestamp("coupon_get_date"));
		usersCoupon.setCouponExpirationDate(rs.getTimestamp("coupon_expiration_date"));
		Coupon coupon = new Coupon();
		coupon.setId(rs.getInt("id"));
		coupon.setDiscountPrice(rs.getInt("discount_price"));
		coupon.setDuration(rs.getInt("duration"));
		coupon.setStartDistributionDate(rs.getTimestamp("start_distribution_date"));
		coupon.setFinishDistributionDate(rs.getTimestamp("finish_distribution_date"));
		coupon.setStartUseDate(rs.getTimestamp("start_use_date"));
		coupon.setFinishUseDate(rs.getTimestamp("finish_use_date"));
		coupon.setStartingAmount(rs.getInt("starting_amount"));
		usersCoupon.setCoupon(coupon);
		return usersCoupon;
	};
	
	/**
	 * 取得可能な全クーポンの一覧を取得する
	 * @return　
	 */
	public List<Coupon> findAllCoupon(){
		
		String sql = "SELECT id,discount_price,duration,start_distribution_date,finish_distribution_date,"
				+ "start_use_date,finish_use_date,starting_amount from coupon;";
		
		List<Coupon> couponList = template.query(sql, COUPON_ROW_MAPPER);
		return couponList;
	}
	
	/**
	 * ユーザーがクーポンを取得したときに、クーポン情報をインサートする
	 * @param usersCoupon
	 */
	public void insertUsersCoupon(UsersCoupon usersCoupon) {
		System.out.println(usersCoupon.getUserId());
		
		String sql ="INSERT INTO users_coupon (user_id, coupon_id, coupon_get_date, coupon_expiration_date)"
				+ " VALUES(:userId, :couponId, :couponGetDate, :couponExpirationDate)";
		SqlParameterSource param = new MapSqlParameterSource()
				.addValue("userId", usersCoupon.getUserId())
				.addValue("couponId", usersCoupon.getCouponId())
				.addValue("couponGetDate", usersCoupon.getCouponGetDate())
				.addValue("couponExpirationDate", usersCoupon.getCouponExpirationDate());
		template.update(sql, param);
	}
	
	
	/**
	 * ユーザーが利用可能な全クーポンの一覧を取得する(couponテーブルとusers_couponを結合)
	 * @return
	 */
	public List<UsersCoupon> findAllUsersCoupon(Integer userId){
		String sql = "SELECT u.id, u.user_id, u.coupon_id, u.coupon_get_date, u.coupon_expiration_date, "
				+ "c.id,c.discount_price,c.duration,c.start_distribution_date,c.finish_distribution_date, "
				+ "c.start_use_date,c.finish_use_date,c.starting_amount "
				+ "from users_coupon u LEFT JOIN coupon c ON c.id = u.coupon_id WHERE user_id=:userId AND deleted=false;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<UsersCoupon> usersCouponList = template.query(sql, param, USERS_COUPON_ROW_MAPPER);
		return usersCouponList;
	}
	
	
	/**
	 * 使用したクーポンのdeletedをtrueにする
	 * @param id ユーザーが保持しているクーポンの一意のid
	 */
	public void usedUsersCoupon(Integer id) {
		String sql = "UPDATE users_coupon SET deleted = true WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}
	
	
	/**
	 *  ユーザーが購入後、クーポンの使用履歴をインサート
	 * @param usersCouponHistory 
	 */
	public void insertUsersCouponHistorys(UsersCouponHistory usersCouponHistory) {
		String sql = "INSERT INTO users_coupon_historys (user_id, order_historys_id, coupon_id, coupon_get_date, coupon_expiration_date)"
				+ " VALUES(:userId, :orderHistorysId, :couponId, :couponGetDate, :couponExpirationDate);";
	SqlParameterSource param = new BeanPropertySqlParameterSource(usersCouponHistory);
	template.update(sql, param);
	}
	

}
