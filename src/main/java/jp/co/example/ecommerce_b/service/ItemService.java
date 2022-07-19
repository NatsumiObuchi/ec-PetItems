package jp.co.example.ecommerce_b.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Review;
import jp.co.example.ecommerce_b.domain.Search;
import jp.co.example.ecommerce_b.repository.ItemRepository;

@Service
@Transactional
public class ItemService {

	@Autowired
	private ItemRepository repository;

	/**
	 * 検索欄、カテゴリーメニューバー、パンくずリスト、並び替えをクリックしたときに検索結果を返す
	 * @param search
	 * @return
	 */
	public List<Item> search(Search search) {
		Integer genre = search.getGenre();
		// genreの値からanimalIdを設定
		String animalMessage = null;
		Integer animalId = null;
		if (genre == 1 || genre == 2 || genre == 3 || genre == 4) {
			animalId = 1;
			animalMessage = "犬用品";
		} else if (genre == 5 || genre == 6 || genre == 7 || genre == 8) {
			animalId = 2;
			animalMessage = "猫用品";
		} else if (genre == 0) {
			animalMessage = "犬・猫用品";
		}
		search.setAnimalId(animalId);

		// genreの値からcategoryIdを設定
		String categoryMessage = null;
		Integer categoryId = null;
		if (genre == 2 || genre == 6) {
			categoryId = 1;
			categoryMessage = "フード";
		} else if (genre == 3 || genre == 7) {
			categoryId = 2;
			categoryMessage = "おもちゃ";
		} else if (genre == 4 || genre == 8) {
			categoryId = 3;
			categoryMessage = "その他";
		} else if (genre == 0 || genre==1 || genre==5) {
			categoryMessage = "全商品";
		}
		search.setCategoryId(categoryId);

		if (search.getCode() == null) {
			search.setCode("");
		}

		List<Item> itemList = new ArrayList<>();
		itemList = repository.findbyElement(animalId, categoryId, search.getCode(), search.getSortId());
		
		// 文字列を入れた検索結果で該当商品がない時、指定のジャンルの商品一覧を表示
		if (itemList.size() == 0) {
			search.setCode("");
			itemList = repository.findbyElement(animalId, categoryId, search.getCode(), search.getSortId());
			search.setSearchMessage("該当の商品がございません。" + animalMessage + "／" + categoryMessage + "を表示します");
		} else {
			// カテゴリーバーから飛んできたとき OR 検索文字列が空だった時
			if (search.getCode()==null || search.getCode().isEmpty() || search.getCode()=="") {
				search.setSearchMessage(animalMessage + "／" + categoryMessage + "を表示します");
			// 文字列を入れた検索結果で該当商品がある時
			} else {
				search.setSearchMessage("「" + search.getCode() + "」の検索結果を表示します");
			}
		}
		return itemList;
	}

	/**
	 * idでitemを検索するメソッド。 (item詳細表示用)
	 */
	public Item load(Integer id) {
		return repository.load(id);
	}

	/** 
	 * 商品一覧を取得するメソッド。 (item一覧表示用)
	 */
	public List<Item> findAll() {
		return repository.findAll();
	}

	/*
	 * オートコンプリート機能用
	 */
	public List<String> findItemName() {
		return repository.findItemName();
	}

	/**
	 * レビューを投稿したときにDBにインサートする
	 * @param review
	 */
	public void insertReview(Review review) {
		repository.insertReview(review);
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
	public void insertRecordsIntoValues(int times, int user_id_min, int user_id_max, int item_id_min, int item_id_max,
			int star_min, int star_max) {
		for (int i = 1; i <= times; i++) {
			repository.insertRecordsIntoValues(user_id_min, user_id_max, item_id_min, item_id_max, star_min, star_max);
		}
	}

	public List<Review> findReview(Integer itemId) {
		return repository.findReview(itemId);
	}

	/**
	 * ユーザーIDからマイレビューを取得する
	 * 
	 * @param id
	 * @return
	 */
	public List<Review> findReviewByUserId(Integer id) {
		return repository.myReview(id);
	}
}
