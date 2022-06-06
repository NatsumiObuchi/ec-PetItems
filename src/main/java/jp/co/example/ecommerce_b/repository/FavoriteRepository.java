package jp.co.example.ecommerce_b.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Favorite;

@Repository
public class FavoriteRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * お気に入りリストに追加する
	 * 
	 * @param favorite
	 */
	public void insertFavorite(Favorite favorite) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(favorite);

		String sql = "insert into favorites(id,user_id,item_id,favorite_date)"
				+ " values(:id,:userId,:itemId,:favoriteDate);";

		template.update(sql, param);
	}

}
