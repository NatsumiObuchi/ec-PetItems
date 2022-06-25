package jp.co.example.ecommerce_b.controller;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.service.CouponServise;

@RequestMapping("/coupon")
@Controller
public class CouponController {

	@Autowired
	private CouponServise couponServise;
	
	@RequestMapping("/getCoupon")
	public String getCoupon(Integer id,Integer duration,Timestamp finishUseDate) {
		couponServise.insertUserCoupon(id,duration,finishUseDate);
		return "top";
		
	}
	
	
}
