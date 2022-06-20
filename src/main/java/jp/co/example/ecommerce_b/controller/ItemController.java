package jp.co.example.ecommerce_b.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.Review;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.FavoriteListRegisterForm;
import jp.co.example.ecommerce_b.form.OrderItemForm;
import jp.co.example.ecommerce_b.form.ReviewForm;
import jp.co.example.ecommerce_b.form.ReviewInsertForm;
import jp.co.example.ecommerce_b.form.SearchForm;
import jp.co.example.ecommerce_b.service.FavoriteService;
import jp.co.example.ecommerce_b.service.ItemService;
import jp.co.example.ecommerce_b.service.OrderItemService;
import jp.co.example.ecommerce_b.service.OrderService;

@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private HttpSession session;

	@Autowired
	private ItemService itemService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private FavoriteService favoriteService;

	@ModelAttribute
	private SearchForm createSearchForm() {
		return new SearchForm();
	}

	@ModelAttribute
	private ReviewInsertForm createReviewInsertForm() {
		return new ReviewInsertForm();
	}

	@ModelAttribute
	private OrderItemForm createOrderItemForm() {
		return new OrderItemForm();
	}

	@ModelAttribute
	private FavoriteListRegisterForm favoriteListRegisterForm() {
		return new FavoriteListRegisterForm();
	}

	@ModelAttribute
	private ReviewForm createReviewForm() {
		return new ReviewForm();
	}

	/**
	 * @return トップに遷移するだけの処理
	 */
	@RequestMapping("/top")
	public String top() {
		// カテゴリーメニューバーのリストをマッピングするメソッドを呼び出す。
		categoryMapping();
		// オートコンプリート用。名前の全件検索をsessionに格納。
		List<String> nameList = itemService.findItemName();
		session.setAttribute("nameList", nameList);
		return "top";
	}

	/**
	 * 商品一覧を表示する
	 */
	@RequestMapping("/list")
	public String itemList(Model model) {
		List<Item> itemList = itemService.findAll();
		model.addAttribute("itemList", itemList);
		session.setAttribute("animalId", 0);
		model.addAttribute("categoryId", 0);
		// カテゴリーメニューバーのリストをマッピングするメソッドを呼び出す。
		categoryMapping();
		// パンくずリスト用
		model.addAttribute("link", "すべて");
		model.addAttribute("access", "list");
		// オートコンプリート用。名前の全件検索をsessionに格納。
		List<String> nameList = itemService.findItemName();
		session.setAttribute("nameList", nameList);
		return "item_list_pet";
	}

	/*
	 * カテゴリーのマッピングメソッド
	 */
	public void categoryMapping() {
		Map<Integer, String> genreMap = new HashMap<>();
		genreMap.put(0, "すべてのカテゴリー");
		genreMap.put(1, "犬｜すべて");
		genreMap.put(2, "犬｜フード");
		genreMap.put(3, "犬｜おもちゃ");
		genreMap.put(4, "犬｜その他");
		genreMap.put(5, "猫｜すべて");
		genreMap.put(6, "猫｜フード");
		genreMap.put(7, "猫｜おもちゃ");
		genreMap.put(8, "猫｜その他");
		session.setAttribute("genreMap", genreMap);
	}

	/**
	 * 商品詳細を表示する
	 */
	@RequestMapping("/itemDetail")
	public String itemDetail(Integer id, Integer animalId, Model model) {
		System.out.println("id:" + id + "  animalId" + animalId);
		// 検索するitem_idでレビュー集め
		List<Review> reviews = itemService.findReview(id);
		model.addAttribute("reviews", reviews);
		System.out.println(reviews);

		if (animalId == 0) {
			model.addAttribute("link", "すべて");
			model.addAttribute("access", "list");
		} else {
			switch (animalId) {
			case 1:
				model.addAttribute("link", "犬用品");
				model.addAttribute("access", "/");
				break;
			case 2:
				model.addAttribute("link", "猫用品");
				model.addAttribute("access", "cat");
				break;
			}
		}
		model.addAttribute("animalId", animalId);
		Item item = itemService.load(id);
		model.addAttribute("item", item);

		return "item_detail";

	}

	/**
	 * 商品を検索する ※該当の商品がない場合は絞り込み選択値によって、結果と表示メッセージを切り替え。
	 * 
	 * @param code
	 * @param model
	 * @return
	 */
	@RequestMapping("/search")
	public String searchItem(SearchForm form, Model model) {
		String code = form.getCode();
		Integer genre = (Integer)(form.getGenre());
		Integer sortId = form.getSortId();
		Integer animalId = form.getAnimalId();
		System.out.println(form);
		// カテゴリーメニューバーからこのメソッドに飛んできたとき、エラーが起きないようにする。
		if (code == null) {
			code = "";
		}
		if(genre==null){
			if(animalId==1) {
				genre=1;
			}else if(animalId==2) {
				genre=5;
			}
		}
		System.out.println("genre = "+ genre);
		
		// genreの値からcategoryIdとanimalIdを定義する。
		Integer categoryId = 0;
		if (genre == 2 || genre == 6) {
			categoryId = 1;
		} else if (genre == 3 || genre == 7) {
			categoryId = 2;
		} else if (genre == 4 || genre == 8) {
			categoryId = 3;
		}
		System.out.println("categoryId = "+categoryId);
		
		if(animalId==null) {
		if (genre == 1 || genre == 2 || genre == 3 || genre == 4) {
			animalId = 1;
		} else if (genre == 5 || genre == 6 || genre == 7 || genre == 8) {
			animalId = 2;
		}
		}
		System.out.println("animalId = "+animalId);
		List<Item> itemList = new ArrayList<>();

		// Integer animalId = (Integer) session.getAttribute("animalId");
		// if(animalId == null) {
		// animalId = 0;
		// }
		if (animalId == 0) {
			itemList = itemService.findByNameAndAnimalId(code, 0);
			model.addAttribute("link", "すべて");
		} else {
			itemList = itemService.findByNameAndAnimalId(code, animalId);
			switch (animalId) {
			case 1:
				model.addAttribute("link", "犬用品");
				break;
			case 2:
				model.addAttribute("link", "猫用品");
				break;
			}
		}

		System.out.println("itemList = "+itemList);
		// 検索結果の該当がない場合＋入力がない場合
		if (itemList.size() == 0 || code.equals("　") || code.equals(" ") || code.isEmpty()) {
			List<Item> itemList2 = new ArrayList<>();
			String noItemMessage = null;
			switch (genre) {
			case 0:
				itemList2 = itemService.findAll();
				if (code.isEmpty()) {
					noItemMessage = "商品一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。商品一覧を表示します。";
				}
				break;
			case 1:
				itemList2 = itemService.findByAnimalId(1);
				if (code.isEmpty()) {
					noItemMessage = "絞り込み検索しました。犬用品一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。犬用品一覧を表示します。";
				}
				break;
			case 2:
				itemList2 = itemService.findByAnimalIdAndCategoryId(1, 1);
				if (code.isEmpty()) {
					noItemMessage = "絞り込み検索しました。犬｜フード一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。犬｜フード一覧を表示します。";
				}
				break;
			case 3:
				itemList2 = itemService.findByAnimalIdAndCategoryId(1, 2);
				if (code.isEmpty()) {
					noItemMessage = "絞り込み検索しました。犬｜おもちゃ一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。犬｜おもちゃ一覧を表示します。";
				}
				break;
			case 4:
				itemList2 = itemService.findByAnimalIdAndCategoryId(1, 3);
				if (code.isEmpty()) {
					noItemMessage = "絞り込み検索しました。犬｜その他の商品一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。犬｜その他の商品一覧を表示します。";
				}
				break;
			case 5:
				itemList2 = itemService.findByAnimalId(2);
				if (code.isEmpty()) {
					noItemMessage = "絞り込み検索しました。猫用品一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。猫用品一覧を表示します。";
				}
				break;
			case 6:
				itemList2 = itemService.findByAnimalIdAndCategoryId(2, 1);
				if (code.isEmpty()) {
					noItemMessage = "絞り込み検索しました。猫｜フード一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。猫｜フード一覧を表示します。";
				}
				break;
			case 7:
				itemList2 = itemService.findByAnimalIdAndCategoryId(2, 2);
				if (code.isEmpty()) {
					noItemMessage = "絞り込み検索しました。猫｜おもちゃ一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。猫｜おもちゃ一覧を表示します。";
				}
				break;
			case 8:
				itemList2 = itemService.findByAnimalIdAndCategoryId(2, 3);
				if (code.isEmpty()) {
					noItemMessage = "絞り込み検索しました。猫｜その他の商品一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。猫｜その他の商品一覧を表示します。";
				}
				break;
			}
			model.addAttribute("noItemMessage", noItemMessage);
			model.addAttribute("categoryId", categoryId);
//			model.addAttribute("word", code);
			if (sortId != null) {
				sort(sortId, itemList2);
			}
			model.addAttribute("itemList", itemList2);
			return "item_list_pet";
		} else {

//			入力フォームに何かしらの入力があった場合
			List<Item> itemList3 = itemService.findByCategoryIdAndAnimaiIdAndName(code, animalId, categoryId);
			if (categoryId == 0) {
				if (sortId != null) {
					sort(sortId, itemList);
				}
				model.addAttribute("itemList", itemList);
				model.addAttribute("noItemMessage", "「" + code + "」の検索結果を表示します。");
			} else if (itemList3.size() == 0) {

//				何かしら入力があったが、絞り込んだ際には該当の結果がない場合
				List<Item> itemList2 = new ArrayList<>();
				String mes = null;
				switch (genre) {
				case 0:
					itemList2 = itemService.findAll();
					mes = "商品";
					break;
				case 1:
					itemList2 = itemService.findByAnimalId(1);
					mes = "犬用品";
					break;
				case 2:
					itemList2 = itemService.findByAnimalIdAndCategoryId(1, 1);
					mes = "犬｜フード";
					break;
				case 3:
					itemList2 = itemService.findByAnimalIdAndCategoryId(1, 2);
					mes = "犬｜おもちゃ";
					break;
				case 4:
					itemList2 = itemService.findByAnimalIdAndCategoryId(1, 3);
					mes = "犬｜その他";
					break;
				case 5:
					itemList2 = itemService.findByAnimalId(2);
					mes = "猫用品";
					break;
				case 6:
					itemList2 = itemService.findByAnimalIdAndCategoryId(2, 1);
					mes = "猫｜フード";
					break;
				case 7:
					itemList2 = itemService.findByAnimalIdAndCategoryId(2, 2);
					mes = "猫｜おもちゃ";
					break;
				case 8:
					itemList2 = itemService.findByAnimalIdAndCategoryId(2, 3);
					mes = "猫｜その他";
					break;
				}
				model.addAttribute("noItemMessage", "該当の商品がございません。" + mes + "一覧を表示します。");
				if (sortId != null) {
					sort(sortId, itemList2);
				}
				model.addAttribute("itemList", itemList2);

			} else {
				if (sortId != null) {
					sort(sortId, itemList3);
				}
				model.addAttribute("itemList", itemList3);
				model.addAttribute("noItemMessage", "「 " + code + " 」の検索結果を表示します。");
			}
			model.addAttribute("categoryId", categoryId);
			model.addAttribute("word", code);
			return "item_list_pet";
		}
	}
	/*
	 * @RequestMapping("/search") public String searchItem(String code, Integer
	 * categoryId, Model model) {
	 * 
	 * List<Item> itemList = new ArrayList<>(); Integer animalId = (Integer)
	 * session.getAttribute("animalId");
	 * 
	 * if (animalId == null || animalId == 0) { itemList =
	 * itemService.findByNameAndAnimalId(code, 0); model.addAttribute("link",
	 * "すべて"); } else { itemList = itemService.findByNameAndAnimalId(code,
	 * animalId); switch (animalId) { case 1: model.addAttribute("link", "犬用品");
	 * break; case 2: model.addAttribute("link", "猫用品"); break; } }
	 * 
	 * // 検索結果の該当がない場合＋入力がない場合 if (itemList.size() == 0 || code.equals("　") ||
	 * code.equals(" ") || code.isEmpty()) { List<Item> itemList2 = new
	 * ArrayList<>(); switch (categoryId) { case 0: if (animalId == null || animalId
	 * == 0) { itemList2 = itemService.findAll(); } else { itemList2 =
	 * itemService.findByAnimalId(animalId); }
	 * 
	 * if (code.isEmpty()) { model.addAttribute("noItemMessage", "商品一覧を表示します。"); }
	 * else { model.addAttribute("noItemMessage", "該当の商品がございません。商品一覧を表示します。"); }
	 * break; case 1: itemList2 = itemService.findByCategoryId(animalId,
	 * categoryId); if (code.isEmpty()) { model.addAttribute("noItemMessage",
	 * "絞り込み検索しました。フード一覧を表示します。"); } else { model.addAttribute("noItemMessage",
	 * "該当の商品がございません。フード一覧を表示します。"); } break; case 2: itemList2 =
	 * itemService.findByCategoryId(animalId, categoryId); if (code.isEmpty()) {
	 * model.addAttribute("noItemMessage", "絞り込み検索しました。おもちゃ一覧を表示します。"); } else {
	 * model.addAttribute("noItemMessage", "該当の商品がございません。おもちゃ一覧を表示します。"); } break;
	 * case 3: itemList2 = itemService.findByCategoryId(animalId, categoryId); if
	 * (code.isEmpty()) { model.addAttribute("noItemMessage",
	 * "絞り込み検索しました。その他の商品一覧を表示します。"); } else { model.addAttribute("noItemMessage",
	 * "該当の商品がございません。その他の商品一覧を表示します。"); } break; } model.addAttribute("categoryId",
	 * categoryId); model.addAttribute("itemList", itemList2);
	 * model.addAttribute("word", code); return "item_list_pet"; } else {
	 * 
	 * // 入力フォームに何かしらの入力があった場合 List<Item> itemList3 =
	 * itemService.findByCategoryIdAndAnimaiIdAndName(code, animalId, categoryId);
	 * if (categoryId == 0) { model.addAttribute("itemList", itemList);
	 * model.addAttribute("noItemMessage", "検索結果を表示します。"); } else if
	 * (itemList3.size() == 0) {
	 * 
	 * // 何かしら入力があったが、絞り込んだ際には該当の結果がない場合 List<Item> itemList2 = new ArrayList<>();
	 * switch (categoryId) { case 1: itemList2 =
	 * itemService.findByCategoryId(animalId, categoryId);
	 * model.addAttribute("noItemMessage", "該当の商品がございません。フード一覧を表示します。"); break; case
	 * 2: itemList2 = itemService.findByCategoryId(animalId, categoryId);
	 * model.addAttribute("noItemMessage", "該当の商品がございません。おもちゃ一覧を表示します。"); break;
	 * case 3: itemList2 = itemService.findByCategoryId(animalId, categoryId);
	 * model.addAttribute("noItemMessage", "該当の商品がございません。その他の商品一覧を表示します。"); break; }
	 * model.addAttribute("itemList", itemList2); } else {
	 * model.addAttribute("itemList", itemList3);
	 * model.addAttribute("noItemMessage", "検索結果を表示します。"); }
	 * model.addAttribute("categoryId", categoryId); model.addAttribute("word",
	 * code); return "item_list_pet"; } }
	 */

	@RequestMapping("/sort")
	public String sort(Integer sortId, List<Item> itemList) {
		System.out.println(itemList);

		switch (sortId) {
		case 0:
			// 今回っているItemListをsortして、新着順に並び替える処理
			itemList.sort(Comparator.comparing(Item::getId));
			break;
		case 1:
			// 今回っているItemListをsortして、レビューが多い順に並び替える処理
			itemList.sort(Comparator.comparing(Item::getCountReview).reversed());
			break;
		case 2:
			// 今回っているItemListをsortして、レビューが高い順に並び替える処理
			itemList.sort(Comparator.comparing(Item::getAvgStar).reversed());
			break;
		case 3:

			// 今回っているItemListをsortして、価格が高い順に並び替える処理
			itemList.sort(Comparator.comparing(Item::getPrice).reversed());
			break;
		case 4:
			// 今回っているItemListをsortして、価格が低い順に並び替える処理
			itemList.sort(Comparator.comparing(Item::getPrice));
			break;
		}

		return "item_list_pet";
	}

	/**
	 * @param userId orderをセッションスコープに格納する処理。
	 */
	public void checkOrderBeforePayment(Integer userId) {
		// DBに存在すれば支払い前のorderが入り、DBに存在しなければ新しいオーダーが入る
		Order order = orderService.findOrderBeforePayment(userId);

		if (order == null) {
			if (session.getAttribute("order") != null) {
				order = (Order) session.getAttribute("order");
				// 支払い前のorderがDBになければ（ショッピングカートに商品が入っていなければ）新しくインサートする。
			} else {
				order = new Order();
				order.setStatus(0);
				order.setUserId(userId);
				orderService.insertOrder(order);
			}
			// userIdが０の場合（ログインしていない場合）新しくDBにインサートする。
		} else if (order.getUserId() == 0) {
			if (session.getAttribute("order") != null) {
				order = (Order) session.getAttribute("order");
			} else {
				order = new Order();
				order.setStatus(0);
				order.setUserId(userId);
				orderService.insertOrder(order);
			}
		}
		session.setAttribute("order", order);
	}

	@RequestMapping("/insertReview")
	public String insertReview(ReviewForm form, Integer item_id, Integer animalId, Model model) {
		User user = (User) session.getAttribute("user");
		Review review = new Review();
		BeanUtils.copyProperties(form, review);
		if (user != null) {
			review.setUser_id(user.getId());
		}
		review.setItem_id(item_id);
		itemService.insertReview(review);
		return itemDetail(item_id, animalId, model);
	}

	/**
	 * @return 「隠されたページ」に飛ぶ処理
	 */
	@RequestMapping("/washio1")
	public String washio() {
		return "hidden";
	}

	/**
	 * @param form
	 * @param model
	 * @return レビューテーブルにランダムな値を挿入する処理
	 */
	@RequestMapping("/washio2")
	public String insertRecordsIntoValues(ReviewInsertForm form, Model model) {
		itemService.insertRecordsIntoValues(Integer.parseInt(form.getTimes()), Integer.parseInt(form.getUser_id_min()),
				Integer.parseInt(form.getUser_id_max()), Integer.parseInt(form.getItem_id_min()),
				Integer.parseInt(form.getItem_id_max()), Integer.parseInt(form.getStar_min()),
				Integer.parseInt(form.getStar_max()));
		return "hidden";
	}
}
