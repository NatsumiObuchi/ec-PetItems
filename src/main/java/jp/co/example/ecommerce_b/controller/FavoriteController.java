package jp.co.example.ecommerce_b.controller;

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
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.FavoriteListRegisterForm;
import jp.co.example.ecommerce_b.service.FavoriteService;
import jp.co.example.ecommerce_b.service.ItemService;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

	@Autowired
	private HttpSession session;

	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private ItemService itemService;

	@ModelAttribute
	private FavoriteListRegisterForm favoriteListRegisterForm() {
		return new FavoriteListRegisterForm();
	}

	/**
	 * お気に入りリストの表示
	 * 
	 * @return
	 */
	@RequestMapping("/favoriteList")
	public String favoriteListShow(Model model) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			session.setAttribute("transitionSourcePage", "favoriteList");
			return "forward:/user/toLogin3";
		}
		Integer userId = user.getId();
		List<Favorite> favoriteList = favoriteService.favoriteAll(userId);

		if (favoriteList == null) {// ユーザ登録済でもお気に入りがゼロの時
			String message = "お気に入り登録はありません";
			model.addAttribute("message", message);
			session.setAttribute("favoriteList", null);
			return "favorite_list";
		}

		session.setAttribute("favoriteList", favoriteList);
		for (Favorite favorite : favoriteList) {
			Integer itemId = favorite.getItemId();
			System.out.println("itemId:" + itemId);
			Item item = itemService.load(itemId);
			favorite.setItem(item);
		}
		return "favorite_list";
	}

	/**
	 * お気に入り登録する処理
	 * 
	 * @return
	 */
	@RequestMapping("/insert")
	public String favorite(FavoriteListRegisterForm favoriteListRegisterForm, Model model) {
		User user = (User) session.getAttribute("user");
		Integer itemId = Integer.parseInt(favoriteListRegisterForm.getItemId());
		if (user == null) {// ユーザの情報はないのでuserIdはセットできない
			Favorite newFavorite = new Favorite();
			newFavorite.setItemId(itemId);
			Date now = new Date();
			newFavorite.setFavoriteDate(now);
			session.setAttribute("newFavorite", newFavorite);
			System.out.println(newFavorite);
			return favoriteListShow(model);
		}
		Integer userId = user.getId();
		Favorite favorite = new Favorite();
		// formのuserIdとitemIdからお気に入り登録情報を取得する
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
			String message1 = "お気に入り登録が完了しました！";
			model.addAttribute("message1", message1);
		} else if (favorite != null) {// ユーザが既にお気に入り登録済の場合
			String message = "既にお気に入り登録済です";
			model.addAttribute("message", message);
		}
		return "forward:/item/itemDetail";
	}

	/**
	 * ユーザ情報を取得できた後、 元々ユーザ登録していなかったユーザがログインした後にお気に入り登録される処理 &
	 * 登録済ユーザがログインした際に追加でお気に入り登録される処理
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/insert2")
	public String favorite2(Model model) {
		User user = (User) session.getAttribute("user");
		Favorite newFavorite = (Favorite) session.getAttribute("newFavorite");
		if (newFavorite == null) {
			return favoriteListShow(model);
		}
		Favorite oldFavorite = favoriteService.findByUserIdItemId(user.getId(), newFavorite.getItemId());
		newFavorite.setUserId(user.getId());// 取得できたuserIdをここでやっとセット

		if (oldFavorite == null) {
			favoriteService.insertFavorite(newFavorite);
		} else {// 既に登録済であったユーザの処理
			String message = "既に登録済の商品です";
			model.addAttribute("message", message);
		}

		return favoriteListShow(model);
	}

	/**
	 * お気に入りリストの削除
	 * 
	 * @param userId
	 */
	@RequestMapping("/deleteFavorite")
	public String deleteFavorite(String itemId, Model model) {
		Integer id = Integer.parseInt(itemId);
		System.out.println(id);
		favoriteService.delete(id);

		return favoriteListShow(model);
	}
}
