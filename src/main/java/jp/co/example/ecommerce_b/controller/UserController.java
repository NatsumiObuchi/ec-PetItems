package jp.co.example.ecommerce_b.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;
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
			signinCheck(form, model);//
			return toSignin();
		} else if (userService.duplicationCheckOfEmail(form)
				|| !(form.getConfirmPassword().equals(form.getPassword()))) {// メールアドレスが重複しているか、確認用パスワードがパスワードと一致しない場合
			signinCheck(form, model);
			return toSignin();
		} else {
			userService.insertUser(form);
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
		User user = userService.loginCheck(form);
		if(user==null) {
			model.addAttribute("loginErrorMessage", "メールアドレス、またはパスワードが間違っています");
			return "login";
		}else {
			session.setAttribute("user", user);
			return "forward:/item/list";
		}
	}

	/**
	 * @return ログアウト処理
	 */
	@RequestMapping("/logout")
	public String logout() {
		session.invalidate();
		return "forward:/item/list";
	}

	/**
	 * @param form
	 * @param model 「メールアドレスが重複している」か「確認用パスワードがパスワードと一致しない」場合にエラーコメントをスコープに格納するメソッド
	 */
	private void signinCheck(UserForm form, Model model) {
		if (userService.duplicationCheckOfEmail(form)) {// メールアドレスが重複している場合
			model.addAttribute("emailError", "そのメールアドレスはすでに使われています");
		}
		if (!(form.getConfirmPassword().equals(form.getPassword()))) {// 確認用パスワードがパスワードと一致しない場合
			if (form.getPassword() != "" && form.getConfirmPassword() != "") {// 「パスワード：未入力/確認パスワード:入力」「パスワード：入力/確認パスワード:未入力」の際は以下のメッセージを表示しない。
				model.addAttribute("confirmPasswordError", "パスワードと確認用パスワードが不一致です");
			}
		}
	}
}
