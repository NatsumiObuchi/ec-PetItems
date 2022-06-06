package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.repository.ItemRepository;

@Service
@Transactional
public class ItemService {

	@Autowired
	private ItemRepository repository;

	/**
	 * 
	 * idでitemを検索するメソッド。 (item詳細表示用)
	 */
	public Item load(Integer id) {

		return repository.load(id);
	}

	/**
	 * 
	 * 商品一覧を取得するメソッド。 (item一覧表示用)
	 */
	public List<Item> findAll() {

		return repository.findAll();
	}

	/**
	 * 
	 * 商品をあいまい検索するメソッド。
	 */
	public List<Item> findByName(String name) {

		return repository.findByName(name);
	}

	/**
	 * 
	 * 商品の絞り込み検索用メソッド。(検索値の入力なし)
	 */
	public List<Item> findByAnimalId(Integer animalId) {

		return repository.findByAnimalId(animalId);
	}

	/**
	 * 
	 * 商品のあいまい検索と、絞り込みを同時に選択された場合のメソッド
	 */
	public List<Item> findByNameAndAnimalId(String name, Integer animalId) {

		return repository.findByNameAndAnimalId(name, animalId);
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
	public void insertRecordsIntoValues(int user_id, int item_id, int star) {
		repository.insertRecordsIntoValues(user_id, item_id, star);
	}
}
