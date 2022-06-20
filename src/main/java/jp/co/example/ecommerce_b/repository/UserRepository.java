package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Addressee;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.form.UserUpdateForm;

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

	private static final RowMapper<Addressee> ADDRESSEE_ROW_MAPPER = (rs, i) -> {
		Addressee addressee = new Addressee();
		addressee.setId(rs.getInt("id"));
		addressee.setUserId(rs.getInt("user_id"));
		addressee.setAddresseeId(rs.getInt("addressee_id"));
		addressee.setZipCode(rs.getString("zipCode"));
		addressee.setAddress(rs.getString("address"));
		addressee.setSettingAddressee(rs.getBoolean("setting_addressee"));
		return addressee;
	};

	/**
	 * @param form
	 * @return UserFormの郵便番号を変更するメソッド
	 *         ユースケースの制約上「ハイフンありの８桁」で入力を受け取るが、DBの制約上「ハイフンなしの７桁」で登録する必要がある。
	 */
	private UserForm modifyZipcode(UserForm form) {
		String zipcode8 = form.getZipcode();
		String zipcode7 = "";
		for (int i = 0; i <= 7; i++) {
			if (i != 3) {// ハイフンの時以外
				zipcode7 += zipcode8.charAt(i);
			}
		}
		form.setZipcode(zipcode7);
		return form;
	}

	/**
	 * @param user ユーザーを追加する
	 */
	public void insertUser(User user) {
//		user = modifyZipcode(user);
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

	/**
	 * @param form
	 * @return 入力されたメールアドレスが既に登録されているか確認する（ユーザ情報変更時）
	 */
	public Boolean findByMailAddress2(UserUpdateForm form) {
		String sql = "SELECT id,name,email,password,zipcode,address,telephone FROM users WHERE email = :email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", form.getEmail());
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return false;// メールアドレスが重複していなければfalse
		} else {
			return true;// メールアドレスが重複していればtrue
		}
	}

	/**
	 * @param form
	 * @return 入力されたメールアドレスとパスワードからユーザーを検索する。
	 */
	public User findByEmailAndPassword(UserForm form) {
		String sql = "SELECT id,name,email,password,zipcode,address,telephone FROM users WHERE email = :email AND password = :password";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", form.getEmail()).addValue("password",
				form.getPassword());
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}

	/**
	 * @param form
	 * @return 入力されたメールアドレスからユーザーを検索する。(パスワード一致確認のため)
	 */
	public User findByEmail(UserForm form) {
		String sql = "SELECT id,name,email,password,zipcode,address,telephone FROM users WHERE email = :email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", form.getEmail());
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if (userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}

	/**
	 * ユーザ情報を更新する ※パスワード以外は全て更新される
	 * 
	 * @param user 登録済のユーザ情報
	 * @return 更新されたユーザ情報
	 */
	public void update(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "update users set name = :name, email = :email,  "
				+ " zipcode = :zipcode, address = :address, telephone = :telephone where id = :id";
		template.update(sql, param);
	}

	/**
	 * ユーザのパスワードを変更する
	 * 
	 * @param user
	 */
	public void updatePassword(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "update users set password = :password where id = :id";
		template.update(sql, param);
	}

	/**
	 * ユーザ情報を削除する
	 * 
	 * @param id
	 */
	public void delete(Integer id) {
		String sql = "delete from users where id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}

	/**
	 * ユーザidから複数のお届け先情報を取得する
	 * 
	 * @param id
	 * @return
	 */
	public List<Addressee> addressees(Integer id) {
		String sql = "select id, user_id, addressee_id, zipCode, address, setting_addressee"
				+ " from addressees where user_id = :userId order by id desc";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", id);
		List<Addressee> addresseeList = template.query(sql, param, ADDRESSEE_ROW_MAPPER);
		if (addresseeList == null) {
			return null;
		}
		return addresseeList;
	}

	/**
	 * ユーザIDとaddresseeIdでお届け先情報を検索する
	 * 
	 * @param userId
	 * @param addreseeId
	 * @return
	 */
	public Addressee addressee(Integer userId, Integer addreseeId) {
		String sql = "select id, user_id, addressee_id, zipCode, address, setting_addressee"
				+ " from addressees where user_id = :userId and addressee_id = :addresseeId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("addresseeId",
				addreseeId);
		Addressee addressee = template.queryForObject(sql, param, ADDRESSEE_ROW_MAPPER);
		if (addressee == null) {
			return null;
		}
		return addressee;
	}

	/**
	 * ユーザが最後に追加したお届け先情報を取得
	 * 
	 * @param userId
	 * @return
	 */
	public Addressee lastAddreseeId(Integer userId) {
		String sql = "select id, user_id, addressee_id, zipCode, address, setting_addressee" + " from addressees"
				+ " where user_id = :userId"
				+ " order by addressee_id desc limit 1;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		try {
			Addressee addressee = template.queryForObject(sql, param, ADDRESSEE_ROW_MAPPER);
			return addressee;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * お届け先情報を追加する
	 * 
	 * @param addresee
	 */
	public Addressee addresseeRegister(Addressee addresee) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(addresee);
		String sql = "insert into addressees (user_id, addressee_id, zipcode, address)"
				+ " values(:userId, :addresseeId, :zipCode, :address)";
		template.update(sql, param);

		return addresee;
	}

	/**
	 * お届け先情報を削除する
	 * 
	 * @param userId
	 * @param addreseeId
	 */
	public void deleteAddressee(Integer userId, Integer addreseeId) {
		String sql = "delete from addressees where user_id = :userId and addressee_id = :addresseeId;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("addresseeId",
				addreseeId);
		template.update(sql, param);
	}

	/**
	 * お届け先情報として設定する(setting_addresseeをtrueにする)
	 * 
	 * @param userId
	 * @param addreseeId
	 */
	public void settingAddrssee(Integer userId, Integer addreseeId, boolean setting) {
		String sql = "update addressees set setting_addressee = :settingAddressee where user_id = :userId and addressee_id = :addresseeId;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("addresseeId",
				addreseeId).addValue("settingAddressee", setting);
		template.update(sql, param);
	}

}
