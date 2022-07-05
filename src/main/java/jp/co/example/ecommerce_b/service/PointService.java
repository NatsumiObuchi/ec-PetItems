package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Point;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.domain.UsersPointHistory;
import jp.co.example.ecommerce_b.repository.PointRepository;

@Service
public class PointService {

	@Autowired
	private PointRepository repository;

	/**
	 * ユーザのポイント情報を取得する
	 * 
	 * @param userId
	 * @return
	 */
	public Point load(Integer userId) {
		return repository.load(userId);
	}

	/**
	 * ユーザ情報を元にポイント情報を登録する
	 * 
	 * @param point
	 */
	public void insertPoint(User user) {
		Point point = new Point();
		point.setUserId(user.getId());
		point.setPoint(0);
		repository.insertPoint(point);
	}

	/**
	 * ユーザーがポイント使用時に登録される
	 * 
	 * @param usersPointHistory
	 */
	public void insertPointHistory(UsersPointHistory usersPointHistory) {
		repository.insertPointHistory(usersPointHistory);
	}

	/**
	 * ユーザのポイント情報を更新する
	 * 
	 * @param point
	 */
	public void update(Point point) {
		repository.update(point);
	}

}
