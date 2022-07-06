package jp.co.example.ecommerce_b.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpSession;
import javax.validation.Validator;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.repository.UserRepository;
import jp.co.example.ecommerce_b.service.PointService;
import jp.co.example.ecommerce_b.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Validator validator;

	@MockBean
	private UserService userService;

	@MockBean
	private PointService pointService;

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
	@DisplayName("ユーザ登録画面へ遷移するだけのテスト")
	void testDisplaySignin() throws Exception {
		mockMvc.perform(get("/user/signin")).andExpect(status().isOk()).andExpect(view().name("register_user"));
	}

	@Test
	@DisplayName("ユーザー情報の登録画面バリデーションテスト")
	void testValidation() throws Exception {
		// mockMvcを使って、実際のリクエストをエミュレートする。
		// formにセットされるのはリクエストパラメーターなので、パラメーターをセットする。
		// 遷移先のファイル名が合っているかどうかをチェック。
		// 今回は電話番号をパラメーターにセットしないことでバリデーションを走らせる
		mockMvc.perform(post("/user/signin").param("name", "TestTest").param("address", "テスト住所")
				.param("email", "test@test.com").param("password", "test1234").param("zipcode", "1240011"))
				.andExpect(status().isOk()).andExpect(view().name("register_user"));
	}

//	@Test
//	@DisplayName("メールアドレス重複時のスコープの中身と遷移先チェック")
//	void testMailAddress() throws Exception {
//		String email = "yksumvcl77@gmail.com";
//		User user = new User();
//		user.setEmail(email);
//		user.setPassword("aaaaaaaa");
//		// Controllerのinsertメソッドを呼び出して、結果を返す。メールアドレスで重複エラーが出てる前提。
//		when(userService.duplicationCheckOfEmail(userForm)).thenReturn(true);
//		MvcResult result = mockMvc
//				.perform(post("/user/signin").param("name", "工藤陽介").param("email", "yksumvcl77@gmail.com")
//						.param("password", "test123").param("confirmPassword", "test123").param("zipcode", "1240011")
//						.param("address", "テスト住所").param("telephone", "08012345678"))
//				.andExpect(view().name("user/register_user")).andReturn();
//		// スコープのデータを取り出す
//		ModelAndView mav = result.getModelAndView();
//		String message = (String) mav.getModel().get("emailError");
//		// 正しく格納されているか確認
//		assertEquals(message, "そのメールアドレスはすでに使われています");
//	}

	@Test
	@DisplayName("signinメソッドの正常系テスト（遷移先確認)")
	void testSignin() throws Exception {
		when(userService.duplicationCheckOfEmail(any(UserForm.class))).thenReturn(false);
//		userService.insertUser(new User());
//		pointService.insertPoint(new User());
		mockMvc.perform(post("/user/signin").param("name", "工藤陽介").param("email", "yksumvcl77@gmail.com")
				.param("password", "test1234").param("confirmPassword", "test1234").param("zipcode", "1240011")
				.param("address", "テスト住所").param("telephone", "08012345678"))
				.andExpect(view().name("redirect:/user/toLogin"));
	}

	@Test
	@DisplayName("入力値をリセットした際の遷移確認")
	void testReset() throws Exception {
		mockMvc.perform(get("/user/reset"))// .andExpect(status().isOk())HTTP302が返ってくるため入れない
				.andExpect(view().name("redirect:/user/toSignin"));
	}

	@Test
	@DisplayName("ログイン画面への遷移先確認")
	void testToRogin() throws Exception {
		mockMvc.perform(get("/user/toLogin")).andExpect(status().isOk()).andExpect(view().name("login")).andReturn();
	}

	@Test
	@DisplayName("ログインに失敗した際(メールアドレスが見つからない)のスコープの中身と遷移先の確認")
	void testLogin() throws Exception {
		// メールアドレスの検索でuserがnullを返す時をしてすればよい
		when(userService.findByEmail(any(UserForm.class))).thenReturn(null);
		MvcResult result = mockMvc
				.perform(get("/user/login"))// .param("email", "test@test.com"))上でwhen~~thenReturn(null)を返しているので不要
				.andExpect(view().name("login")).andReturn();
		ModelAndView mav = result.getModelAndView();
		String message = (String) mav.getModel().get("loginErrorMessage");
//		System.out.println(message);
		assertEquals("メールアドレス、またはパスワードが間違っています", message);
	}

	// ハッシュ化したパスワードをどう一致させるか不明。
//	@Test
//	@DisplayName("パスワードが一致してログインする際の遷移先(トップページ)とスコープの値を確認")
//	void testLoginTop() throws Exception {
//		MockHttpSession session = new MockHttpSession();
//		when(userService.findByEmail(any(UserForm.class))).thenReturn(new User());
//		MvcResult result = mockMvc.perform(get("/user/login").param(null, null));
//	}

	@Test
	@DisplayName("ログアウトした際の遷移先とスコープ内の値を確認")
	void testLogout() throws Exception {
		MvcResult result = mockMvc.perform(get("/user/logout")).andExpect(status().isOk()).andReturn();
		HttpSession session = result.getRequest().getSession();
		assertEquals(null, session.getAttribute("user"));// ユーザだけでいいのか？
	}

	@Test
	@DisplayName("ログインしてないユーザーが注文履歴を見ようとした際の遷移先とスコープの値を確認")
	void testToLogin2() throws Exception {
		MvcResult result = mockMvc.perform(get("/user/toLogin2")).andExpect(status().isOk()).andReturn();
		ModelAndView mav = result.getModelAndView();
		String message = (String) mav.getModel().get("historyMessage");
//		System.out.println(message);
		assertEquals("注文履歴のご確認にはログインもしくはユーザー登録が必要です。", message);
	}
	
	@Test
	@DisplayName("ログインしてない人ユーザーがお気に入り登録をしようとした際の遷移先とスコープの値を確認")
	void testToLogin3() throws Exception {
		MvcResult result = mockMvc.perform(get("/user/toLogin3")).andExpect(status().isOk()).andReturn();
		ModelAndView mav = result.getModelAndView();
		String message = (String) mav.getModel().get("favoriteMessage");
//		System.out.println(message);
		assertEquals("お気に入り登録にはログイン、もしくはユーザ登録が必要です。", message);
	}
}
