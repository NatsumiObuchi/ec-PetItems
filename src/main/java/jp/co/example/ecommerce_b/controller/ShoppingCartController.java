package jp.co.example.ecommerce_b.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/shopping")
public class ShoppingCartController {

	@Autowired
	private HttpSession session;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private OrderService orderService;

	/**
	 * カートに入れた商品を全表示する カートに商品がない場合、「カートに商品がありません。」と表示する
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/cartList")
	public String cartListShow(Model model) {

		// ユーザー周り
		User user = (User) session.getAttribute("user");
		if (user == null) {
			user = new User();
			user.setId(0);
		}

		// カートリスト周り
		checkOrderBeforePayment(user.getId());// ユーザーの未払いオーダーがあった場合、その「オーダー」をセッションに格納。なくてもuserId=0の「オーダー」を格納。
		List<OrderItem> cartList = (List<OrderItem>) session.getAttribute("cartList");
		if (cartList == null || cartList.size() == 0) {// sessionスコープにカートがない時
			if (user.getId() == 0) {// ユーザーIDが0の場合、過去の他の未登録ユーザーのオーダーがDBにある可能性がある。
				String emptyMessage = "現在、カートに商品はありません。";
				model.addAttribute("emptyMessage", emptyMessage);
				session.setAttribute("cartList", null);
			} else {// ログインユーザーの場合
				Order order = (Order) session.getAttribute("order");
				Integer orderId = order.getId();
				List<OrderItem> orderItemsFromDB = orderItemService.findByOrderId(orderId);

				if (orderItemsFromDB == null || orderItemsFromDB.size() == 0) {
					String emptyMessage = "現在、カートに商品はありません。";
					model.addAttribute("emptyMessage", emptyMessage);
					session.setAttribute("cartList", null);
				} else {
					List<OrderItem> orderItems = new ArrayList<>();
					for (OrderItem orderItem : orderItemsFromDB) {
						orderItem.setSubTotal(orderItem.getItem().getPrice() * orderItem.getQuantity());
						orderItems.add(orderItem);
					}
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
	 * 商品詳細画面で「カートに入れる」を押した時にショッピングカートに追加する
	 * 
	 * @param form
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
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
		if (order.getId() == null || order.getId().equals("")) {// この処理の意味がわからない
			order = orderService.insertOrder(order);
		}

		// OrderItemを生成→ショッピングカートにセットする
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
	@SuppressWarnings("unchecked")
	@RequestMapping("/delete")
	public String deleteInCartItem(String id, String index, Model model) {
		Integer id1 = Integer.parseInt(id);
		int index1 = Integer.parseInt(index);
		// カートリストの更新
		List<OrderItem> cartList = (List<OrderItem>) session.getAttribute("cartList");// 現状の取得
		cartList.remove(index1);// インスタンスの更新
		session.setAttribute("cartList", cartList);// スコープへの格納
		orderItemService.delete(id1);// DBの更新

		// オーダーの更新
		Order order = (Order) session.getAttribute("order");// 取得
		order.setOrderItemList(cartList);// 更新
		order.setTotalPrice(order.calcTotalPrice());
		session.setAttribute("order", order);// スコープへの格納
		orderService.updateOrdersWhenDeleteOrderItemFromCart(order);// DBの更新
		return cartListShow(model);
	}

	/**
	 * @param userId orderをセッションスコープに格納する処理。
	 */
	public void checkOrderBeforePayment(Integer userId) {
		Order order = orderService.findOrderBeforePayment(userId);
		if (order == null) {// ユーザーの支払い前のオーダー(status=0)がDBにない時
			if (session.getAttribute("order") != null) {//
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
