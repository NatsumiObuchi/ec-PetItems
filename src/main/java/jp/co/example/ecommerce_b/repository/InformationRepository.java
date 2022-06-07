package jp.co.example.ecommerce_b.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Information;

@Repository
public class InformationRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
//	今回RowMapper使わないが今後のため実装
//	private static final RowMapper<Information> ROW_MAPPER=(rs,i)->{
//		Information information=new Information();
//		information.setId(rs.getInt("id"));
//		information.setUserId(rs.getInt("user_id"));
//		information.setNickName(rs.getString("nick_name"));
//		information.setGender(rs.getString("gender"));
//		information.setEmail(rs.getString("email"));
//		information.setComment(rs.getString("comment"));
//		return information;
//	};
	
//	入力されたお問合せフォームをDBにINSERT(ログインしているuserはuserIdを保存、ログインしていない人はnull)
	public void insertInfo(Information information) {
		String sql="insert into informations (user_id,nick_name,gender,email,comment) VALUES (:userId,:nickName,:gender,:email,:comment);";
		SqlParameterSource param=new BeanPropertySqlParameterSource(information);
		template.update(sql, param);
	}

}
