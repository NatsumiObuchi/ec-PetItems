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
	 * 商品一覧をanimalIdを絞って取得するメソッド。 (item一覧表示用)
	 */
	public List<Item> findByAnimalId(Integer animalId) {

		return repository.findByAnimalId(animalId);
	}

	/**
	 * 
	 * 商品をあいまい検索するメソッド。(絞り込み：すべて)
	 */
	public List<Item> findByNameAndAnimalId(String name, Integer animalId) {

		return repository.findByNameAndAnimalId(name, animalId);
	}

	/**
	 * 
	 * 商品の絞り込み検索用メソッド。(検索値の入力なし)
	 */
	public List<Item> findByCategoryId(Integer animalId, Integer categoryId) {

		return repository.findByCategoryId(animalId, categoryId);
	}

	/**
	 * 
	 * 商品のあいまい検索と、絞り込みを同時に選択された場合のメソッド
	 */
	public List<Item> findByCategoryIdAndAnimaiIdAndName(String name, Integer animalId, Integer categoryId) {

		return repository.findByCategoryIdAndAnimaiIdAndName(name, animalId, categoryId);
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
