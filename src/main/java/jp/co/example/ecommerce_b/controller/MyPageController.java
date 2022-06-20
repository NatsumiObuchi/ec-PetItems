package jp.co.example.ecommerce_b.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Review;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.form.UserPasswordUpdateForm;
import jp.co.example.ecommerce_b.form.UserUpdateForm;
import jp.co.example.ecommerce_b.service.ItemService;
import jp.co.example.ecommerce_b.service.UserService;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

	@Autowired
	private UserService userService;

	@Autowired
	private ItemService itemService;

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

	@ModelAttribute
	private UserPasswordUpdateForm userPasswordUpdateForm() {
		return new UserPasswordUpdateForm();
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
		Integer count = (Integer) session.getAttribute("count");
		System.out.println(count);
		if (count == null) {
			count = 1;// 1回目のユーザ認証
			session.setAttribute("count", count);
			return "change_permission";
		}
		count++;// 2回目以降のログイン(countが加算される)
		session.setAttribute("count", count);
		return "confirm_userInfo";
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
		if (user == null) {// メールアドレスで探しデータが存在しなかった場合
			model.addAttribute("AuthenticationFailureMessage", "メールアドレス、またはパスワードが間違っています");
			return "change_permission";
		} else if (bcpe.matches(oldPass, user.getPassword())) {// 入力されたパスワードとハッシュ化されたデータベース内のパスワードが合っているか照合
			form.setPassword(user.getPassword());
			User user2 = userService.loginCheck(form);
			session.setAttribute("user", user2);
		} else {
			model.addAttribute("AuthenticationFailureMessage", "メールアドレス、またはパスワードが間違っています");
			return "change_permission";
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
	 * パスワード変更を行う画面の表示
	 * 
	 * @return
	 */
	@RequestMapping("/changePassDisplay")
	public String changePassDisplay() {
		return "change_password";
	}

	@RequestMapping("/changePass")
	public String changePass(@Validated UserPasswordUpdateForm form, BindingResult result, Model model) {
		User user = (User) session.getAttribute("user");
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		if (result.hasErrors()) {
			return changePassDisplay();
		} else if (!(form.getConfirmPassword().equals(form.getPassword()))) {// 確認用パスワードがパスワードと一致しない場合
			model.addAttribute("confirmPasswordError", "確認用パスワードとパスワードが一致していません");
			return changePassDisplay();
		} else if (bcpe.matches(form.getPassword(), user.getPassword())) {// 入力されたパスワードが以前のものと同じ場合、エラー
			model.addAttribute("duplicatePasswordError", "以前のパスワードと同じ場合、変更できません");
			return changePassDisplay();
		} else {
			String oldPass = form.getPassword();
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashPass = passwordEncoder.encode(oldPass);// パスワードのハッシュ化
			user.setPassword(hashPass);
			userService.updatePassword(user);
			return "redirect:/myPage/finish";
		}
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
	 * 退会処理を完了する
	 * 
	 * @return
	 */
	@RequestMapping("/withdrawal")
	public String withdrawal(Integer id) {
		userService.delete(id);
		session.removeAttribute("user");// sessionスコープ内にあるuserの情報を削除
		return "withdrawal";
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

	@RequestMapping("/myReview")
	public String myReviewShow(Integer id) {
		List<Review> reviewList = new ArrayList<>();
		reviewList = itemService.findReviewByUserId(id);
		session.setAttribute("reviewList", reviewList);
		return "my_review";
	}
}
