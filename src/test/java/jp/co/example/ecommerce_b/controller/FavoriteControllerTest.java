package jp.co.example.ecommerce_b.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import jp.co.example.ecommerce_b.domain.Favorite;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.service.FavoriteService;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FavoriteService service;

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
		// userをsessionにセットしなければ if (user == null) を通るので以下は不要
//		MockHttpSession mockSession = new MockHttpSession();
//		User user = null;
//		mockSession.setAttribute("user", null);
		MvcResult result = mockMvc.perform(get("/favorite/favoriteList"))// .session(mockSession))これも不要
				.andExpect(view().name("forward:/user/toLogin3")).andReturn();
		HttpSession session = result.getRequest().getSession();
		assertEquals("favoriteList", session.getAttribute("transitionSourcePage"));
	}
	
	@Test
	@DisplayName("お気に入り登録がない場合の遷移先とスコープの値を確認")
	void testNonFavorite() throws Exception {
		User user = new User();
		MockHttpSession mockHttpSession = new MockHttpSession();
		mockHttpSession.setAttribute("user", user);// ユーザーがログインしている前提のテストのためMockにsessionをセット
//		when(service.favoriteAll(anyInt())).thenReturn(null);
		MvcResult result = mockMvc.perform(get("/favorite/favoriteList").session(mockHttpSession))
				.andExpect(status().isOk()).andExpect(view().name("favorite_list")).andReturn();
		ModelAndView mav = result.getModelAndView();
		String message = (String) mav.getModel().get("message");
		System.out.println("message = " + message);
		System.out.println("mav = " + mav);
		assertEquals("お気に入り登録はありません", message);
	}

	@Test
	@DisplayName("お気に入り登録済がある場合の遷移先とスコープの値を確認")
	void testFavoriteList() throws Exception {
		User user = new User();
		List<Favorite> favoriteList = new ArrayList<Favorite>();
		MockHttpSession mockHttpSession = new MockHttpSession();
		mockHttpSession.setAttribute("user", user);// ユーザーがログインしている前提のテストのためMockにsessionをセット
		when(service.favoriteAll(anyInt())).thenReturn(favoriteList);
		MvcResult result = mockMvc.perform(get("/favorite/favoriteList").param("itemId", "1")
				.session(mockHttpSession))
				.andExpect(status().isOk()).andExpect(view().name("favorite_list")).andReturn();
		ModelAndView mav = result.getModelAndView();
		String message = (String) mav.getModel().get("message");
		System.out.println("message = " + message);
		System.out.println("mav = " + mav);
		assertEquals(null, message);
	}

	@Test
	@DisplayName("お気に入り登録する処理の遷移先とスコープ内の値を確認")
	void testInsertFavorite() throws Exception {
		MvcResult result =
		mockMvc.perform(get("/favorite/insert").param("itemId", "1").param("date", "date"))
				.andExpect(view().name("forward:/user/toLogin3")).andReturn();
		HttpSession session = result.getRequest().getSession();
		assertEquals("favoriteList", session.getAttribute("transitionSourcePage"));
	}
}
