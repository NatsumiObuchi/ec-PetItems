package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.ecommerce_b.domain.Coupon;
import jp.co.example.ecommerce_b.repository.CouponRepository;

@Transactional
@Service
public class CouponServise {

	@Autowired
	private CouponRepository couponRepository;
	
	public List<Coupon> findAll(){
		return couponRepository.findAll();
	}
	
}
