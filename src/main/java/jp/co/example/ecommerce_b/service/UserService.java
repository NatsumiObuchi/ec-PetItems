package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Addressee;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.form.UserUpdateForm;
import jp.co.example.ecommerce_b.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	/**
	 * @param user ユーザーを追加
	 */
	public void insertUser(User user) {
		userRepository.insertUser(user);
	}

	/**
	 * @param form メールアドレスが既に登録されているか確認
	 */
	public Boolean duplicationCheckOfEmail(UserForm form) {
		return userRepository.findByMailAddress(form);// メールアドレスが重複していればtrue
	}
	
	/**
	 * @param form メールアドレスが既に登録されているか確認（ユーザ情報変更時）
	 */
	public Boolean duplicationCheckOfEmail(UserUpdateForm form) {
		return userRepository.findByMailAddress2(form);// メールアドレスが重複していればtrue
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

	/**
	 * ユーザidから複数のお届け先情報を取得する
	 * 
	 * @param id
	 * @return お届け先情報
	 */
	public List<Addressee> findAddresseeByUserId(Integer id) {
		return userRepository.addressees(id);
	}

	/**
	 * ユーザIDとaddresseeIdでお届け先情報を検索する
	 * 
	 * @param userId
	 * @param addreseeId
	 * @return
	 */
	public Addressee addressee(Integer userId, Integer addresseeId) {
		return userRepository.addressee(userId, addresseeId);
	}

	/**
	 * お届け先情報を追加する
	 * 
	 * @param addresee
	 */
	public Addressee addresseeRegister(Addressee addressee) {
		return userRepository.addresseeRegister(addressee);
	}

	/**
	 * お届け先情報を削除する
	 * 
	 * @param userId
	 * @param addreseeId
	 */
	public void deleteAddressee(Integer userId, Integer addresseeId) {
		userRepository.deleteAddressee(userId, addresseeId);
	}

	/**
	 * 最後に追加したお届け先情報を取得する
	 * 
	 * @param userId
	 * @return
	 */
	public Addressee lastAddlesseeId(Integer userId) {
		return userRepository.lastAddreseeId(userId);
	}

	/**
	 * お届け先情報として設定する(setting_addresseeをtrueにする)
	 * 
	 * @param userId
	 * @param addreseeId
	 */
	public void setting(Integer userId, Integer addresseeId, boolean setting) {
		userRepository.settingAddrssee(userId, addresseeId, setting);
	}
}
