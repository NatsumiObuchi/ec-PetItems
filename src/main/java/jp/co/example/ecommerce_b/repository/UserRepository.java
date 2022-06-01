package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;

@Repository
public class UserRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setZipcode(rs.getString("zipcode"));
		user.setAddress(rs.getString("address"));
		user.setTelephone(rs.getString("telephone"));
		return user;
	};

	/**
	 * @param user ユーザーを追加する
	 */
	public void insertUser(User user) {
		String sql = "INSERT INTO users (name,email,password,zipcode,address,telephone) VALUES (:name,:email,:password,:zipcode,:address,:telephone)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", user.getName())
				.addValue("email", user.getEmail()).addValue("password", user.getPassword())
				.addValue("zipcode", user.getZipcode()).addValue("address", user.getAddress())
				.addValue("telephone", user.getTelephone());
		template.update(sql, param);
	}

	/**
	 * @param form
	 * @return 入力されたメールアドレスが既に登録されているか確認する
	 */
	public Boolean findByMailAddress(UserForm form) {
		String sql = "SELECT id,name,email,password,zipcode,address,telephone FROM users WHERE email = :email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", form.getEmail());
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return false;// メールアドレスが重複していなければfalse
		} else {
			return true;// メールアドレスが重複していればtrue
		}
	}

}
