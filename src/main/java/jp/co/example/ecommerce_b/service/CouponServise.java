package jp.co.example.ecommerce_b.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.ecommerce_b.domain.Coupon;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.domain.UsersCoupon;
import jp.co.example.ecommerce_b.domain.UsersCouponHistory;
import jp.co.example.ecommerce_b.repository.CouponRepository;

@Transactional
@Service
public class CouponServise {

	@Autowired
	private CouponRepository couponRepository;

	/**
	 * 取得可能な全クーポンの一覧を取得する
	 * @return
	 */
	public List<Coupon> findAllCoupon() {
		return couponRepository.findAllCoupon();
	}
	
	/**
	 * ユーザーがクーポンを取得したとき、DBにクーポン情報をインサートする。
	 * @param couponId
	 * @param duration
	 * @param finishUseDate
	 */
	public void insertUserCoupon(User user,Integer couponId, Integer duration, Timestamp finishUseDate) {
		UsersCoupon usersCoupon = new UsersCoupon();
		usersCoupon.setUserId(user.getId());
		System.out.println("USERID　：　" + user.getId());

		usersCoupon.setCouponId(couponId);

		Timestamp nowDate = new Timestamp(System.currentTimeMillis());
		usersCoupon.setCouponGetDate(nowDate);
		System.out.println("取得日 ：" + nowDate);

		System.out.println("クーポン自体の終了日：　" + finishUseDate);

		// Timestamp型をDate型へ変換
		Date dt = new Date(nowDate.getTime());
		Calendar cal = Calendar.getInstance();
		// 有効期間の日付を取得日に加算
		cal.setTime(dt);
		cal.add(Calendar.DATE, duration);
		// Calender型をDate型へ変換
		Date findt = cal.getTime();
		// Date型をTimestamp型へ変換
		Timestamp finishDate = new Timestamp(findt.getTime());
		System.out.println("取得日＋有効期間 ：" + finishDate);

		if (finishUseDate.after(finishDate)) {
			System.out.println("クーポンは" + finishDate + "まで使えます。");
			usersCoupon.setCouponExpirationDate(finishDate);
		} else {
			System.out.println("クーポンは" + finishUseDate + "まで使えます。");
			usersCoupon.setCouponExpirationDate(finishUseDate);
		}
		System.out.println(usersCoupon);
		couponRepository.insertUsersCoupon(usersCoupon);
	}

	
	/**
	 * ユーザーが使用可能な全クーポンの一覧を取得する
	 * @return
	 */
	public List<UsersCoupon> findAllUsersCoupon(Integer userId){
		return couponRepository.findAllUsersCoupon(userId);
	}
	
	
	/**
	 * 使用したクーポンのdeletedをtrueにする
	 * @param id ユーザーが保持しているクーポンの一意のid
	 */
	public void usedUsersCoupon(Integer id) {
		List<UsersCoupon> usersCouponList = couponRepository.findAllUsersCoupon(id);
		for (UsersCoupon usersCoupon : usersCouponList) {
			Timestamp couponExpirationDate = usersCoupon.getCouponExpirationDate();
			Timestamp nowDate = new Timestamp(System.currentTimeMillis());
			if (nowDate.after(couponExpirationDate)) {
				couponRepository.usedUsersCoupon(usersCoupon.getId());
			}
		}
	}
	
	
	/**
	 *  ユーザーが購入後、クーポンの使用履歴をインサート
	 * @param usersCouponHistory 
	 */
	public void insertUsersCouponHistorys(UsersCouponHistory usersCouponHistory) {
		couponRepository.insertUsersCouponHistorys(usersCouponHistory);
	}
	
}
