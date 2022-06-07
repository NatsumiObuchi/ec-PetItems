package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Item;

@Repository
public class ItemRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * itemsテーブル操作用のROW_MAPPER
	 */
	private static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setDescription(rs.getString("description"));
		item.setPrice(rs.getInt("price"));
		item.setImagePath(rs.getString("image_path"));
		item.setImagePath2(rs.getString("image_path2"));
		item.setDeleted(rs.getBoolean("deleted"));
		return item;
	};

	/**
	 * 
	 * idでitemを検索するメソッド。 (item詳細表示用)
	 */
	public Item load(Integer id) {
		String sql = "SELECT id,name,description,price,image_path,image_path2,deleted FROM items WHERE id=:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);

		return item;
	}

	/**
	 * 
	 * 商品一覧を取得するメソッド。 (item一覧表示用)
	 */
	public List<Item> findAll() {
		String sql = "SELECT id,name,description,price,image_path,image_path2,deleted FROM items ORDER BY price ASC";
		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 
	 * 商品一覧をanimalIdを絞って取得するメソッド。 (item一覧表示用)
	 */
	public List<Item> findByAnimalId(Integer animalId) {
		String sql = "SELECT id,name,description,price,image_path,image_path2,deleted FROM items WHERE animal_id=:animalId ORDER BY price ASC";
		SqlParameterSource param = new MapSqlParameterSource().addValue("animalId", animalId);
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 
	 * 商品をあいまい検索するメソッド。(絞り込み：すべて)
	 */
	public List<Item> findByNameAndAnimalId(String name, Integer animalId) {
		String sql = "SELECT id,name,description,price,image_path,image_path2,deleted FROM items WHERE name LIKE :name ";
		String sql2 = "ORDER BY price ASC";
		String sql3 = "AND animal_id = :animalId ORDER BY price ASC";
		String sqlA = "";

		if (animalId == 0) {
			sqlA = sql + sql2;
		} else {
			sqlA = sql + sql3;
		}

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("animalId",
				animalId);
		List<Item> itemList = template.query(sqlA, param, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 
	 * 商品の絞り込み検索用メソッド。(検索値の入力なし)
	 */
	public List<Item> findByCategoryId(Integer animalId, Integer categoryId) {
		String sql = "SELECT id,name,description,price,image_path,image_path2,deleted FROM items WHERE category_id=:categoryId ";
		String sql2 = "ORDER BY price ASC";
		String sql3 = "AND animal_id = :animalId ORDER BY price ASC";
		String sqlA = "";

		if (animalId == 0) {
			sqlA = sql + sql2;
		} else {
			sqlA = sql + sql3;
		}
		SqlParameterSource param = new MapSqlParameterSource().addValue("animalId", animalId).addValue("categoryId",
				categoryId);
		List<Item> itemList = template.query(sqlA, param, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 
	 * 商品のあいまい検索と、絞り込みを同時に選択された場合のメソッド
	 */
	public List<Item> findByCategoryIdAndAnimaiIdAndName(String name, Integer animalId, Integer categoryId) {
		String sql = "SELECT id,name,description,price,image_path,image_path2,deleted FROM items WHERE name LIKE :name AND category_id=:categoryId ";
		String sql2 = "ORDER BY price ASC";
		String sql3 = "AND animal_id = :animalId ORDER BY price ASC";
		String sqlA = "";

		if (animalId == 0) {
			sqlA = sql + sql2;
		} else {
			sqlA = sql + sql3;
		}

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("animalId",
				animalId).addValue("categoryId", categoryId);
		List<Item> itemList = template.query(sqlA, param, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 
	 * 商品情報を更新するメソッド。
	 */
	public Item save(Item item) {

//		6/1 商品情報更新用メソッドについては一旦未実装
		return null;
	}

	/**
	 * 
	 * 商品情報を削除するメソッド。
	 */
	public void deleteById(Integer id) {
//		6/1　商品情報を削除するメソッドについては一旦未実装
	}
}
