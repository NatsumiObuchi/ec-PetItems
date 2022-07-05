package jp.co.example.ecommerce_b.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import jp.co.example.ecommerce_b.domain.User;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MockMvc mvc;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("ログインせずにお気に入りリストに遷移した場合の遷移先とスコープの値を確認")
	void testNonLoginFavorite() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		User user = null;
		mockSession.setAttribute("user", user);
		MvcResult result = mvc.perform(get("/favoriteList").session(mockSession))
				.andExpect(view().name("forward:/user/toLogin3")).andReturn();
		HttpSession session = result.getRequest().getSession();
		assertEquals("favoriteList", session.getAttribute("transitionSourcePage"));
	}

}
