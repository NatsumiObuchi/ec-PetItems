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
		item.setImagePath2(rs.getString("image_path2"));
		item.setAnimalId(rs.getInt("animal_id"));
		item.setCategoryId(rs.getInt("category_id"));
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
		item.setAnimalId(rs.getInt("animal_id"));
		item.setCategoryId(rs.getInt("category_id"));
		item.setDeleted(rs.getBoolean("item_deleted"));
		review.setItem(item);
		return review;
	};

	private static final String QUERY = """
			SELECT id, name, description, price, image_path, image_path2, animal_id, category_id, deleted,
			avg(star) avg_star, count(star) count_review
			from(SELECT i.id id, i.name as name, i.description description, i.price price,i.image_path image_path, i.image_path2 image_path2,
			i.animal_id animal_id, i.category_id category_id, i.deleted deleted,r.stars star
			from items as i left join reviews as r on i.id = r.item_id where i.deleted is false order by r.stars desc) as new_table
			 """;

	public void groupByBuilder(StringBuilder builder) {
		builder.append(
				" group by id, name , description, price, image_path, image_path2,animal_id,category_id, deleted order by avg_star desc NULLS LAST, count_review desc, id desc;");
	}

	int firstBuilderLength = QUERY.length();

	/** 
	 * 商品一覧を取得するメソッド。 (item一覧表示用)
	 */
	public List<Item> findAll() {
		StringBuilder builder = new StringBuilder(QUERY);
		groupByBuilder(builder);
		List<Item> itemList = template.query(builder.toString(), ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * idでitemを検索するメソッド。 (item詳細表示用)
	 */
	public Item load(Integer id) {
		StringBuilder builder = new StringBuilder(QUERY);
		builder.append(" WHERE id=:id");
		groupByBuilder(builder);
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Item item = template.queryForObject(builder.toString(), param, ITEM_ROW_MAPPER);
		return item;
	}

	/**
	 * 検索をかけたときに条件によってSQL文を分岐させ、指定のitemListを取得するメソッド
	 * 
	 * @param animalId   アニマルID
	 * @param categoryId カテゴリーID
	 * @param name       検索文字列
	 * @param sortId     並び替えID
	 * @return itemList
	 */
	public List<Item> findbyElement(Integer animalId, Integer categoryId, String name, Integer sortId) {
		StringBuilder builder = new StringBuilder(QUERY);
		MapSqlParameterSource param = new MapSqlParameterSource();
		if (animalId != null) {
			authenticationBuilder(builder);
			builder.append(" animal_id=:animalId");
			param.addValue("animalId", animalId);
		}
		if (categoryId != null) {
			authenticationBuilder(builder);
			builder.append(" category_id=:categoryId");
			param.addValue("categoryId", categoryId);
		}
		if (!(name.isEmpty()) || name != "") {
			authenticationBuilder(builder);
			builder.append(" name LIKE :name");
			param.addValue("name", "%" + name + "%");
		}
		// sortIdが入っている時、並び替え（idによって分岐）
		if (sortId != null) {
			builder.append(
					" group by id, name , description, price, image_path, image_path2,animal_id,category_id, deleted");
			switch (sortId) {
			case 0: // 新着順に並び替える
				builder.append(" order by id desc, avg_star desc NULLS LAST, count_review desc");
				break;
			case 1: // レビューが多い順に並び替える
				builder.append(" order by count_review desc, id desc, avg_star desc NULLS LAST");
				break;
			case 2: // レビューが高い順に並び替える
				builder.append(" order by avg_star desc NULLS LAST, count_review desc, id desc");
				break;
			case 3: // 価格が高い順に並び替える
				builder.append(" order by price desc, avg_star desc NULLS LAST, count_review desc, id desc");
				break;
			case 4: // 価格が安い順に並び替える
				builder.append(" order by price, id desc, avg_star desc NULLS LAST, count_review desc");
				break;
			}
		} else {
			groupByBuilder(builder);
		}
		List<Item> itemList = template.query(builder.toString(), param, ITEM_ROW_MAPPER);
		System.out.println(itemList);
		return itemList;
	}

	/**
	 * 引数に受け取ったbuilderの文字列の長さが、フィールド宣言時のbuilderの長さと等しいかどうかチェック。条件分岐でsql文追加。
	 * 
	 * @param builder SQLのSELECT文字列
	 */
	public void authenticationBuilder(StringBuilder builder) {
		if (builder.length() == firstBuilderLength) {
			builder.append(" WHERE");
		} else {
			builder.append(" AND");
		}
	}

	
	/**
	 * 商品情報を更新するメソッド。
	 */
	public Item save(Item item) {

//		6/1 商品情報更新用メソッドについては一旦未実装
		return null;
	}

	/**
	 * 商品情報を削除するメソッド。
	 */
	public void deleteById(Integer id) {
//		6/1　商品情報を削除するメソッドについては一旦未実装
	}

	/**
	 * オートコンプリート機能用、名前全検索
	 */
	private static final RowMapper<String> ITEMNAME_ROW_MAPPER = (rs, i) -> {
		String itemName = rs.getString("name");
		return itemName;
	};

	public List<String> findItemName() {
		String sql = "SELECT name FROM items ORDER BY id";
		List<String> nameList = template.query(sql, ITEMNAME_ROW_MAPPER);
		return nameList;
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
				+ " i.image_path item_image_path, i.image_path2 item_image_path2, i.animal_id, i.category_id, i.deleted item_deleted"
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
