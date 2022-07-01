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
	 * お届け先情報を新規追加する処理
	 * 
	 * @param addresee
	 */
	public void addresseeRegister(Addressee newAddressee) {
		Addressee lastAddressee = lastAddresseeId(newAddressee.getUserId());
		if (lastAddressee == null) {// 初めてお届け先情報を登録する人はaddresseeIdに1をセット
			newAddressee.setAddresseeId(1);
		} else {// それ以外の人（既に登録済のお届け先が存在する）
			Integer lastAddresseeId = lastAddressee.getAddresseeId();
			newAddressee.setAddresseeId(lastAddresseeId + 1);// 新しいaddresseeIdをセット
		}
		repository.addresseeRegister(newAddressee);
	}

	/**
	 * 最後に追加したお届け先情報を取得
	 * 
	 * @param userId
	 * @return
	 */
	public Addressee lastAddresseeId(Integer userId) {
		return repository.lastAddreseeId(userId);
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
	 * お届け先情報として設定する(setting_addresseeをtrueにする)
	 * 
	 * @param userId
	 * @param addreseeId
	 */
	public void setting(Integer userId, Integer addresseeId, boolean setting) {
		repository.settingAddrssee(userId, addresseeId, setting);
	}

	/**
	 * デフォルトで表示するお届け先を検索
	 * 
	 * @param userId
	 * @param setting
	 * @return
	 */
	public Addressee findByUserIdandSettingAddresseeTrue(Integer userId) {
		return repository.findByUserIdandSettingAddresseeTrue(userId);
	}

	/**
	 * お届け先情報の更新
	 * 
	 * @param userId
	 * @param addreseeId
	 */
	public void updateAddressee(Addressee addressee) {
		repository.updateAddressee(addressee);
	}
}
