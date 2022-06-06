package jp.co.example.ecommerce_b.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		item.setDeleted(rs.getBoolean("deleted"));
		return item;
	};

	/**
	 * 
	 * idでitemを検索するメソッド。 (item詳細表示用)
	 */
	public Item load(Integer id) {
		String sql = "SELECT id,name,description,price,image_path,deleted FROM items WHERE id=:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);

		return item;
	}

	/**
	 * 
	 * 商品一覧を取得するメソッド。 (item一覧表示用)
	 */
	public List<Item> findAll() {
		String sql = "SELECT id,name,description,price,image_path,deleted FROM items ORDER BY price ASC";
		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 
	 * 商品をあいまい検索するメソッド。(絞り込み：すべて)
	 */
	public List<Item> findByName(String name) {
		String sql = "SELECT id,name,description,price,image_path,deleted FROM items WHERE name LIKE :name ORDER BY price ASC";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 
	 * 商品の絞り込み検索用メソッド。(検索値の入力なし)
	 */
	public List<Item> findByAnimalId(Integer animalId) {
		String sql = "SELECT id,name,description,price,image_path,deleted FROM items WHERE animal_id=:animalId ORDER BY price ASC";
		SqlParameterSource param = new MapSqlParameterSource().addValue("animalId", animalId);
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 
	 * 商品のあいまい検索と、絞り込みを同時に選択された場合のメソッド
	 */
	public List<Item> findByNameAndAnimalId(String name, Integer animalId) {
		String sql = "SELECT id,name,description,price,image_path,deleted FROM items WHERE name LIKE :name AND animal_id=:animalId ORDER BY price ASC";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("animalId",
				animalId);
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
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

	public void insertRecordsIntoValues(int user_id, int item_id, int star) {
		// リスト作成が目的
		List<Integer> user_ids = new ArrayList<>();
		List<Integer> item_ids = new ArrayList<>();
		List<Integer> stars = new ArrayList<>();
		List<String> contents = new ArrayList<>();

		for (int i = 1; i <= user_id; i++) {
			user_ids.add(i);
		}

		for (int i = 1; i <= item_id; i++) {
			item_ids.add(i);
		}

		for (int i = 0; i <= star; i++) {
			stars.add(i);
		}

		contents.add("コメントA");
		contents.add("コメントB");
		contents.add("コメントC");

		System.out.println(user_ids);
		System.out.println(item_ids);
		System.out.println(stars);
		System.out.println(contents);

		// ランダムな整数を4つ生成
		Random random = new Random();
		int random1 = random.nextInt(user_ids.size() - 1);
		int random2 = random.nextInt(item_ids.size() - 1);
		int random3 = random.nextInt(stars.size() - 1);
		int random4 = random.nextInt(contents.size() - 1);

		String sql = "INSERT INTO reviews (user_id, item_id, stars, content) VALUES (:user_id, :item_id, :star, :content)";
		// 配列からランダムにセット
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user_ids.get(random1))
				.addValue("item_id", item_ids.get(random2)).addValue("star", stars.get(random3))
				.addValue("content", contents.get(random4));
	}

}
