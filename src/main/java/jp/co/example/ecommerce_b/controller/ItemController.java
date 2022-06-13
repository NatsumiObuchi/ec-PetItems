package jp.co.example.ecommerce_b.controller;

import java.util.ArrayList;
import java.util.List;

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
	 * 商品一覧を表示する
	 */
	@RequestMapping("/list")
	public String itemList(Model model) {
		List<Item> itemList = itemService.findAll();
		model.addAttribute("itemList", itemList);
		session.setAttribute("animalId", 0);
		model.addAttribute("categoryId", 0);
		List<String> nameList = itemService.findItemName();
		//オートコンプリート用。名前の全件検索をsessionに格納。
		session.setAttribute("nameList", nameList);
		return "item_list_pet";
	}

	/**
	 * 商品詳細を表示する
	 */
	@RequestMapping("/itemDetail")
	public String itemDetail(Integer id,Model model) {

		// 検索するitem_idでレビュー集め
		List<Review> reviews = itemService.findReview(id);
		model.addAttribute("reviews", reviews);
		System.out.println(reviews);

		Integer animalId = (Integer) session.getAttribute("animalId");

		if (animalId == 0) {
			model.addAttribute("link2", "すべて");
			model.addAttribute("access", "list");
		} else {
			switch (animalId) {
			case 1:
				model.addAttribute("link2", "犬用品");
				model.addAttribute("access", "dog");
				break;
			case 2:
				model.addAttribute("link2", "猫用品");
				model.addAttribute("access", "cat");
				break;
			}
		}

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
	public String searchItem(String code, Integer categoryId, Model model) {

		List<Item> itemList = new ArrayList<>();
		Integer animalId = (Integer) session.getAttribute("animalId");

		if (animalId == null || animalId == 0) {
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

//		検索結果の該当がない場合＋入力がない場合
		if (itemList.size() == 0 || code.equals("　") || code.equals(" ") || code.isEmpty()) {
			List<Item> itemList2 = new ArrayList<>();
			switch (categoryId) {
			case 0:
				if (animalId == null || animalId == 0) {
					itemList2 = itemService.findAll();
				} else {
					itemList2 = itemService.findByAnimalId(animalId);
				}

				if (code.isEmpty()) {
					model.addAttribute("noItemMessage", "商品一覧を表示します。");
				} else {
					model.addAttribute("noItemMessage", "該当の商品がございません。商品一覧を表示します。");
				}
				break;
			case 1:
				itemList2 = itemService.findByCategoryId(animalId, categoryId);
				if (code.isEmpty()) {
					model.addAttribute("noItemMessage", "絞り込み検索しました。フード一覧を表示します。");
				} else {
					model.addAttribute("noItemMessage", "該当の商品がございません。フード一覧を表示します。");
				}
				break;
			case 2:
				itemList2 = itemService.findByCategoryId(animalId, categoryId);
				if (code.isEmpty()) {
					model.addAttribute("noItemMessage", "絞り込み検索しました。おもちゃ一覧を表示します。");
				} else {
					model.addAttribute("noItemMessage", "該当の商品がございません。おもちゃ一覧を表示します。");
				}
				break;
			case 3:
				itemList2 = itemService.findByCategoryId(animalId, categoryId);
				if (code.isEmpty()) {
					model.addAttribute("noItemMessage", "絞り込み検索しました。その他の商品一覧を表示します。");
				} else {
					model.addAttribute("noItemMessage", "該当の商品がございません。その他の商品一覧を表示します。");
				}
				break;
			}
			model.addAttribute("categoryId", categoryId);
			model.addAttribute("itemList", itemList2);
			model.addAttribute("word", code);
			return "item_list_pet";
		} else {

//			入力フォームに何かしらの入力があった場合
			List<Item> itemList3 = itemService.findByCategoryIdAndAnimaiIdAndName(code, animalId, categoryId);
			if (categoryId == 0) {
				model.addAttribute("itemList", itemList);
				model.addAttribute("noItemMessage", "検索結果を表示します。");
			} else if (itemList3.size() == 0) {

//				何かしら入力があったが、絞り込んだ際には該当の結果がない場合
				List<Item> itemList2 = new ArrayList<>();
				switch (categoryId) {
				case 1:
					itemList2 = itemService.findByCategoryId(animalId, categoryId);
					model.addAttribute("noItemMessage", "該当の商品がございません。フード一覧を表示します。");
					break;
				case 2:
					itemList2 = itemService.findByCategoryId(animalId, categoryId);
					model.addAttribute("noItemMessage", "該当の商品がございません。おもちゃ一覧を表示します。");
					break;
				case 3:
					itemList2 = itemService.findByCategoryId(animalId, categoryId);
					model.addAttribute("noItemMessage", "該当の商品がございません。その他の商品一覧を表示します。");
					break;
				}
				model.addAttribute("itemList", itemList2);
			} else {
				model.addAttribute("itemList", itemList3);
				model.addAttribute("noItemMessage", "検索結果を表示します。");
			}
			model.addAttribute("categoryId", categoryId);
			model.addAttribute("word", code);
			return "item_list_pet";
		}
	}
	
	/**
	 * 犬用品のみのリストへ遷移する際のリンク
	 */
	@RequestMapping("/dog")
	public String dog(Model model) {

		session.setAttribute("animalId", 1);
		List<Item> itemList = itemService.findByAnimalId(1);

		model.addAttribute("categoryId", 0);
		model.addAttribute("link", "犬用品");
		model.addAttribute("itemList", itemList);

		return "item_list_pet";
	}
	
	/**
	 * 猫用品のみのリストへ遷移する際のリンク
	 */
	@RequestMapping("/cat")
	public String cat(Model model) {

		session.setAttribute("animalId", 2);
		List<Item> itemList = itemService.findByAnimalId(2);

		model.addAttribute("categoryId", 0);
		model.addAttribute("link", "猫用品");
		model.addAttribute("itemList", itemList);

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
				//支払い前のorderがDBになければ（ショッピングカートに商品が入っていなければ）新しくインサートする。
			} else {
				order = new Order();
				order.setStatus(0);
				order.setUserId(userId);
				orderService.insertOrder(order);
			}
			//userIdが０の場合（ログインしていない場合）新しくDBにインサートする。
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
	public String insertReview(ReviewForm form, Integer item_id, Model model) {
		User user = (User) session.getAttribute("user");
		Review review = new Review();
		BeanUtils.copyProperties(form, review);
		if (user != null) {
			review.setUser_id(user.getId());
		}
		review.setItem_id(item_id);
		itemService.insertReview(review);
		return itemDetail(item_id, model);
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
