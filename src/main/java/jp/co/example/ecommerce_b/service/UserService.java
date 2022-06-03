package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	/**
	 * @param form ユーザーを追加
	 */
	public void insertUser(UserForm form) {
		userRepository.insertUser(form);
	}

	/**
	 * @param form メールアドレスが既に登録されているか確認
	 */
	public Boolean duplicationCheckOfEmail(UserForm form) {
		return userRepository.findByMailAddress(form);// メールアドレスが重複していればtrue
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
}
