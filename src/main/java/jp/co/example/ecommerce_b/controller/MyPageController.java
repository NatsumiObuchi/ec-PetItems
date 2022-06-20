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
import jp.co.example.ecommerce_b.form.InsertAddresseeForm;
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
		User user = (User) session.getAttribute("user");
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		String oldPass = form.getPassword();
		System.out.println(user);
		if (bcpe.matches(oldPass, user.getPassword())) {// ハッシュ化されたパスワードと入力されたパスワードがマッチしても...
			if (!(form.getEmail().equals(user.getEmail()))) {// 今ログインしているユーザのメールアドレスでなければエラー
				model.addAttribute("AuthenticationFailureMessage", "メールアドレス、またはパスワードが間違っています");
				return "change_permission";
			} else {// ログインしてるユーザのメールアドレスであればユーザ認証成功
				session.setAttribute("user", user);
			}
		} else {// そもそもハッシュ化されたパスワードと入力されたパスワードが一致していない
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
		List<Addressee> addresseeList = userService.findAddresseeByUserId(id);
		if (addresseeList == null || addresseeList.size() == 0) {
			model.addAttribute("non", "お届け先情報は現在登録されていません。追加してください。");
		}
		session.setAttribute("addresseeList", addresseeList);
		return "addressee";
	}

	/**
	 * お届け先情報追加画面
	 * 
	 * @return
	 */
	@RequestMapping("/registerShow")
	public String registerShow(Integer id, Model model) {
		List<Addressee> addresseeList = userService.findAddresseeByUserId(id);
		if (addresseeList.size() == 3) {
			model.addAttribute("full", "お届け先情報の登録は3件までです。追加するにはどれかを削除してください。");
			return addressee(id, model);
		}
		return "addressee_register";
	}

	/**
	 * ユーザのお届け先を新規追加する
	 * 
	 * @param id          userId
	 * @param addresseeId
	 * @param model
	 * @return
	 */
	@RequestMapping("/addresseeRegister")
	public String addresseeRegister(@Validated InsertAddresseeForm insertAddresseeForm, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "addressee_register";
		}
		// 値のコピー
		Addressee newAddressee = new Addressee();
		BeanUtils.copyProperties(insertAddresseeForm, newAddressee);

		// ログインユーザが最後に登録したaddresseeIdを取得
		Addressee addressee = userService.lastAddlesseeId(insertAddresseeForm.getUserId());
		if (addressee == null) {// 初めてお届け先情報を登録する人はaddresseeIdに1をセット
			newAddressee.setAddresseeId(1);
		} else {// それ以外の人（既に登録済のお届け先が存在する）
			Integer lastAddresseeId = addressee.getAddresseeId();
			newAddressee.setAddresseeId(lastAddresseeId + 1);// 新しいaddresseeIdを手動でセット
		}

		userService.addresseeRegister(newAddressee);
		return addressee(insertAddresseeForm.getUserId(), model);
	}

	/**
	 * お届け先情報を削除する
	 * 
	 * @param id          userId
	 * @param addresseeId
	 * @param model
	 * @return
	 */
	@RequestMapping("/addresseeDelete")
	public String addresseeDelete(Integer id, Integer addresseeId, Model model) {
		userService.deleteAddressee(id, addresseeId);
		return addressee(id, model);
	}

	@RequestMapping("/settingAddressee")
	public String settingAddressee(Integer id, Integer addresseeId, boolean setting, Model model) {
		if (addresseeId == null) {
			model.addAttribute("nonRadio", "お届け先を設定してください。");
			return addressee(addresseeId, model);
		}
		switch (addresseeId) {// お届け先として設定したいaddresseeIdと、それ以外のsetting_addresseeはfalse
		case 1:
			userService.setting(id, addresseeId, setting);
			userService.setting(id, 2, false);
			userService.setting(id, 3, false);
			break;
		case 2:
			userService.setting(id, addresseeId, setting);
			userService.setting(id, 1, false);
			userService.setting(id, 3, false);
			break;
		case 3:
			userService.setting(id, addresseeId, setting);
			userService.setting(id, 1, false);
			userService.setting(id, 2, false);
			break;
		}
		Addressee addressee = userService.addressee(id, addresseeId);
		session.setAttribute("addressee", addressee);// 今設定されているお届け先から最新のお届け先として上書き
		return addressee(id, model);
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
