package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.form.UserUpdateForm;
import jp.co.example.ecommerce_b.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	/**
	 * formに入力されたユーザー情報を登録する
	 * 
	 * @param user ユーザーを追加
	 */
	public User insertUser(User user) {// ハッシュ化もここで行う
		String oldPass = user.getPassword();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashPass = passwordEncoder.encode(oldPass);
		user.setPassword(hashPass);
		User user2 = userRepository.insertUser(user);
		return user2;
	}


	/**
	 * @param form メールアドレスが既に登録されているか確認
	 */
	public Boolean duplicationCheckOfEmail(UserForm form) {
		return userRepository.findByMailAddress(form);// メールアドレスが重複していればtrue
	}
	
	/**
	 * 同一ユーザのメールアドレスであれば変更しなくても良い
	 * 
	 * @param form メールアドレスが既に登録されているか確認（ユーザ情報変更時）
	 */
	public Boolean duplicationCheckOfEmail(UserUpdateForm form, Integer userId) {
		User user = userRepository.load(userId);
		String oldEmail = user.getEmail();
		String newEmail = form.getEmail();
		if (oldEmail.equals(newEmail)) {// これまでとメールアドレスに変更がなければそのままでもOK
			return false;
		} else {
			return userRepository.findByMailAddress(form);// メールアドレスが重複していればtrue
		}
	}

	/**
	 * @param form
	 * @return 入力されたメールアドレスとパスワードからユーザーを検索する。
	 */
	public User loginCheck(UserForm form) {
		return userRepository.findByEmailAndPassword(form);
	}

	/**
	 * @param form
	 * @return 入力されたメールアドレスからユーザーを検索する。(パスワード一致確認のため)
	 */
	public User findByEmail(UserForm form) {
		return userRepository.findByEmail(form);
	}

	/**
	 * ユーザ情報を更新する
	 * 
	 * @param user
	 */
	public void update(User user) {
		userRepository.update(user);
	}

	/**
	 * パスワードを変更する
	 * 
	 * @param user
	 */
	public void updatePassword(User user) {
		userRepository.updatePassword(user);
	}

	/**
	 * ユーザ情報を削除する
	 * 
	 * @param id
	 */
	public void delete(Integer id) {
		userRepository.delete(id);
	}
}
