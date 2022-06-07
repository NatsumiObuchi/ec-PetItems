package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Favorite;

@Repository
public class FavoriteRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Favorite> FAVORITE_ROW_MAPPER = (rs, i) -> {
		Favorite favorite = new Favorite();
		favorite.setId(rs.getInt("id"));
		favorite.setItemId(rs.getInt("item_id"));
		favorite.setUserId(rs.getInt("user_id"));
		favorite.setFavoriteDate(rs.getDate("favorite_date"));
//		Item item = new Item();
//		item.setId(rs.getInt("id"));
//		item.setName(rs.getString("name"));
//		item.setDescription(rs.getString("description"));
//		item.setPrice(rs.getInt("price"));
//		item.setImagePath(rs.getString("image_path"));
//		item.setImagePath(rs.getString("image_path2"));
//		item.setDeleted(rs.getBoolean("deleted"));
//		favorite.setItem(item);
		return favorite;
	};

	/**
	 * お気に入りリストに追加する
	 * 
	 * @param favorite
	 */
	public void insertFavorite(Favorite favorite) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(favorite);

		String sql = "insert into favorites(user_id,item_id,favorite_date)" + " values(:userId,:itemId,:favoriteDate);";

		template.update(sql, param);
	}

	/**
	 * userIdとitemIdで既に登録済のお気に入りリストを取得する
	 * 
	 * @param itemId
	 * @return
	 */
	public Favorite findByUserIdItemId(Integer userId, Integer itemId) {
		String sql = "select id, user_id, item_id, favorite_date from favorites where user_id = :userId and item_id = :itemId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("itemId", itemId);
		List<Favorite> favoriteList = template.query(sql, param, FAVORITE_ROW_MAPPER);
		if (favoriteList.size() == 0) {
			return null;
		}
		return favoriteList.get(0);
	}

	/**
	 * userIdでお気に入りリストを取得する
	 * 
	 * @param userId
	 * @return
	 */
	public List<Favorite> favoriteAll(Integer userId) {
		String sql = "select id, user_id, item_id, favorite_date from favorites where user_id = :userId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<Favorite> favoriteList = template.query(sql, param, FAVORITE_ROW_MAPPER);
		if (favoriteList.size() == 0) {
			return null;
		}
		return favoriteList;
	}

	/**
	 * お気に入りリストから削除する
	 * 
	 * @param itemId
	 */
	public void deleteFavorite(Integer itemId) {
		String sql = "delete from favorites where item_id = :itemId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId);
		template.update(sql, param);
	}
}
