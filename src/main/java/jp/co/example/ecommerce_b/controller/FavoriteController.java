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
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.FavoriteListRegisterForm;
import jp.co.example.ecommerce_b.service.FavoriteService;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

	@Autowired
	private HttpSession session;

	@Autowired
	private FavoriteService favoriteService;

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
		if (user == null) {// ログインせずにお気に入りリストに遷移した場合、ログイン画面へ遷移する
			session.setAttribute("transitionSourcePage", "favoriteList");
			return "forward:/user/toLogin3";
		}
		Integer userId = user.getId();
		List<Favorite> favoriteList = favoriteService.favoriteAll(userId);
		if (favoriteList.size() == 0) {// ユーザ登録済でもお気に入りがゼロの時
			String message = "お気に入り登録はありません";
			model.addAttribute("message", message);
		}
		session.setAttribute("favoriteList", favoriteList);
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
		Favorite newFavorite = new Favorite();
		newFavorite.setItemId(itemId);
		Date now = new Date();
		newFavorite.setFavoriteDate(now);
		if (user == null) {// ユーザの情報はないのでここでuserIdはセットできない
			session.setAttribute("newFavorite", newFavorite);
			return favoriteListShow(model);// ログイン画面まで遷移してもらう
		} else {
			newFavorite.setUserId(user.getId());
			favoriteService.insertFavorite(newFavorite);
		}

		return "redirect:/favorite/favoriteList";
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
	public String deleteFavorite(Integer userId, Integer itemId, Model model) {
		favoriteService.delete(userId, itemId);
		return favoriteListShow(model);
	}
}
