package jp.co.example.ecommerce_b.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.service.ItemService;


@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private HttpSession session;

	@Autowired
	private ItemService service;

	/**
	 * 商品一覧を表示する
	 */
	@RequestMapping("/list")
	public String itemList(Model model) {
		List<Item> itemList = service.findAll();
		model.addAttribute("itemList", itemList);
		return "item_list_pizza";
	}

	@RequestMapping("/cartList")
	public String cartList(Model model) {
		List<Item> cartList = (List<Item>) session.getAttribute("cartList");
		if (cartList == null) {
			cartList = new ArrayList<>();
			String emptyMessage = "現在、ショッピングカートに商品はありません。";
			model.addAttribute("emptyMessage", emptyMessage);
			session.setAttribute("cartList", cartList);
		}
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
	 * 商品をあいまい検索する ※該当の商品がない場合は全件表示する。
	 */
	@RequestMapping("/search")
	public String searchItem(String code, Model model) {
		List<Item> itemList = service.findByName(code);

		if (itemList.size() == 0) {
			List<Item> itemList2 = service.findAll();
			model.addAttribute("itemList", itemList2);
			model.addAttribute("noItemMessage", "該当の商品がございません。商品一覧を表示します。");
			return "item_list_pizza";
		} else {
			model.addAttribute("itemList", itemList);
			return "item_list_pizza";
		}
	}
}
