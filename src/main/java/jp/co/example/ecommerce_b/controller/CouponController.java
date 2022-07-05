package jp.co.example.ecommerce_b.controller;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.service.CouponServise;

@RequestMapping("/coupon")
@Controller
public class CouponController {
	
	@Autowired
	private HttpSession session;	

	@Autowired
	private CouponServise couponServise;
	
	@RequestMapping("/getCoupon")
	public String getCoupon(Integer id,Integer duration,Timestamp finishUseDate,Integer couponLink) {
		User user = (User) session.getAttribute("user");
		couponServise.insertUserCoupon(user,id,duration,finishUseDate);
		if(couponLink==0) {
			return "forward:/item/top";			
		}else {
			return "forward:/item/list";			
		}
		
	}
	
	
}
