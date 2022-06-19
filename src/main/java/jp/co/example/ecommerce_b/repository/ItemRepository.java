package jp.co.example.ecommerce_b.repository;

import java.math.BigDecimal;
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
import jp.co.example.ecommerce_b.domain.Review;

/**
 * @author 81906
 *
 */
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
		item.setImagePath2(rs.getString("image_path2"));//
		item.setAnimal_id(rs.getInt("animal_id"));
		item.setCategory_id(rs.getInt("category_id"));
		item.setDeleted(rs.getBoolean("deleted"));
		if (rs.getBigDecimal("avg_star") == null) {
			item.setAvgStar("0.00");
		} else {
			BigDecimal bigDecimal = rs.getBigDecimal("avg_star");
			Float x = Float.valueOf(String.valueOf(bigDecimal));
			int y = Math.round(x * 100);
			Float z = (float) y / 100;
			String xx = String.valueOf(z);
			String[] yy = xx.split("");
			if (yy.length == 3) {//
				xx += "0";
			}
			item.setAvgStar(xx);
		}
		item.setCountReview(rs.getInt("count_review"));

		Double avgStar = Double.valueOf(item.getAvgStar());// 1.23,0.00
		avgStar *= 10;// 12.3,0.0
		long avgSTAR = Math.round(avgStar);// 13,0
		long avgSTAR10 = avgSTAR / 10;// 1
		long avgSTAR1 = avgSTAR % 10;// 3
		if (avgSTAR1 < 5) {
			avgSTAR1 = 0;
		} else if (avgSTAR1 <= 9) {
			avgSTAR1 = 5;
		}
		String a = avgSTAR10 + "." + avgSTAR1;
		item.setAvgstar2(a);

		return item;
	};

	private static final RowMapper<Review> REVIEW_ROW_MAPPER = (rs, i) -> {
		Review review = new Review();
		review.setId(rs.getInt("id"));
		review.setUser_id(rs.getInt("user_id"));
		review.setItem_id(rs.getInt("item_id"));
		review.setStars(rs.getInt("stars"));
		review.setContent(rs.getString("content"));
		if (rs.getInt("user_id") == 0) {
			review.setUser_name("未登録ユーザーさん");
		} else {
			review.setUser_name(rs.getString("user_name") + "さん");
		}
		return review;
	};

	private static final RowMapper<Review> MYREVIEW_ROW_MAPPER = (rs, i) -> {
		Review review = new Review();
		review.setId(rs.getInt("id"));
		review.setUser_id(rs.getInt("user_id"));
		review.setItem_id(rs.getInt("item_id"));
		review.setStars(rs.getInt("stars"));
		review.setContent(rs.getString("content"));
		Item item = new Item();
		item.setId(rs.getInt("item_id"));
		item.setName(rs.getString("item_name"));
		item.setDescription(rs.getString("item_description"));
		item.setPrice(rs.getInt("item_price"));
		item.setImagePath(rs.getString("item_image_path"));
		item.setImagePath2(rs.getString("item_image_path2"));
		item.setDeleted(rs.getBoolean("item_deleted"));
		review.setItem(item);
		return review;
	};

	/**
	 * 
	 * idでitemを検索するメソッド。 (item詳細表示用)
	 */
	public Item load(Integer id) {
		String sql = "select id, name, description, price, image_path, image_path2,animal_id,category_id, deleted, avg(star) avg_star ,count(star) count_review "
				+ "from(select i.id id, i.name as name, i.description description, i.price price,i.image_path image_path, i.image_path2 image_path2, i.animal_id animal_id, i.category_id category_id, i.deleted deleted,r.stars star "
				+ "from items as i left join reviews as r on i.id = r.item_id where i.deleted is false order by r.stars desc) as new_table "
				+ "WHERE id=:id "
				+ "group by id, name , description, price, image_path, image_path2,animal_id,category_id, deleted order by avg_star desc NULLS LAST, count_review desc, id desc";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);

		return item;
	}

	/**
	 * 
	 * 商品一覧を取得するメソッド。 (item一覧表示用)
	 */
	public List<Item> findAll() {
		String sql = "select id, name, description, price, image_path, image_path2,animal_id,category_id, deleted, avg(star) avg_star ,count(star) count_review "
				+ "from(select i.id id, i.name as name, i.description description, i.price price,i.image_path image_path, i.image_path2 image_path2, i.animal_id animal_id, i.category_id category_id, i.deleted deleted,r.stars star "
				+ "from items as i left join reviews as r on i.id = r.item_id where i.deleted is false order by r.stars desc) as new_table "
				+ "group by id, name , description, price, image_path, image_path2, animal_id, category_id, deleted order by avg_star desc NULLS LAST, count_review desc, id desc";
		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 
	 * 商品一覧をanimalIdを絞って取得するメソッド。 (item一覧表示用)
	 */

	public List<Item> findByAnimalId(Integer animalId) {
	String sql = "select id, name, description, price, image_path, image_path2,animal_id,category_id, deleted, avg(star) avg_star ,count(star) count_review "
			+ "from(select i.id id, i.name as name, i.description description, i.price price,i.image_path image_path, i.image_path2 image_path2, i.animal_id animal_id, i.category_id category_id, i.deleted deleted,r.stars star "
			+ "from items as i left join reviews as r on i.id = r.item_id where i.deleted is false order by r.stars desc) as new_table "
			+ "WHERE animal_id=:animalId "
			+ "group by id, name , description, price, image_path, image_path2, animal_id, category_id, deleted order by avg_star desc NULLS LAST, count_review desc, id desc";
		SqlParameterSource param = new MapSqlParameterSource().addValue("animalId", animalId);
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 
	 * 商品一覧をanimalId・categoryIdを絞って取得するメソッド。 (item一覧表示用)
	 */

	public List<Item> findByAnimalIdAndCategoryId(Integer animalId, Integer categoryId) {
	String sql = "select id, name, description, price, image_path, image_path2,animal_id,category_id, deleted, avg(star) avg_star ,count(star) count_review "
			+ "from(select i.id id, i.name as name, i.description description, i.price price,i.image_path image_path, i.image_path2 image_path2, i.animal_id animal_id, i.category_id category_id, i.deleted deleted,r.stars star "
			+ "from items as i left join reviews as r on i.id = r.item_id where i.deleted is false order by r.stars desc) as new_table "
			+ "WHERE animal_id=:animalId AND category_id=:categoryId "
			+ "group by id, name, description, price, image_path, image_path2, animal_id, category_id, deleted order by avg_star desc NULLS LAST, count_review desc, id desc";
		SqlParameterSource param = new MapSqlParameterSource().addValue("animalId", animalId).addValue("categoryId", categoryId);
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;
	}
	
	
	
	/*
	 * オートコンプリート機能用、名前全検索
	 */
	private static final RowMapper<String> ITEMNAME_ROW_MAPPER =(rs,i)->{
		String itemName = rs.getString("name");
		return itemName;
	};
	
	public List<String> findItemName(){
		String sql = "SELECT name FROM items ORDER BY id";
		List<String> nameList = template.query(sql, ITEMNAME_ROW_MAPPER);
		return nameList;
	}
	
	

	/**
	 * 
	 * 商品をあいまい検索するメソッド。(絞り込み：カテゴリがすべて(指定なし)の時)
	 */
	public List<Item> findByNameAndAnimalId(String name, Integer animalId) {
		String sql = "select id, name, description, price, image_path, image_path2,animal_id,category_id, deleted, avg(star) avg_star ,count(star) count_review "
				+ "from(select i.id id, i.name as name, i.description description, i.price price,i.image_path image_path, i.image_path2 image_path2, i.animal_id animal_id, i.category_id category_id, i.deleted deleted,r.stars star "
				+ "from items as i left join reviews as r on i.id = r.item_id where i.deleted is false order by r.stars desc) as new_table "
				+ "WHERE name LIKE :name ";
		String sql2 = "AND animal_id=:animalId ";//
		String sql3 = "group by id, name , description, price, image_path2, animal_id,category_id, image_path, deleted order by avg_star desc NULLS LAST, count_review desc, id desc";
		String sqlA = "";

		if (animalId == 0) {// 動物の指定がないとき
			sqlA = sql + sql3;
		} else {
			sqlA = sql + sql2 + sql3;
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
		String sql = "select id, name, description, price, image_path, image_path2,animal_id,category_id, deleted, avg(star) avg_star ,count(star) count_review "
				+ "from(select i.id id, i.name as name, i.description description, i.price price,i.image_path image_path, i.image_path2 image_path2, i.animal_id animal_id, i.category_id category_id, i.deleted deleted,r.stars star "
				+ "from items as i left join reviews as r on i.id = r.item_id where i.deleted is false order by r.stars desc) as new_table WHERE category_id=:categoryId ";
		String sql2 = "AND animal_id=:animalId ";//
		String sql3 = "group by id, name , description, price, image_path2, animal_id,category_id, image_path, deleted order by avg_star desc NULLS LAST, count_review desc, id desc";
		String sqlA = "";

		if (animalId == 0) {
			sqlA = sql + sql3;
		} else {
			sqlA = sql + sql2 + sql3;
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
		String sql = "select id, name, description, price, image_path, image_path2,animal_id,category_id, deleted, avg(star) avg_star ,count(star) count_review "
				+ "from(select i.id id, i.name as name, i.description description, i.price price,i.image_path image_path, i.image_path2 image_path2, i.animal_id animal_id, i.category_id category_id, i.deleted deleted,r.stars star "
				+ "from items as i left join reviews as r on i.id = r.item_id where i.deleted is false order by r.stars desc) as new_table WHERE category_id=:categoryId AND name LIKE :name ";
		String sql2 = "AND animal_id=:animalId ";//
		String sql3 = "group by id, name , description, price, image_path2, animal_id,category_id, image_path, deleted order by avg_star desc NULLS LAST, count_review desc, id desc";
		String sqlA = "";

		if (animalId == 0) {
			sqlA = sql + sql3;
		} else {
			sqlA = sql + sql2 + sql3;
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

	/**
	 * @param user_id
	 * @param item_id
	 * @param star    レビューテーブルにランダムな値を挿入する処理
	 */
	public void insertRecordsIntoValues(int user_id_min, int user_id_max, int item_id_min, int item_id_max,
			int star_min, int star_max) {
		// SQL本文
		String sql = "INSERT INTO reviews (user_id, item_id, stars, content) VALUES (:user_id, :item_id, :star, :content)";

		// リスト作成が目的
		List<Integer> user_ids = new ArrayList<>();
		List<Integer> item_ids = new ArrayList<>();
		List<Integer> stars = new ArrayList<>();
		List<String> contents = new ArrayList<>();

		for (int i = user_id_min; i <= user_id_max; i++) {
			user_ids.add(i);
		}

		for (int i = item_id_min; i <= item_id_max; i++) {
			item_ids.add(i);
		}

		for (int i = star_min; i <= star_max; i++) {
			stars.add(i);
		}

		contents.add("めっちゃおいしかった！");
		contents.add("手に入りにくかったのにあって嬉しかった！");
		contents.add("いいね！");

		// ランダムな整数を4つ生成
		Random random = new Random();
		int random1 = random.nextInt(user_ids.size() - 1);
		int random2 = random.nextInt(item_ids.size() - 1);
		int random3 = random.nextInt(stars.size() - 1);
		int random4 = random.nextInt(contents.size() - 1);

		// 配列からランダムにセット
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", user_ids.get(random1))
				.addValue("item_id", item_ids.get(random2)).addValue("star", stars.get(random3))
				.addValue("content", contents.get(random4));


		// 実行
		template.update(sql, param);

	}

	public void insertReview(Review review) {
		System.out.println(review);
		String sql = "insert into reviews (user_id, item_id, stars, content) VALUES (:userId, :itemId, :stars, :content)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", review.getUser_id())
				.addValue("itemId", review.getItem_id()).addValue("stars", review.getStars())
				.addValue("content", review.getContent());
		template.update(sql, param);
	}

	public List<Review> findReview(Integer itemId) {
//		String sql = "select reviews.id as id, user_id, item_id, stars, content from reviews where item_id = :itemId order by id desc";
		String sql = "select reviews.id as id, user_id, item_id, stars, content , users.name as user_name from reviews left join users on reviews.user_id = users.id where item_id = :itemId order by id desc";
		SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId);
		List<Review> reviews = new ArrayList<>();
		reviews = template.query(sql, param, REVIEW_ROW_MAPPER);
		System.out.println(reviews);
		if (reviews.size() == 0) {
			return null;
		} else {
			return reviews;
		}
	}

	/**
	 * マイレビュー一覧を取得する
	 * 
	 * @param id
	 * @return
	 */
	public List<Review> myReview(Integer id) {
		String sql = "select r.id, r.user_id, r.item_id, r.stars, r.content "
				+ ", i.id item_id, i.name item_name, i.description item_description, i.price item_price,"
				+ " i.image_path item_image_path, i.image_path2 item_image_path2, i.deleted item_deleted"
				+ " from reviews as r" + " inner join" + " items as i" + " on r.item_id = i.id"
				+ " where r.user_id = :user_id order by r.id desc;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("user_id", id);
		List<Review> reviewList = template.query(sql, param, MYREVIEW_ROW_MAPPER);
		if (reviewList.size() == 0) {
			return null;
		}
		return reviewList;
	}
}
