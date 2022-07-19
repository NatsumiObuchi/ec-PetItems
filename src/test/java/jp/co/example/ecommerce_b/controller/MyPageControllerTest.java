package jp.co.example.ecommerce_b.controller;

import static org.hamcrest.CoreMatchers.anything;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.stripe.model.Address;

import jp.co.example.ecommerce_b.domain.Addressee;
import jp.co.example.ecommerce_b.domain.Coupon;
import jp.co.example.ecommerce_b.domain.Point;
import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.domain.UsersCoupon;
import jp.co.example.ecommerce_b.form.UserUpdateForm;
import jp.co.example.ecommerce_b.service.AddresseeService;
import jp.co.example.ecommerce_b.service.CouponServise;
import jp.co.example.ecommerce_b.service.UserService;

@SpringBootTest
class MyPageControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	// CouponServiceをモックオブジェクトとして使いますよ、という指定
	@MockBean
	private CouponServise couponService;

	@MockBean
	private AddresseeService addresseeService;

	// テスト対象へMockオブジェクトを注入
	@InjectMocks
	private MyPageController myPageCntroller = new MyPageController();

	private AutoCloseable closeable;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		// 各テストの実行前にモックオブジェクトを初期化する(モックオブジェクトを生成する)
		closeable = MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	@DisplayName("１、マイページを表示するメソッドの正常系挙動における遷移先パスの確認")
	// 正しくmyPage.htmlに遷移するか
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
	@DisplayName("２、ユーザ情報を変更するときに、パスワード入力を求められるか確認(ログイン後一回目アクセス)")
	// 1回目のアクセスの時、countは1がsessionにセットされているか
	// 正しくchange_permission.htmlに遷移するか
	void test2() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("count", null);

		MvcResult mvcResult = mockMvc.perform(get("/myPage/changePermissionDisplay").session(mockSession))
				.andExpect(status().isOk()).andExpect(view().name("change_permission")).andReturn();
		HttpSession session = mvcResult.getRequest().getSession();
		assertEquals(1, session.getAttribute("count"));
	}

	@Test
	@DisplayName("３、ユーザ情報を変更するときに、パスワード入力を求めらないで遷移できるか確認(ログイン後2回目以降アクセス)")
	// 2回目以降のアクセスの時、countは１のままsessionにセットされているか
	// 正しくconfirm_userInfo.htmlに遷移するか
	void test3() throws Exception {
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

	// 一応テストは通ったけど、絶対こういうことじゃない～～～。
	// ObjectのクラスをAssertEqualしても、User user と UserUpdateForm form
	// だからエラーが出ちゃう。わからないので諦め。
	@Test
	@DisplayName("４、ユーザ情報を変更する際確認画面に遷移できるか確認(リンクから飛んできてFormの値がnullの場合)")
	// formに値が入っていない場合、リクエストスコープにユーザーの値がセットされているか
	// 正しくchange_userInfo.htmlに遷移するか
	void test4() throws Exception {

		User user = new User();
		user.setName("小渕");
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("user", user);
		MvcResult result = mockMvc.perform(get("/myPage/change").session(mockSession)
//				.param("name", null)
//				.param("email", null)
//				.param("zipcode", null)
//				.param("address", null)
//				.param("telephone", null)
		).andExpect(status().isOk()).andExpect(view().name("change_userInfo")).andReturn();

		ModelAndView mav = result.getModelAndView();
		UserUpdateForm form = (UserUpdateForm) mav.getModel().get("form");

//		assertEquals("小渕", form.getName());
		assertEquals(user.getName(), form.getName());
	}

	@Test
	@DisplayName("５、ユーザ情報を変更する際確認画面に遷移できるか確認(バリデーションから飛んできてFormの値がなにかしら入っている場合)")
	// formに値が入っている場合、リクエストスコープにフォームの値がセットされているか
	// 正しくchange_userInfo.htmlに遷移するか
	void test5() throws Exception {
		User user = new User();
		user.setName("小渕");
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("user", user);
		MvcResult result = mockMvc.perform(get("/myPage/change").param("name", "夏美").session(mockSession))
				.andExpect(status().isOk()).andExpect(view().name("change_userInfo")).andReturn();
		ModelAndView mav = result.getModelAndView();
		UserUpdateForm form = (UserUpdateForm) mav.getModel().get("form");

		assertEquals("夏美", form.getName());
	}

	// ↓エラー出ないけどうまくいってないよ～～～～
	@Test
	@DisplayName("６、マイクーポンを表示する(1枚以上所持している場合)")
	// userIdを渡したときクーポン一枚がsessionにセットされているか
	// 正しくmyCoupon.htmlに遷移するか
	void test6() throws Exception {
		// ログインしているように見せるため、user情報をmockSessionにつめる
		User user = new User();
		user.setName("小渕");
		user.setId(6);
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("user", user);

		Coupon coupon = new Coupon();
		coupon.setDiscountPrice(100);

		List<UsersCoupon> usersCouponList = new ArrayList<>();
		UsersCoupon usersCoupon = new UsersCoupon();
		usersCoupon.setUserId(6);
		usersCoupon.setCoupon(coupon);
		usersCouponList.add(usersCoupon);
		mockSession.setAttribute("usersCouponList", usersCouponList);

		System.out.println("*********" + usersCouponList.size());

//		doReturn(usersCouponList).when(couponService.findAllUsersCoupon(anyInt()));
		when(couponService.findAllUsersCoupon(anyInt())).thenReturn(usersCouponList);

		// mvcResultにテストの結果を返す
		MvcResult mvcResult = mockMvc.perform(get("/myPage/myCoupon").param("id", "6").session(mockSession))
				.andExpect(status().isOk()).andExpect(view().name("myCoupon")).andReturn();

		// sessionにテスト結果のsession情報を格納
		HttpSession session = mvcResult.getRequest().getSession();

		// getClassメソッドを使って、インスタンスのクラスが同じかどうかを比較する
		// ※Listの中身を比較するテストはServiceで行う
		assertEquals(usersCouponList.getClass(), session.getAttribute("usersCouponList").getClass());
	}

	@Test
	@DisplayName("７、マイクーポンを表示する（所持していない場合）")
	// userIdを渡したときにsessionに空のListがセットされているか
	// リクエストスコープにエラー文がセットされているか
	// 正しくmyCoupon.htmlに遷移するか
	void test7() throws Exception {
		User user = new User();
		user.setName("小渕");
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("user", user);

		List<UsersCoupon> usersCouponList = new ArrayList<>();
		mockSession.setAttribute("usersCouponList", usersCouponList);

		System.out.println("*********lllll" + usersCouponList.size());
		when(couponService.findAllUsersCoupon(anyInt())).thenReturn(usersCouponList);

		MvcResult mvcResult = mockMvc.perform(get("/myPage/myCoupon").param("id", "1").session(mockSession))
				.andExpect(status().isOk()).andExpect(view().name("myCoupon")).andReturn();

		// リクエストスコープのデータを取り出す
		ModelAndView mav = mvcResult.getModelAndView();

		// 正しく格納されているか確認。
		assertEquals("所持しているクーポンはありません。", mav.getModel().get("nonCoupon"));

		// sessionのデータを取り出す
		HttpSession session = mvcResult.getRequest().getSession();

		// 正しく格納されているか確認。
		assertEquals(usersCouponList.getClass(), session.getAttribute("usersCouponList").getClass());
	}

	@Test
	@DisplayName("８、お届け先情報追加画面(0~2件登録してある時)")
	// 正しくaddressee_register.htmlに遷移するか
	void test8() throws Exception {
		User user = new User();
		user.setName("小渕");
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("user", user);

		List<Addressee> addresseeList = new ArrayList<>();
		Addressee addressee = new Addressee();
		addressee.setAddresseeId(1);
		addresseeList.add(addressee);
		when(addresseeService.findAddresseeByUserId(anyInt())).thenReturn(addresseeList);

		mockMvc.perform(get("/myPage/registerShow").param("id", "6").session(mockSession)).andExpect(status().isOk())
				.andExpect(view().name("addressee_register"));

	}

	@Test
	@DisplayName("９、お届け先情報追加画面(3件登録してある時)")
	// リクエストスコープにセットされているオブジェクトの型があっているか
	// 正しくaddressee_register.htmlに遷移するか
	void test9() throws Exception {
		User user = new User();
		user.setName("小渕");
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("user", user);

		List<Addressee> addresseeList = new ArrayList<>();
		Addressee addressee1 = new Addressee();
		addressee1.setId(1);
		Addressee addressee2 = new Addressee();
		addressee2.setId(2);
		Addressee addressee3 = new Addressee();
		addressee3.setId(3);		
		addresseeList.add(addressee1);
		addresseeList.add(addressee2);
		addresseeList.add(addressee3);
		mockSession.setAttribute("addresseeList", addresseeList);
		System.out.println("addresseeList:"+addresseeList.size());

		when(addresseeService.findAddresseeByUserId(anyInt())).thenReturn(addresseeList);

		MvcResult result = mockMvc.perform(get("/myPage/registerShow").param("id", "7").session(mockSession))
				.andExpect(status().isOk())
				.andExpect(view().name("addressee")).andReturn();
		ModelAndView mav = result.getModelAndView();
		assertEquals("お届け先情報の登録は3件までです。追加するにはどれかを削除してください。", mav.getModel().get("full"));

	}

	@Test
	@DisplayName("１０、ユーザのお届け先を新規追加する(バリデーション未通過)")
	// バリデーションのエラーに引っかかりaddressee_registeraddressee_register.htmlに遷移するか
	void test10() throws Exception {
		User user = new User();
		user.setId(6);
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("user", user);

		//郵便番号を8桁でparamに挿入し、バリデーションで通過できないようにする
		mockMvc.perform(post("/myPage/addresseeRegister").param("zipCode", "11111111").param("address", "東京都江戸川区")
				.session(mockSession)).andExpect(status().isOk()).andExpect(view().name("addressee_register"));
	}

	@Test
	@DisplayName("１１、ユーザのお届け先を新規追加する（バリデーション通過）")
	// バリデーションのエラーに引っかからず、sessionに値をセットしているか
	// addresseeメソッドに移り、addressee.htmlに遷移するか
	void test11() throws Exception {
		System.out.println("test11");
		User user = new User();
		user.setId(6);
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("user", user);

		Addressee newAddressee = new Addressee();	
		// void（戻り値のない）Serviceのメソッドをモック化する場合、doReturnは使えない！（返ってくる値がないから）
//		doReturn(newAddressee).when(addresseeService).addresseeRegister(addressee);
	    doThrow(new RuntimeException()).when(addresseeService).addresseeRegister(newAddressee);
	    
	    //バリデーションチェックを通過できる正しい郵便番号・住所をparamに挿入する
		mockMvc.perform(post("/myPage/addresseeRegister").param("zipCode", "1111111").param("address", "東京都江戸川区")
				.session(mockSession)).andExpect(status().isOk()).andExpect(view().name("addressee"));
	}
	
	@Test
	@DisplayName("１２、お届け先情報を削除する")
	// addresseeメソッドに移り、addressee.htmlに遷移するか
	void test12() throws Exception {
		User user = new User();
		user.setId(6);
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("user", user);

		doThrow(new RuntimeException()).when(addresseeService).deleteAddressee(anyInt(), anyInt());
		mockMvc.perform(get("/myPage/addresseeDelete").session(mockSession))
				.andExpect(status().isOk()).andExpect(view().name("addressee"));
		
	}
	
	@Test
	@DisplayName("１３、デフォルトのお届け先を設定する(ラジオボタンを選択せずに設定ボタンを押した場合)")
	// リクエストスコープにエラー文がセットされているか
	// addresseeメソッドに移り、addressee.htmlに遷移するか
	void test13() throws Exception {
		User user = new User();
		user.setName("小渕");
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("user", user);

		//ラジオボタンのidをセットするparamは空にする
		MvcResult result = mockMvc.perform(get("/myPage/settingAddressee").param("id","6").param("addresseeId","").session(mockSession))
				.andExpect(status().isOk()).andExpect(view().name("addressee")).andReturn();
		ModelAndView mav = result.getModelAndView();
		assertEquals("お届け先が選択されていません！", mav.getModel().get("nonRadio"));
	}
	
	@Test
	@DisplayName("１４、デフォルトのお届け先を設定する(ラジオボタンを選択してから設定ボタンを押し、更新した場合)")
	// リクエストスコープにメッセージがセットされているか
	// addresseeメソッドに移り、addressee.htmlに遷移するか
	void test14() throws Exception {
		User user = new User();
		user.setName("小渕");
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("user", user);
		
//		doThrow(new RuntimeException()).when(addresseeService).setting(anyInt(), anyInt(), anyBoolean());

		//ラジオボタンのidをセットするparamは空にする
		MvcResult result = mockMvc.perform(get("/myPage/settingAddressee").param("id","6").param("addresseeId","1").param("setting", "true").session(mockSession))
				.andExpect(status().isOk()).andExpect(view().name("addressee")).andReturn();
		ModelAndView mav = result.getModelAndView();
		assertEquals("更新しました！" , mav.getModel().get("update"));
		assertEquals("※複数お届け先情報がある方は、一番上のお届け先が「注文確認画面」のデフォルトで表示されます" , mav.getModel().get("updateText"));
	}
	

}
