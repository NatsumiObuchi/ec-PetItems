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

import jp.co.example.ecommerce_b.domain.Addressee;
import jp.co.example.ecommerce_b.domain.Review;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.domain.UsersCoupon;
import jp.co.example.ecommerce_b.form.InsertAddresseeForm;
import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.form.UserPasswordUpdateForm;
import jp.co.example.ecommerce_b.form.UserUpdateForm;
import jp.co.example.ecommerce_b.service.AddresseeService;
import jp.co.example.ecommerce_b.service.CouponServise;
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
	private AddresseeService addresseeService;

	@Autowired
	private CouponServise couponServise;

	@Autowired
	private HttpSession session;

	@ModelAttribute
	private UserUpdateForm userUpdateForm() {
		return new UserUpdateForm();
	}

	@ModelAttribute
	private InsertAddresseeForm insertAddresseeForm() {
		return new InsertAddresseeForm();
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
	 * test1 マイページを表示する
	 * 
	 * @return
	 */
	@RequestMapping("")
	public String index() {
		return "myPage";
	}

	/**
	 * test2,test3 ユーザ変更前にパスワード入力を求める画面を表示
	 * 
	 * @return
	 */
	@RequestMapping("/changePermissionDisplay")
	public String permissionDisplay() {
		Integer count = (Integer) session.getAttribute("count");
		if (count == null) {
			count = 1;// 1回目はユーザ認証が必要
			session.setAttribute("count", count);
			return "change_permission";
		}
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
		User user = (User) session.getAttribute("user");
		System.out.println(user.getPassword());
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		String inputPass = form.getPassword();
		System.out.println(inputPass);
		if (bcpe.matches(inputPass, user.getPassword())) {// 入力されたパスワードとハッシュ化されたパスワードがマッチ（照合）
			if (!(form.getEmail().equals(user.getEmail()))) {// 今ログインしているユーザのメールアドレスでなければエラー
				model.addAttribute("AuthenticationFailureMessage", "メールアドレス、またはパスワードが間違っています");
				return "change_permission";
			}
		} else {// そもそもハッシュ化されたパスワードと入力されたパスワードが一致していない
			model.addAttribute("AuthenticationFailureMessage", "メールアドレス、またはパスワードが間違っています");
			return "change_permission";
		}
		return "confirm_userInfo";
	}

	/**
	 * test4,test5 ユーザ情報を変更する画面 ユーザ情報を更新するためのformに現在のuser情報をセットし表示する
	 * 
	 * @return
	 */
	@RequestMapping("/change")
	public String change(UserUpdateForm form, Model model) {
		User user = (User) session.getAttribute("user");
		if (form.getName() == null && form.getEmail() == null && form.getZipcode() == null && form.getAddress() == null
				&& form.getTelephone() == null) {// UserUpdateformに何も情報がないとき(最初にこの画面にきた時)
			BeanUtils.copyProperties(user, form);
		}
		model.addAttribute("form", form);
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
		User user = (User) session.getAttribute("user");
		signinCheck(form, user.getId(), model);// ここでメールアドレスの重複があるかチェック
		if (result.hasErrors() || model.getAttribute("emailError") != null) {
			return change(form, model);
		} else {
			User updateUser = new User();
			// session内にあるuser(既にログインしているユーザ)のidを手動でコピー
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
			String inputPass = form.getPassword();
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashPass = passwordEncoder.encode(inputPass);// パスワードのハッシュ化
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
	 * マイレビューを表示する
	 * 
	 * @param id userId
	 * @return
	 */
	@RequestMapping("/myReview")
	public String myReviewShow(Integer id, Model model) {
		List<Review> reviewList = new ArrayList<>();
		reviewList = itemService.findReviewByUserId(id);
		if (reviewList == null) {
			model.addAttribute("non", "これまでのレビュー投稿はありません。");
		}
		session.setAttribute("reviewList", reviewList);
		return "my_review";
	}

	/**
	 * お届け先情報を変更・登録する画面を表示する
	 * 
	 * @param id userId
	 * @return
	 */
	@RequestMapping("/addressee")
	public String addressee(Integer id, Model model) {
		List<Addressee> addresseeList = addresseeService.findAddresseeByUserId(id);
		if (addresseeList == null || addresseeList.size() == 0) {
			model.addAttribute("non", "お届け先情報は現在登録されていません。追加してください。");
		}
		session.setAttribute("addresseeList", addresseeList);
		return "addressee";
	}

	/**
	 * test8,test9 お届け先情報追加画面
	 * 
	 * @return
	 */
	@RequestMapping("/registerShow")
	public String registerShow(Integer id, Model model) {
		List<Addressee> addresseeList = addresseeService.findAddresseeByUserId(id);
		System.out.println("~~~~~~~~~~~" + addresseeList);
		if (addresseeList.size() == 3) {
			model.addAttribute("full", "お届け先情報の登録は3件までです。追加するにはどれかを削除してください。");
			return addressee(id, model);
		}
		return "addressee_register";
	}

	/**
	 * test9,test10 ユーザのお届け先を新規追加する
	 * 
	 * @param insertAddresseeForm お届け先登録情報
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping("/addresseeRegister")
	public String addresseeRegister(@Validated InsertAddresseeForm insertAddresseeForm, BindingResult result,
			Model model) {
		System.out.println(insertAddresseeForm);
		if (result.hasErrors()) {
			return "addressee_register";
		}
		// 値のコピー
		Addressee newAddressee = new Addressee();
		BeanUtils.copyProperties(insertAddresseeForm, newAddressee);
		addresseeService.addresseeRegister(newAddressee);

		return addressee(insertAddresseeForm.getUserId(), model);
	}

	/**
	 * test12 お届け先情報を削除する
	 * 
	 * @param id          userId
	 * @param addresseeId
	 * @param model
	 * @return
	 */
	@RequestMapping("/addresseeDelete")
	public String addresseeDelete(Integer id, Integer addresseeId, Model model) {
		addresseeService.deleteAddressee(id, addresseeId);
		return addressee(id, model);
	}

	/**
	 * test13,test14 デフォルトのお届け先を設定する
	 * 
	 * @param id          ユーザーid
	 * @param addresseeId
	 * @param setting
	 * @param model
	 * @return
	 */
	@RequestMapping("/settingAddressee")
	public String settingAddressee(Integer id, Integer addresseeId, boolean setting, Model model) {
		if (addresseeId == null) {
			model.addAttribute("nonRadio", "お届け先が選択されていません！");
			return addressee(id, model);
		}
		addresseeService.setting(id, addresseeId, setting);
		model.addAttribute("update", "更新しました！");
		model.addAttribute("updateText", "※複数お届け先情報がある方は、一番上のお届け先が「注文確認画面」のデフォルトで表示されます");
		return addressee(id, model);
	}

	/**
	 * 
	 * メールアドレスは他者と被っていないかだけを判定する
	 * 
	 * @param form
	 * @param model
	 */
	private void signinCheck(UserUpdateForm form, Integer userId, Model model) {
		if (userService.duplicationCheckOfEmail(form, userId) == true) {
			model.addAttribute("emailError", "そのメールアドレスはすでに使われています");
		}
	}

	/**
	 * test6,test7 マイクーポンを表示する
	 * 
	 * @param id userId
	 * @return
	 */
	@RequestMapping("/myCoupon")
	public String myCoupon(Integer id, Model model) {
		List<UsersCoupon> usersCouponList = couponServise.findAllUsersCoupon(id);
		session.setAttribute("usersCouponList", usersCouponList);
		System.out.println("1111111111" + usersCouponList);
		if (usersCouponList.size() == 0) {
			model.addAttribute("nonCoupon", "所持しているクーポンはありません。");
			System.out.println("22222222222" + usersCouponList);

		}
		return "myCoupon";
	}

}
