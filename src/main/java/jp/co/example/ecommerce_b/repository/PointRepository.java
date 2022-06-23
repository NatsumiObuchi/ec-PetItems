package jp.co.example.ecommerce_b.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Point;
import jp.co.example.ecommerce_b.domain.User;

@Repository
public class PointRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Point> POINT_ROW_MAPPER = (rs, i) -> {
		Point point = new Point();
		point.setId(rs.getInt("id"));
		point.setUserId(rs.getInt("user_id"));
		point.setPoint(rs.getInt("point"));
		User user = new User();
//		user.setId(rs.getInt("id"));
//		user.setName(rs.getString("name"));
//		user.setEmail(rs.getString("email"));
//		user.setPassword(rs.getString("password"));
//		user.setZipcode(rs.getString("zipcode"));
//		user.setAddress(rs.getString("address"));
//		user.setTelephone(rs.getString("telephone"));
//		point.setUser(user);
		return point;
	};

	/**
	 * ユーザのポイント情報を取得する
	 * 
	 * @param userId
	 * @return
	 */
	public Point load(Integer userId) {
		String sql = "select id, user_id, point from points where user_id = :userId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		Point point = template.queryForObject(sql, param, POINT_ROW_MAPPER);
		if (point == null) {
			return null;
		}
		return point;
	}

	/**
	 * ポイント情報を会員登録時に登録する
	 * 
	 * @param point
	 */
	public void insertPoint(Point point) {
		String sql = "insert into points(user_id, point) values(:userId, :point)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", point.getUserId()).addValue("point",
				point.getPoint());
		template.update(sql, param);
	}
}
