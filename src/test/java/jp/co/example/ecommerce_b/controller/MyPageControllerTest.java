package jp.co.example.ecommerce_b.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jp.co.example.ecommerce_b.domain.Point;
import jp.co.example.ecommerce_b.domain.User;

@SpringBootTest
class MyPageControllerTest {

	@Autowired
	private WebApplicationContext  wac;
	
	private MockMvc mockMvc;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
    @DisplayName("マイページを表示するメソッドの正常系挙動における遷移先パスの確認")
	void test1() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User();
		user.setName("小渕夏美");
		Point point = new Point();
		point.setPoint(100);
		mockSession.setAttribute("user", user);
		mockSession.setAttribute("point", point);
		
		mockMvc.perform(get("/myPage").session(mockSession)).andExpect(view().name("myPage")).andReturn();
	}
	
	@Test
	@DisplayName("ユーザ情報を変更するときに、パスワード入力を求められるか確認(ログイン後一回目アクセス)")
	void test2() throws Exception{
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("count", null);
		
		MvcResult mvcResult = mockMvc.perform(get("/myPage/changePermissionDisplay").session(mockSession))
				.andExpect(status().isOk()).andExpect(view().name("change_permission")).andReturn();
		HttpSession session = mvcResult.getRequest().getSession();
        assertEquals(1, session.getAttribute("count"));
	}
	
	@Test
	@DisplayName("ユーザ情報を変更するときに、パスワード入力を求めらないで遷移できるか確認(ログイン後2回目以降アクセス)")
	void test3() throws Exception{
		MockHttpSession mockSession = new MockHttpSession();
		User user = new User();
		user.setName("小渕夏美");
		mockSession.setAttribute("user", user);
		mockSession.setAttribute("count", 1);
		
		MvcResult mvcResult = mockMvc.perform(get("/myPage/changePermissionDisplay").session(mockSession))
				.andExpect(status().isOk()).andExpect(view().name("confirm_userInfo")).andReturn();
		HttpSession session = mvcResult.getRequest().getSession();
        assertEquals(1, session.getAttribute("count"));
	}
	
	

}
