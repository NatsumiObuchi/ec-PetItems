package jp.co.example.ecommerce_b.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.OrderItemForm;
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

	@ModelAttribute
	private OrderItemForm createOrderItemForm() {
		return new OrderItemForm();
	}

	/**
	 * 商品一覧を表示する
	 */
	@RequestMapping("/list")
	public String itemList(Model model) {
		List<Item> itemList = itemService.findAll();
		model.addAttribute("itemList", itemList);
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
		List<OrderItem> cartList = (List<OrderItem>) session.getAttribute("cartList");


		if (cartList == null || cartList.size() == 0) {// sessionスコープ内のcartListがからの時、エラーメッセージ表示
			cartList = new ArrayList<OrderItem>();
			String emptyMessage = "現在、カートに商品はありません。";
			model.addAttribute("emptyMessage", emptyMessage);
		}
		// cartList内の合計金額を計算
		Order order = (Order) session.getAttribute("order");
		session.setAttribute("totalPrice", order.getTotalPrice());
		
		// 消費税
		Integer totalTax = (int) (order.getTotalPrice() * 1 / 11);
		session.setAttribute("totalTax", totalTax);

		return "cart_list";
	}

	/**
	 * 商品詳細を表示する
	 */
	@RequestMapping("/itemDetail")
	public String itemDetail(Integer id,Model model) {
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
		if (order.getId() == null) {
			order = orderService.insertOrder(order);
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
	public String searchItem(String code, Integer animalId, Model model) {

		List<Item> itemList = itemService.findByName(code);

//		検索結果の該当がない場合＋入力がない場合
		if (itemList.size() == 0 || code.equals("　") || code.equals(" ") || code.isEmpty()) {
			List<Item> itemList2 = new ArrayList<>();
			switch (animalId) {
			case 0:
				itemList2 = itemService.findAll();
				if (code.isEmpty()) {
					model.addAttribute("noItemMessage", "すべての商品一覧を表示します。");
				} else {
					model.addAttribute("noItemMessage", "該当の商品がございません。商品一覧を表示します。");
				}
				break;
			case 1:
				itemList2 = itemService.findByAnimalId(animalId);
				if (code.isEmpty()) {
					model.addAttribute("noItemMessage", "絞り込み検索しました。犬用商品一覧を表示します。");
				} else {
					model.addAttribute("noItemMessage", "該当の商品がございません。犬用商品一覧を表示します。");
				}
				break;
			case 2:
				itemList2 = itemService.findByAnimalId(animalId);
				if (code.isEmpty()) {
					model.addAttribute("noItemMessage", "絞り込み検索しました。猫用商品一覧を表示します。");
				} else {
					model.addAttribute("noItemMessage", "該当の商品がございません。猫用商品一覧を表示します。");
				}
				break;
			}
			model.addAttribute("itemList", itemList2);
			model.addAttribute("word", code);
			return "item_list_pet";
		} else {

//			入力フォームに何かしらの入力があった場合
			List<Item> itemList3 = itemService.findByNameAndAnimalId(code, animalId);
			if (animalId == 0) {
				model.addAttribute("itemList", itemList);
				model.addAttribute("noItemMessage", "検索結果を表示します。");
			} else if (itemList3.size() == 0) {

//				何かしら入力があったが、絞り込んだ際には該当の結果がない場合
				List<Item> itemList2 = new ArrayList<>();
				switch (animalId) {
					case 1:
						itemList2 = itemService.findByAnimalId(animalId);
						model.addAttribute("noItemMessage", "該当の商品がございません。犬用商品一覧を表示します。");
						break;
					case 2:
						itemList2 = itemService.findByAnimalId(animalId);
						model.addAttribute("noItemMessage", "該当の商品がございません。猫用商品一覧を表示します。");
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
			}
		}
		session.setAttribute("order", order);
	}
	
	
}
