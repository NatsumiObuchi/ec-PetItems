package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Point;
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

}
