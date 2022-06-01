package jp.co.example.ecommerce_b.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
	private UserService userService;

	@RequestMapping("/toSignup")
	public String toSignUp() {
		return "register_user";
	}

	@RequestMapping("/signup") // 要修正「ダブルサブミット対策（リダイレクト）」
	public String signup(@Validated UserForm form, BindingResult result,Model model) {
		if (result.hasErrors()) {
			signupCheck(form, model);//
			return toSignUp();
		} else if (userService.duplicationCheckOfEmail(form)
				|| !(form.getConfirmPassword().equals(form.getPassword()))) {// メールアドレスが重複しているか、確認用パスワードがパスワードと一致しない場合
			signupCheck(form, model);
			return toSignUp();
		} else {
			userService.insertUser(form);
			return "login";// あとでここをリダイレクトにする。
		}
	}

	/**
	 * @param form
	 * @param model 「メールアドレスが重複している」か「確認用パスワードがパスワードと一致しない」場合にエラーコメントをスコープに格納するメソッド
	 */
	private void signupCheck(UserForm form, Model model) {
		if (userService.duplicationCheckOfEmail(form)) {// メールアドレスが重複している場合
			model.addAttribute("emailError", "そのメールアドレスはすでに使われています");
		}
		if (!(form.getConfirmPassword().equals(form.getPassword()))) {// 確認用パスワードがパスワードと一致しない場合
			model.addAttribute("confirmPasswordError", "パスワードと確認用パスワードが不一致です");
		}
	}
}
