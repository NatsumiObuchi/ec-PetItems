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

@Repository
public class AddresseeRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

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
	 * ユーザidから複数のお届け先情報を取得する
	 * 
	 * @param id
	 * @return
	 */
	public List<Addressee> addressees(Integer id) {
		String sql = "select id, user_id, addressee_id, zipCode, address, setting_addressee"
				+ " from addressees where user_id = :userId";
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
				+ " where user_id = :userId" + " order by addressee_id desc limit 1;";
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
	 * お届け先情報の更新
	 * 
	 * @param userId
	 * @param addreseeId
	 */
	public void updateAddressee(Addressee addressee) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(addressee);
		String sql = "update addressees set addressee_id = :addresseeId,"
				+ " zipCode = :zipCode, address = :address, setting_addressee = :settingAddressee"
				+ " where id = :id and user_id = :userId;";
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
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId)
				.addValue("addresseeId", addreseeId).addValue("settingAddressee", setting);
		template.update(sql, param);
	}

	/**
	 * デフォルトで表示するお届け先を検索
	 * 
	 * @param userId
	 * @param setting
	 * @return
	 */
	public Addressee findByUserIdandSettingAddresseeTrue(Integer userId) {
		String sql = "select id, user_id, addressee_id, zipCode, address, setting_addressee"
				+ " from addressees where user_id = :userId and setting_addressee = true;";
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

}
