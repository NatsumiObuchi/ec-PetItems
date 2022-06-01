package jp.co.example.ecommerce_b.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	private UserService userService;

	@RequestMapping("/toSignup")
	public String toSignUp() {
		return "register_user";
	}

	@RequestMapping("/signup") // 要修正「ダブルサブミット対策（リダイレクト）」「validationチェック（現時点だと、どんな状態でも登録できてしまう）」
	public String signup(UserForm form) {
		User user = new User();
		BeanUtils.copyProperties(form, user);
		System.out.println(user);// あとで消す。
		userService.insertUser(user);
		return "item_list_pizza";
	}
}
