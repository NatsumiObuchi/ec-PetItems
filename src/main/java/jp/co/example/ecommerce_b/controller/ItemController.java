package jp.co.example.ecommerce_b.controller;

import java.util.ArrayList;
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

import jp.co.example.ecommerce_b.domain.Coupon;
import jp.co.example.ecommerce_b.domain.Favorite;
import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.Point;
import jp.co.example.ecommerce_b.domain.Review;
import jp.co.example.ecommerce_b.domain.SortLabel;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.FavoriteListRegisterForm;
import jp.co.example.ecommerce_b.form.OrderItemForm;
import jp.co.example.ecommerce_b.form.ReviewForm;
import jp.co.example.ecommerce_b.form.ReviewInsertForm;
import jp.co.example.ecommerce_b.form.SearchForm;
import jp.co.example.ecommerce_b.service.CouponServise;
import jp.co.example.ecommerce_b.service.FavoriteService;
import jp.co.example.ecommerce_b.service.ItemService;
import jp.co.example.ecommerce_b.service.OrderItemService;
import jp.co.example.ecommerce_b.service.OrderService;
import jp.co.example.ecommerce_b.service.PointService;

/**
 * @author 81906
 *
 */
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

	@Autowired
	private CouponServise couponServise;

	@Autowired
	private PointService pointService;

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
	public String top(Model model) {
		// カテゴリーメニューバーのリストをマッピングするメソッドを呼び出す。
		categoryMapping();
		// オートコンプリート用。名前の全件検索をsessionに格納。
		List<String> nameList = itemService.findItemName();
		session.setAttribute("nameList", nameList);
		// 取得可能なクーポン一覧を表示
		List<Coupon> couponList = couponServise.findAllCoupon();
		model.addAttribute("couponList", couponList);
		model.addAttribute("couponLink", 0);

		// ユーザのポイント情報
		User user = (User) session.getAttribute("user");
		if (user != null) {
			Point point = pointService.load(user.getId());
			session.setAttribute("point", point);
		}

		return "top";
	}

	/**
	 * 商品一覧を表示する
	 * 
	 * @param form  code/genre/sortIdの3つの変数を扱う
	 * @param model
	 * @return メッセージとitemListをスコープに格納し、商品一覧画面に遷移する。
	 */
	@RequestMapping("/list")
	public String itemList(SearchForm form, Model model) {
		List<Item> itemList = itemService.findAll();

		// 並び替えメニューを表示するメソッド
		form.setLink(model);
		sort(0, itemList);
		label(0, model);

		model.addAttribute("itemList", itemList);
		model.addAttribute("itemListSize", itemList.size());
		model.addAttribute("genre", 0);
		model.addAttribute("noItemMessage", "商品一覧を表示します。");
		// カテゴリーメニューバーのリストをマッピングするメソッドを呼び出す。
		categoryMapping();
		// パンくずリスト用
		model.addAttribute("link", "すべて");
		model.addAttribute("panGenre", 0);
		
		// 取得可能なクーポン一覧を表示
		List<Coupon> couponList = couponServise.findAllCoupon();
		model.addAttribute("couponList", couponList);
		model.addAttribute("couponLink", 1);

		// ユーザのポイント情報
		User user = (User) session.getAttribute("user");
		if (user != null) {
			Point point = pointService.load(user.getId());
			session.setAttribute("point", point);
		}

		// オートコンプリート用。名前の全件検索をsessionに格納。
		List<String> nameList = itemService.findItemName();
		session.setAttribute("nameList", nameList);

		return "item_list_pet";
	}

	/**
	 * 商品詳細を表示する
	 * 
	 * @param id    該当の商品id
	 * @param genre カテゴリーid
	 * @param model
	 * @return
	 */
	@RequestMapping("/itemDetail")
	public String itemDetail(Integer id, Integer genre, Model model) {
		// 検索するitem_idでレビュー集め
		List<Review> reviews = itemService.findReview(id);
		model.addAttribute("reviews", reviews);

		// パンくずリストのリンク処理
		panList(genre, model);

		// お気に入りリストの情報(お気に入り済か、そうでないか)
		User user = (User) session.getAttribute("user");
		if (user == null) {// ユーザーでログインしていない
			model.addAttribute("nonFavorite");
		} else {// ユーザでログイン済
			Favorite favorite = favoriteService.findByUserIdItemId(user.getId(), id);
			System.out.println("favorite = " + favorite);
			if (favorite == null) {// お気に入り登録していない
				model.addAttribute("nonFavorite");
			}
		}

		Item item = itemService.load(id);
		model.addAttribute("item", item);

		return "item_detail";

	}

	/**
	 * 検索欄、カテゴリーメニューバー、パンくずリスト、並び替えをクリックしたときに呼ばれるメソッド
	 * 
	 * @param form  code/genre/sortIdの3つの変数を扱う
	 * @param model
	 * @return 絞り込みメッセージとitemListをスコープに格納し、商品一覧画面に遷移する。
	 */
	@RequestMapping("/search")
	public String searchItem(SearchForm form, Model model) {
		String code = form.getCode();
		Integer genre = form.getGenre();
		Integer sortId = form.getSortId();
		System.out.println(form);
		// 取得可能なクーポン一覧を表示
		List<Coupon> couponList = couponServise.findAllCoupon();
		model.addAttribute("couponList", couponList);

		// カテゴリーメニューバーのリストをマッピングするメソッドを呼び出す
		categoryMapping();

		// カテゴリーメニューバーからこのメソッドに飛んできたとき、エラーが起きないようにする
		if (code == null) {
			code = "";
		}

		// genreの値からanimalIdを設定
		Integer animalId = null;
		if (genre == 1 || genre == 2 || genre == 3 || genre == 4) {
			animalId = 1;
		} else if (genre == 5 || genre == 6 || genre == 7 || genre == 8) {
			animalId = 2;
		} else if (genre == 0) {
			animalId = 0;
		}
		System.out.println("animalId = " + animalId);

		// genreの値からcategoryIdを設定
		Integer categoryId = 0;
		if (genre == 2 || genre == 6) {
			categoryId = 1;
		} else if (genre == 3 || genre == 7) {
			categoryId = 2;
		} else if (genre == 4 || genre == 8) {
			categoryId = 3;
		}
		System.out.println("categoryId = " + categoryId);

		// パンくずリストのリンク処理
		panList(genre, model);

		List<Item> itemList = new ArrayList<>();
		itemList = itemService.findByNameAndAnimalId(code, animalId);

		System.out.println("itemList = " + itemList);
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
					noItemMessage = "犬用品一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。犬用品一覧を表示します。";
				}
				break;
			case 2:
				itemList2 = itemService.findByAnimalIdAndCategoryId(1, 1);
				if (code.isEmpty()) {
					noItemMessage = "犬｜フード一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。犬｜フード一覧を表示します。";
				}
				break;
			case 3:
				itemList2 = itemService.findByAnimalIdAndCategoryId(1, 2);
				if (code.isEmpty()) {
					noItemMessage = "犬｜おもちゃ一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。犬｜おもちゃ一覧を表示します。";
				}
				break;
			case 4:
				itemList2 = itemService.findByAnimalIdAndCategoryId(1, 3);
				if (code.isEmpty()) {
					noItemMessage = "犬｜その他の商品一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。犬｜その他の商品一覧を表示します。";
				}
				break;
			case 5:
				itemList2 = itemService.findByAnimalId(2);
				if (code.isEmpty()) {
					noItemMessage = "猫用品一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。猫用品一覧を表示します。";
				}
				break;
			case 6:
				itemList2 = itemService.findByAnimalIdAndCategoryId(2, 1);
				if (code.isEmpty()) {
					noItemMessage = "猫｜フード一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。猫｜フード一覧を表示します。";
				}
				break;
			case 7:
				itemList2 = itemService.findByAnimalIdAndCategoryId(2, 2);
				if (code.isEmpty()) {
					noItemMessage = "猫｜おもちゃ一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。猫｜おもちゃ一覧を表示します。";
				}
				break;
			case 8:
				itemList2 = itemService.findByAnimalIdAndCategoryId(2, 3);
				if (code.isEmpty()) {
					noItemMessage = "猫｜その他の商品一覧を表示します。";
				} else {
					noItemMessage = "該当の商品がございません。猫｜その他の商品一覧を表示します。";
				}
				break;
			}
			model.addAttribute("noItemMessage", noItemMessage);
			model.addAttribute("itemList", itemList2);

			// 入力フォームに何かしらの入力があった場合
		} else {

			List<Item> itemList3 = itemService.findByCategoryIdAndAnimaiIdAndName(code, animalId, categoryId);

			// DBのcategoryIdは1か2しかない。0(すべて)の場合はcodeとanimalIdで検索したitemListを持ってくる
			if (categoryId == 0) {
				model.addAttribute("itemList", itemList);
				model.addAttribute("noItemMessage", "「" + code + "」の検索結果を表示します。");

				// categoryIdが1か2で、何かしら入力があったが、絞り込んだ際には該当の結果がない場合
			} else if (itemList3.size() == 0) {

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
				model.addAttribute("itemList", itemList2);

			} else {
				// categoryIdが1か2で、検索欄入力があり、絞り込んだ際に該当の結果が見つかった場合
				model.addAttribute("itemList", itemList3);
				model.addAttribute("noItemMessage", "「 " + code + " 」の検索結果を表示します。");
			}
		}
		// 並べ替えをするときに使うリンクをリクエストスコープに入れるメソッド
		form.setLink(model);
		// sortIdが入っていれば並び替え/入っていなければ、デフォルトで新着順で並び替え
		itemList = (List<Item>) model.getAttribute("itemList");
		if (sortId != null) {
			sort(sortId, itemList);
			label(sortId, model);
		} else if (sortId == null) {
			sort(0, itemList);
			label(0, model);
		}
		// 表示件数・categoryId・検索文字列・genreをスコープに格納し商品一覧ページへ渡す
		model.addAttribute("itemListSize", itemList.size());
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("word", code);
		model.addAttribute("genre", genre);
		model.addAttribute("searchGenre", genre);
		return "item_list_pet";
	}

	/**
	 * 並び替えのListに値を詰めるメソッド
	 * 
	 * @param sortId 各並び順に付けられたid
	 * @param model
	 */
	public void label(Integer sortId, Model model) {
		List<SortLabel> sortLabelList = new ArrayList<>();
		sortLabelList.add(new SortLabel(0, "新着順", sortId != 0));
		sortLabelList.add(new SortLabel(1, "レビューが多い順", sortId != 1));
		sortLabelList.add(new SortLabel(2, "レビューが高い順", sortId != 2));
		sortLabelList.add(new SortLabel(3, "価格が高い順", sortId != 3));
		sortLabelList.add(new SortLabel(4, "価格が安い順", sortId != 4));

		model.addAttribute("sortLabelList", sortLabelList);
	}

	/**
	 * itemListを並びけるメソッド
	 * 
	 * @param sortId   各並び順に付けられたid
	 * @param itemList 並び替えを行うitemList
	 * @return 並び替えを終えた後のitemList
	 */
	public String sort(Integer sortId, List<Item> itemList) {
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
	 * パンくずリストを作成するためにスコープにlinkと名前を入れる
	 * 
	 * @param genre カテゴリーのid
	 * @param model
	 */
	public void panList(Integer genre, Model model) {
		if (genre == 1 || genre == 2 || genre == 3 || genre == 4) {
			model.addAttribute("link", "犬用品");
			model.addAttribute("panGenre", 1);
		} else if (genre == 5 || genre == 6 || genre == 7 || genre == 8) {
			model.addAttribute("link", "猫用品");
			model.addAttribute("panGenre", 5);
		} else if (genre == 0) {
			model.addAttribute("link", "すべて");
			model.addAttribute("panGenre", 0);
		}

	}

	/**
	 * カテゴリーのマッピングをする
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

	/*
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
