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
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.form.OrderItemForm;
import jp.co.example.ecommerce_b.service.ItemService;


@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private HttpSession session;

	@Autowired
	private ItemService service;

	@ModelAttribute
	private OrderItemForm createOrderItemForm() {
		return new OrderItemForm();
	}

	/**
	 * 商品一覧を表示する
	 */
	@RequestMapping("/list")
	public String itemList(Model model) {
		List<Item> itemList = service.findAll();
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
		System.out.println(cartList);

		if (cartList == null || cartList.size() == 0) {// sessionスコープ内のcartListがからの時、エラーメッセージ表示
			cartList = new ArrayList<OrderItem>();
			String emptyMessage = "現在、カートに商品はありません。";
			model.addAttribute("emptyMessage", emptyMessage);
		}
		System.out.println(cartList);
		
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
		Item item = service.load(id);
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
		Item item = service.load(Integer.parseInt(form.getItemId()));

		OrderItem orderItem = new OrderItem();
		orderItem.setItemId(Integer.parseInt(form.getItemId()));
		orderItem.setQuantity(Integer.parseInt(form.getQuantity()));
		orderItem.setItem(item);
		Integer answer = item.getPrice() * Integer.parseInt(form.getQuantity());
		orderItem.setSubTotal(answer);

		// cartListの情報を取得
		List<OrderItem> cartList = (List<OrderItem>) session.getAttribute("cartList");
		if (cartList == null) {// cartListが空だった場合、新しくリストを追加
			cartList = new ArrayList<>();
		}
		cartList.add(orderItem);

		session.setAttribute("cartList", cartList);
		return cartListShow(model);
	}

	@RequestMapping("/delete")
	public String deleteInCartItem(String index, Model model) {
		List<OrderItem> cartList = (List<OrderItem>) session.getAttribute("cartList");
		System.out.println(index);
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
		List<Item> itemList = service.findByName(code);

		if (itemList.size() == 0) {
			List<Item> itemList2 = service.findAll();
			model.addAttribute("itemList", itemList2);
			model.addAttribute("noItemMessage", "該当の商品がございません。商品一覧を表示します。");
			return "item_list_pet";
		} else {
			model.addAttribute("itemList", itemList);
			return "item_list_pet";
		}
	}
}
