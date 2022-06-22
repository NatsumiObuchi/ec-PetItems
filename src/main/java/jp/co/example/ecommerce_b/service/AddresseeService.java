package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Addressee;
import jp.co.example.ecommerce_b.repository.AddresseeRepository;

@Service
public class AddresseeService {

	@Autowired
	private AddresseeRepository repository;

	/**
	 * ユーザidから複数のお届け先情報を取得する
	 * 
	 * @param id
	 * @return お届け先情報
	 */
	public List<Addressee> findAddresseeByUserId(Integer id) {
		return repository.addressees(id);
	}

	/**
	 * ユーザIDとaddresseeIdでお届け先情報を検索する
	 * 
	 * @param userId
	 * @param addreseeId
	 * @return
	 */
	public Addressee addressee(Integer userId, Integer addresseeId) {
		return repository.addressee(userId, addresseeId);
	}

	/**
	 * お届け先情報を追加する
	 * 
	 * @param addresee
	 */
	public Addressee addresseeRegister(Addressee addressee) {
		return repository.addresseeRegister(addressee);
	}

	/**
	 * お届け先情報を削除する
	 * 
	 * @param userId
	 * @param addreseeId
	 */
	public void deleteAddressee(Integer userId, Integer addresseeId) {
		repository.deleteAddressee(userId, addresseeId);
	}

	/**
	 * 最後に追加したお届け先情報を取得する
	 * 
	 * @param userId
	 * @return
	 */
	public Addressee lastAddlesseeId(Integer userId) {
		return repository.lastAddreseeId(userId);
	}

	/**
	 * お届け先情報として設定する(setting_addresseeをtrueにする)
	 * 
	 * @param userId
	 * @param addreseeId
	 */
	public void setting(Integer userId, Integer addresseeId, boolean setting) {
		repository.settingAddrssee(userId, addresseeId, setting);
	}

}
