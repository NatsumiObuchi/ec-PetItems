package jp.co.example.ecommerce_b.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Information;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.InformationForm;
import jp.co.example.ecommerce_b.service.InformationService;

@Controller
@RequestMapping("/info")
public class InformationController {
	
	@Autowired
	private InformationService service;
	
	@Autowired
	private HttpSession session;
	
	@ModelAttribute
	private InformationForm setUpInformation() {
		return new InformationForm();
	}
	
	@RequestMapping("")
	public String index() {
		return "information";
	}
	
	@RequestMapping("/insertInfo")
	public String insertInfo(@Validated InformationForm informationForm,BindingResult result) {
		if(result.hasErrors()) {
			return index();
		}
		Information information=new Information();
		BeanUtils.copyProperties(informationForm, information);
		if (session.getAttribute("user") != null) {
			User user=(User) session.getAttribute("user");
			information.setUserId(user.getId());
		}
		service.insertInfo(information);
		return "information_end";
	}

}
