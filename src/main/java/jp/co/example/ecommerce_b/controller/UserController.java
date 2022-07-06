package jp.co.example.ecommerce_b.controller;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Addressee;
import jp.co.example.ecommerce_b.domain.Point;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.service.AddresseeService;
import jp.co.example.ecommerce_b.service.CouponServise;
import jp.co.example.ecommerce_b.service.PointService;
import jp.co.example.ecommerce_b.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@ModelAttribute
	private UserForm setUpUserForm() {
		return new UserForm();
	}

	@Autowired
	private HttpSession session;

	@Autowired
	private UserService userService;
	
	@Autowired
	private PointService pointService;
	
	@Autowired
	private AddresseeService addresseeService;
	
	@Autowired
	private CouponServise couponServise;

	/**
	 * @return ユーザー登録画面に遷移するだけの処理
	 */
	@RequestMapping("/toSignin")
	public String toSignin() {
		return "register_user";
	}

	/**
	 * @param form
	 * @param result
	 * @param model
	 * @return ユーザーを登録する処理
	 */
	@RequestMapping("/signin")
	public String signin(@Validated UserForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "register_user";
		} else if (userService.duplicationCheckOfEmail(form)
				|| !(form.getConfirmPassword().equals(form.getPassword()))) {// メールアドレスが重複しているか、確認用パスワードがパスワードと一致しない場合
			userService.signinCheck(form, model);
			return "register_user";
		} else {// ユーザー登録処理
			User user = new User();
			BeanUtils.copyProperties(form, user);
			User user2 = userService.insertUser(user);

			// 今登録されたユーザのuserIdに紐付けてpointテーブルに情報を追加
			pointService.insertPoint(user2);

			return "redirect:/user/toLogin";
		}
	}

	/**
	 * @return ユーザー登録において入力フォームを初期値に戻す処理
	 */
	@RequestMapping("/reset")
	public String reset() {
		return "redirect:/user/toSignin";
	}

	/**
	 * @return ログイン画面に遷移するだけの処理
	 */
	@RequestMapping("/toLogin")
	public String toLogin() {
		return "login";
	}

	/**
	 * @param form
	 * @param model
	 * @return ログイン処理
	 */
	@RequestMapping("/login")
	public String login(UserForm form, Model model) {

		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		String inputPass = form.getPassword();
		User user = userService.findByEmail(form);
		if (user == null) {
			model.addAttribute("loginErrorMessage", "メールアドレス、またはパスワードが間違っています");
			return "login";
		} else if (bcpe.matches(inputPass, user.getPassword())) {
			form.setPassword(user.getPassword());
			User user2 = userService.loginCheck(form);
			session.setAttribute("user", user2);

			// ユーザがログインしたタイミングで使用できないクーポン(有効期限切れ)のdeletedをtrueに変える
			couponServise.usedUsersCoupon(user2.getId());
			
			// ユーザのポイント情報をログインしたタイミングで取得
			Point point = pointService.load(user.getId());
			session.setAttribute("point", point);

			// ユーザが登録済のお届け先一覧をログインしたタイミングでsessionにセットする
			List<Addressee> addresseeList = addresseeService.findAddresseeByUserId(user.getId());
			session.setAttribute("addresseeList", addresseeList);

			switch (String.valueOf(session.getAttribute("transitionSourcePage"))) {
			case "order":
				session.setAttribute("transitionSourcePage", null);
				return "forward:/order";
			case "favoriteList":
				session.setAttribute("transitionSourcePage", null);
				return "forward:/favorite/insert2";
			case "orderHistory":
				session.setAttribute("transitionSourcePage", null);
				return "forward:/order/orderHistory";
			default:
				return "forward:/item/top";
			}
		} else {
			model.addAttribute("loginErrorMessage", "メールアドレス、またはパスワードが間違っています");
			return "login";
		}
	}

	/**
	 * @return ログアウト処理
	 */
	@RequestMapping("/logout")
	public String logout() {
		session.invalidate();
		return "forward:/item/top";
	}

	/**
	 * @param model(ログインしてない人が注文履歴を見ようとした際のメッセージ)
	 * @return ログイン画面
	 */
	@RequestMapping("/toLogin2")
	public String index2(Model model) {
		User user = (User) session.getAttribute("user");
		if(user==null) {
			model.addAttribute("historyMessage","注文履歴のご確認にはログインもしくはユーザー登録が必要です。");
		}
		return "login";
	}

	/**
	 * ログインしてない人がお気に入り登録をしようとした際に入ってくる処理
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toLogin3")
	public String index3(Model model) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			model.addAttribute("favoriteMessage", "お気に入り登録にはログイン、もしくはユーザ登録が必要です。");
		}
		return "login";
	}
}
