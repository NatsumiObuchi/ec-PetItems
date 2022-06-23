package jp.co.example.ecommerce_b.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setZipcode(rs.getString("zipcode"));
		user.setAddress(rs.getString("address"));
		user.setTelephone(rs.getString("telephone"));
		point.setUser(user);
		return point;
	};

}
