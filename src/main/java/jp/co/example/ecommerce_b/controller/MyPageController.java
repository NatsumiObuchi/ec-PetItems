package jp.co.example.ecommerce_b.controller;

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

import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.form.UserUpdateForm;
import jp.co.example.ecommerce_b.service.UserService;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

	@Autowired
	private UserService userService;

	@Autowired
	private HttpSession session;

	@ModelAttribute
	private UserUpdateForm userUpdateForm() {
		return new UserUpdateForm();
	}

	@ModelAttribute
	private UserForm userForm() {
		return new UserForm();
	}

	/**
	 * マイページを表示する
	 * 
	 * @return
	 */
	@RequestMapping("")
	public String index() {
		return "myPage";
	}

	/**
	 * ユーザ変更前にパスワード入力を求める画面を表示
	 * 
	 * @return
	 */
	@RequestMapping("/changePermissionDisplay")
	public String permissionDisplay() {
		return "change_permission";
	}

	/**
	 * ユーザ情報変更許可の可否
	 * 
	 * @return
	 */
	@RequestMapping("/changePermission")
	public String permission(UserForm form, Model model) {
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		String oldPass = form.getPassword();
		User user = userService.findByEmail(form);
		if (user == null) {
			model.addAttribute("loginErrorMessage", "メールアドレス、またはパスワードが間違っています");
			return "change_permission";
		} else if (bcpe.matches(oldPass, user.getPassword())) {
			form.setPassword(user.getPassword());
			User user2 = userService.loginCheck(form);
			session.setAttribute("user", user2);
		}
		return "confirm_userInfo";
	}

	/**
	 * 現在のユーザ情報を確認する画面を表示
	 * 
	 * @return
	 */
	@RequestMapping("/userConfirm")
	public String confirm() {
		return "confirm_userInfo";
	}

	/**
	 * ユーザ情報を変更する画面 ユーザ情報を更新するためのformに現在のuser情報をセットし表示する
	 * 
	 * @return
	 */
	@RequestMapping("/change")
	public String change(UserUpdateForm form, Model model) {
		User user = (User) session.getAttribute("user");
		if (form.getName() == null && form.getEmail() == null && form.getZipcode() == null && form.getAddress() == null
				&& form.getTelephone() == null) {// UserUpdateformに何も情報がないとき
			form.setName(user.getName());
			form.setEmail(user.getEmail());
			form.setZipcode(user.getZipcode());
			form.setAddress(user.getAddress());
			form.setTelephone(user.getTelephone());
			model.addAttribute("form", form);
		} else {
			model.addAttribute("form", form);
		}
		return "change_userInfo";
	}

	/**
	 * @return ユーザー登録において入力フォームを初期値に戻す処理
	 */
	@RequestMapping("/reset")
	public String reset() {
		return "redirect:/myPage/change";
	}

	/**
	 * ユーザ情報の変更を確認する画面を表示
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping("/changeConfirm")
	public String changeConfirm(@Validated UserUpdateForm form, BindingResult result, Model model) {
		signinCheck(form, model);// ここでメールアドレスの重複があるかチェック
		if (result.hasErrors() || model.getAttribute("emailError") != null) {
			return change(form, model);
		} else {
			User user = (User) session.getAttribute("user");
			User updateUser = new User();
			// session内にあるuser(既にログインしているユーザ)のidを手動でコピー
			// 「ユーザ情報確認画面」からリンク遷移しているので、idをhiddenで送っていないため
			updateUser.setId(user.getId());
			BeanUtils.copyProperties(form, updateUser);
			session.setAttribute("updateUser", updateUser);
			return "change_confirm";
		}
	}

	/**
	 * ユーザ情報を更新（パスワード以外）
	 * 
	 * @return
	 */
	@RequestMapping("/update")
	public String update() {
		User updateUser = (User) session.getAttribute("updateUser");
		userService.update(updateUser);
		// 今あるsessionのユーザ情報を上書き
		session.setAttribute("user", updateUser);
		return "redirect:/myPage/finish";
	}

	/**
	 * ユーザ情報の変更を完了した際に表示する画面
	 * 
	 * @return
	 */
	@RequestMapping("/finish")
	public String finish() {
		return "change_finish";
	}

	/**
	 * ※UserControllerにも存在するため省略できるならしたい...
	 * 
	 * @param form
	 * @param model 「メールアドレスが重複している」かを判定する
	 */
	private void signinCheck(UserUpdateForm form, Model model) {
		if (userService.duplicationCheckOfEmail(form)) {// メールアドレスが重複している場合
			model.addAttribute("emailError", "そのメールアドレスはすでに使われています");
		}
	}
}
