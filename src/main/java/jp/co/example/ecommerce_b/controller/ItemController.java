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
//		System.out.println("id:" + );
//		System.out.println("quantity:" + );
		List<OrderItem> cartList = (List<OrderItem>) session.getAttribute("cartList");
		// System.out.println(cartList);

		if (cartList == null || cartList.size() == 0) {// sessionスコープ内のcartListがからの時、エラーメッセージ表示
			cartList = new ArrayList<OrderItem>();
			String emptyMessage = "現在、カートに商品はありません。";
			model.addAttribute("emptyMessage", emptyMessage);
		}
		// System.out.println(cartList);
		
		// cartList内の合計金額を計算
		Integer totalPrice = 0;
		for (OrderItem ordItem : cartList) {
			totalPrice += (int) (ordItem.getSubTotal() * 1.1);
		}
		
		
		
		// 消費税を計算
		Integer totalTax = (int) (totalPrice * 0.1);

		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalTax", totalTax);

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
//		System.out.println("id:" + form.getItemId());
//		System.out.println("quantity:" + form.getQuantity());
		// ショッピングカートに入れる商品の情報を商品idを元に取得
		Item item = itemService.load(Integer.parseInt(form.getItemId()));

		OrderItem orderItem = new OrderItem();
		orderItem.setItemId(Integer.parseInt(form.getItemId()));
		orderItem.setQuantity(Integer.parseInt(form.getQuantity()));
		orderItem.setItem(item);
		Integer answer = item.getPrice() * Integer.parseInt(form.getQuantity());
		orderItem.setSubTotal(answer);
		
		User user = (User) session.getAttribute("user");

		Integer userId;
		if(user==null) {
			userId=0;
		}else {
			userId=user.getId();
		}
		checkOrderBeforePayment(userId);
		Order order = (Order) session.getAttribute("order");
		System.out.println("いまセッションにあるorderのIDは" + order.getId());
		
		if(order.getId()==null) {//からっぽのorderなら
//			orderテーブルにインサート
			
			order = orderService.insertOrder(order);
		}
		
		//100%orderがidを持っている状態になる

		
//		orderのidをorderItemのorderIdにセット
		orderItem.setOrderId(order.getId());
		
		// orderItemテーブルにインサート
		orderItemService.insert(orderItem);
		


		// cartListの情報を取得
		List<OrderItem> cartList = (List<OrderItem>) session.getAttribute("cartList");
		// System.out.println(cartList);
		if (cartList == null) {// cartListが空だった場合、新しくリストを追加
			cartList = new ArrayList<>();
		}
		// ショッピングカート（cartList）に追加
		cartList.add(orderItem);
		
		order.setOrderItemList(cartList);
		order.setTotalPrice(order.calcTotalPrice());

		session.setAttribute("cartList", cartList);
		

		//update
		orderService.update(order);
		
		
		return cartListShow(model);
	}

	@RequestMapping("/delete")
	public String deleteInCartItem(String index, Model model) {
		List<OrderItem> cartList = (List<OrderItem>) session.getAttribute("cartList");
		// System.out.println(index);
		cartList.remove(Integer.parseInt(index));

		session.setAttribute("cartList", cartList);
		return cartListShow(model);
	}

	/**
	 * 商品をあいまい検索する ※該当の商品がない場合は全件表示する。
	 * 
	 * @param code
	 * @param model
	 * @return
	 */
	@RequestMapping("/search")
	public String searchItem(String code, Model model) {
		List<Item> itemList = itemService.findByName(code);

		if (itemList.size() == 0) {
			List<Item> itemList2 = itemService.findAll();
			model.addAttribute("itemList", itemList2);
			model.addAttribute("noItemMessage", "該当の商品がございません。商品一覧を表示します。");
			return "item_list_pet";
		} else {
			model.addAttribute("itemList", itemList);
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
	 * @param user ログイン中のユーザーの、支払い前のオーダーをセッションスコープに格納する処理。
	 */
	public void checkOrderBeforePayment(Integer userId) {
		// 存在すればそのorderが入り、存在しなければnullがはいる。
		Order order = orderService.findOrderBeforePayment(userId);
		if (order == null) {
			if (session.getAttribute("order") != null) {
				order = (Order) session.getAttribute("order");
			} else {
				order = new Order();
				order.setStatus(0);
			}
		}
		System.out.println("sessionに格納されるorderのIDは" + order.getId());
		session.setAttribute("order", order);
	}
	
	
}
