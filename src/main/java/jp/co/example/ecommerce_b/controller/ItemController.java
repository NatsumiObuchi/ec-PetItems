package jp.co.example.ecommerce_b.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Favorite;
import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.FavoriteListRegisterForm;
import jp.co.example.ecommerce_b.form.OrderItemForm;
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
	private OrderItemForm createOrderItemForm() {
		return new OrderItemForm();
	}

	@ModelAttribute
	private FavoriteListRegisterForm favoriteListRegisterForm() {
		return new FavoriteListRegisterForm();
	}

	/**
	 * 商品一覧を表示する
	 */
	@RequestMapping("/list")
	public String itemList(Model model) {
		List<Item> itemList = itemService.findAll();
		model.addAttribute("itemList", itemList);
		session.setAttribute("animalId", 0);
		return "item_list_pet";
	}

	/**
	 * カートに入れた商品を全表示する カートに商品がない場合、「カートに商品がありません。」と表示する
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/cartList")
	public String cartListShow(Model model) {

		// ユーザー周り
		User user = (User) session.getAttribute("user");
		if (user == null) {
			user = new User();
			user.setId(0);
		}
		System.out.println("userId:" + user.getId());

		// カートリスト周り
		checkOrderBeforePayment(user.getId());// ユーザーの未払いオーダーがあった場合、その「オーダー」をセッションに格納。なくてもuserId=0の「オーダー」を格納。
		List<OrderItem> cartList = (List<OrderItem>) session.getAttribute("cartList");
		if (cartList == null || cartList.size() == 0) {// sessionスコープにカートがない時
			if (user.getId() == 0) {// ユーザーIDが0の場合、過去の他の未登録ユーザーのオーダーがDBにある可能性がある。
				String emptyMessage = "現在、カートに商品はありません。";
				model.addAttribute("emptyMessage", emptyMessage);
				session.setAttribute("cartList", null);
			} else {
				Order order = (Order) session.getAttribute("order");
				System.out.println("order:" + order);
				Integer orderId = order.getId();
				System.out.println("orderId:" + orderId);
				List<OrderItem> orderItemsFromDB = orderItemService.findByOrderId(orderId);
				System.out.println("orderItemsFromDB:" + orderItemsFromDB);

				if (orderItemsFromDB == null) {
					String emptyMessage = "現在、カートに商品はありません。";
					model.addAttribute("emptyMessage", emptyMessage);
					session.setAttribute("cartList", null);
				} else if (orderItemsFromDB.size() == 0) {
					String emptyMessage = "現在、カートに商品はありません。";
					model.addAttribute("emptyMessage", emptyMessage);
					session.setAttribute("cartList", null);
				} else {
					List<OrderItem> orderItems = new ArrayList<>();
					for (OrderItem orderItem : orderItemsFromDB) {
						Item item = itemService.load(orderItem.getItemId());
						orderItem.setItem(item);
						orderItem.setSubTotal(item.getPrice() * orderItem.getQuantity());
						orderItems.add(orderItem);
					}
					System.out.println("orderItems:" + orderItems);
					session.setAttribute("cartList", orderItems);
					order = (Order) session.getAttribute("order");
					session.setAttribute("totalPrice", order.getTotalPrice());
					Integer totalTax = (int) (order.getTotalPrice() * 1 / 11);
					session.setAttribute("totalTax", totalTax);
				}
			}
		} else {// セッションにカートがあるとき
			Order order = (Order) session.getAttribute("order");
			session.setAttribute("totalPrice", order.getTotalPrice());
			Integer totalTax = (int) (order.getTotalPrice() * 1 / 11);
			session.setAttribute("totalTax", totalTax);
		}
		return "cart_list";
	}

	/**
	 * 商品詳細を表示する
	 */
	@RequestMapping("/itemDetail")
	public String itemDetail(Integer id,Model model) {

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
	 * 商品詳細画面で「カートに入れる」を押した時にショッピングカートに追加する
	 * 
	 * @param form
	 * @param model
	 * @return
	 */
	@RequestMapping("/inCart")
	public String inCart(OrderItemForm form, Model model) {

		// ユーザー情報・オーダーの取得
		User user = (User) session.getAttribute("user");
		Integer userId;
		if (user == null || user.getId() == 0) {// ログイン中のユーザーがいなければ
			userId = 0;
		} else {// ログイン中のユーザーがいれば
			userId = user.getId();
		}
		checkOrderBeforePayment(userId);
		Order order = (Order) session.getAttribute("order");
//		System.out.println("checkOrderBeforePayment:"+order);
		if (order.getId() == null || order.getId().equals("")) {
			order = orderService.insertOrder(order);
//			System.out.println("if:"+order);
		}else {
			System.out.println(111);
		}

		// OrderItem
		OrderItem orderItem = new OrderItem();
		int itemId = Integer.parseInt(form.getItemId());
		orderItem.setItemId(itemId);// itemIdの格納
		orderItem.setOrderId(order.getId());// orderIdの格納
		int quantity = Integer.parseInt(form.getQuantity());
		orderItem.setQuantity(quantity);// quantityの格納
		Item item = itemService.load(itemId);// itemの格納
		orderItem.setItem(item);
		Integer subtotal = item.getPrice() * orderItem.getQuantity();// subTotalの格納
		orderItem.setSubTotal(subtotal);
		orderItem = orderItemService.insert(orderItem);// orderItemテーブルにインサートかつidの取得(idが詰まったorderItem)
		List<OrderItem> cartList = (List<OrderItem>) session.getAttribute("cartList");//
		if (cartList == null || cartList.size() == 0) {
			cartList = new ArrayList<>();
		}
		cartList.add(orderItem);
		order.setOrderItemList(cartList);
		order.setTotalPrice(order.calcTotalPrice());
		session.setAttribute("cartList", cartList);
		session.setAttribute("order", order);// 最新のorderスコープへ格納
		orderService.update(order);// DB上のorderを最新に更新
//		System.out.println("update:"+order);
		
		System.out.println(order.getOrderItemList());
		return cartListShow(model);
	}

	/**
	 * @param index
	 * @param model
	 * @return カートから商品を削除する時
	 */
	@RequestMapping("/delete")
	public String deleteInCartItem(String id, String index, Model model) {
		Integer id1 = Integer.parseInt(id);
		int index1 = Integer.parseInt(index);
		//カートリストの更新
		List<OrderItem> cartList = (List<OrderItem>) session.getAttribute("cartList");//現状の取得
		cartList.remove(index1);// インスタンスの更新
		session.setAttribute("cartList", cartList);//スコープへの格納
		orderItemService.delete(id1);// DBの更新
		
		//オーダーの更新
		Order order = (Order) session.getAttribute("order");//取得
		order.setOrderItemList(cartList);//更新
		order.setTotalPrice(order.calcTotalPrice());
		session.setAttribute("order", order);//スコープへの格納
		System.out.println(order);
		orderService.updateOrdersWhenDeleteOrderItemFromCart(order);// DBの更新
		return cartListShow(model);
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
		System.out.println(animalId);

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
					System.out.println(itemList);
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
			model.addAttribute("word", code);
			return "item_list_pet";
		}
	}
	

	@RequestMapping("/dog")
	public String dog(String code, Integer categoryId, Model model) {

		session.setAttribute("animalId", 1);
		List<Item> itemList = itemService.findByAnimalId(1);

		model.addAttribute("link", "犬用品");
		model.addAttribute("itemList", itemList);

		return "item_list_pet";
	}
	
	@RequestMapping("/cat")
	public String cat(Model model) {

		session.setAttribute("animalId", 2);
		List<Item> itemList = itemService.findByAnimalId(2);

		model.addAttribute("link", "猫用品");
		model.addAttribute("itemList", itemList);

		return "item_list_pet";
	}

	/**
	 * お気に入り登録する処理
	 * 
	 * @return
	 */
	@RequestMapping("/favorite")
	public String favorite(FavoriteListRegisterForm favoriteListRegisterForm, Model model) {
		User user = (User) session.getAttribute("user");
		Integer userId = user.getId();
		Favorite favorite = new Favorite();
		// formのuserIdとitemIdからお気に入り登録情報を取得する
		Integer itemId = Integer.parseInt(favoriteListRegisterForm.getItemId());
		System.out.println("userId:" + userId);
		System.out.println("itemId:" + itemId);
		favorite = favoriteService.findByUserIdItemId(userId, itemId);

		System.out.println(1111111);
		System.out.println(favorite);
		if (favorite == null) {
			Favorite newFavorite = new Favorite();
			newFavorite.setItemId(itemId);
			newFavorite.setUserId(user.getId());
			Date now = new Date();
			newFavorite.setFavoriteDate(now);
			favoriteService.insertFavorite(newFavorite);
			System.out.println(2222222);
		} else if (favorite != null) {// ユーザが既にお気に入り登録済の場合
			String message = "既にお気に入り登録済です";
			model.addAttribute("message", message);
		}
		return itemDetail(itemId, model);
	}
	
	@RequestMapping("/favoriteList")
	public String favoriteListShow() {
		User user = (User) session.getAttribute("user");
		Integer userId = user.getId();
		List<Favorite> favoriteList = favoriteService.favoriteAll(userId);
		session.setAttribute("favoriteList", favoriteList);
		List<Item> favoriteItemList = new ArrayList<>();

		for (Favorite favorite : favoriteList) {
			Integer itemId = favorite.getItemId();
			System.out.println("itemId:" + itemId);
			Item item = itemService.load(itemId);
			favoriteItemList.add(0, item);
		}
		session.setAttribute("favoriteItemList", favoriteItemList);
		return "favorite_list";

	}

//	/**
//	 * 購入履歴を表示する
//	 */
//	@RequestMapping("/orderHistory")
//	public String orderHistory() {
//		return "order_history";
//	}
	
	/**
	 * @param userId orderをセッションスコープに格納する処理。
	 */
	public void checkOrderBeforePayment(Integer userId) {
		// DBに存在すれば支払い前のorderが入り、DBに存在しなければ新しいオーダーが入る
		Order order = orderService.findOrderBeforePayment(userId);
		if (order == null) {
			if (session.getAttribute("order") != null) {
				order = (Order) session.getAttribute("order");
			} else {
				order = new Order();
				order.setStatus(0);
				order.setUserId(userId);
				orderService.insertOrder(order);
			}
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
}
