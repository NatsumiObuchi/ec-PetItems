package jp.co.example.ecommerce_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

	@RequestMapping("")
	public String index() {
		return "myPage";
	}

}
