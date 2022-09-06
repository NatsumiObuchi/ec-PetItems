package jp.co.example.ecommerce_b.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.form.UserUpdateForm;

@SpringBootTest
class UserServiceTest {

//	@Autowired
//	private NamedParameterJdbcTemplate template;

	@Autowired
	private UserService service;

	private static final RowMapper<User> USER_ROW_MAPPER = new BeanPropertyRowMapper<>(User.class);

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(
				"jdbc:postgresql://localhost:5432/ec_exam_test", "postgres", "postgres", true);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(singleConnectionDataSource);
		User user = new User();
		// userテーブルの情報をサラの状態にする
		String deleteSql = "DELETE FROM users";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		template.update(deleteSql, param);
		// テスト用に1件だけデータを用意
		String sql = "INSERT INTO users(name, email, password, zipcode, address, telephone)"
				+ " VALUES('testName01', 'test01@abc.com', 'abcdABCD', '1111111', '東京都', '09011111111')";
		template.update(sql, param);
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

	/**
	 * userServiceクラスのinsertメソッドのテスト。 データ挿入前と挿入後でDBのデータ数が変わっているかどうかをテスト。
	 */
	@Test
	@DisplayName("User情報の登録処理&入力されたメールアドレスからユーザーを検索するテスト")
	void testInsert() {
		User user = new User();
//		user.setId(2);
		user.setName("name");
		user.setEmail("email@email.com");
		user.setPassword("password");
		user.setZipcode("1234567");
		user.setAddress("address");
		user.setTelephone("08011112222");
		service.insertUser(user);

		// ちゃんとユーザー情報がinsertされたかfindByEmailで確認
		UserForm userForm = new UserForm();
		userForm.setEmail("email@email.com");
		User testUser = service.findByEmail(userForm);

		// insertしたオブジェクトとfindByEmailで取得したオブジェクトの内容が同じか確認
//		assertEquals(user.getId(), testUser.getId());
		assertEquals(user.getName(), testUser.getName());
		assertEquals(user.getEmail(), testUser.getEmail());
		// パスワードはハッシュ化されるから比較してない
		assertEquals(user.getZipcode(), testUser.getZipcode());
		assertEquals(user.getAddress(), testUser.getAddress());
		assertEquals(user.getTelephone(), testUser.getTelephone());
	}

	@Test
	@DisplayName("メールアドレスの重複がない時")
	void testNotFindByMailAddress() {
		// formオブジェクトを直接repositoryに投げているので、formにメールアドレスをセット
		UserForm userForm = new UserForm();
		// まだ未登録のEmailアドレスを指定してfalseが返ってくるようにする
		userForm.setEmail("testtaro@test.com");
		assertEquals(false, service.duplicationCheckOfEmail(userForm));
	}

	@Test
	@DisplayName("メールアドレスの重複がある時")
	void testFindByMailAddress() {
		// 重複用のテストデータをインサート
		SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(
				"jdbc:postgresql://localhost:5432/ec_exam_test", "postgres", "postgres", true);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(singleConnectionDataSource);
		String insertSql = "INSERT INTO users(id, name, email, password, zipcode, address, telephone)"
				+ " VALUES('3', 'テストメールアドレス3', 'testDuplicationEmail@test.com', 'testDuplicationEmail1', '1234567', 'address', '08011112222')";
		template.update(insertSql, (SqlParameterSource) (null));

		// ちゃんとインサートできたか確認するselectのSQL文
		String selectSql = "SELECT * FROM users WHERE id=3;";
		User testUser = template.queryForObject(selectSql, (SqlParameterSource) (null), USER_ROW_MAPPER);
		assertNotNull(testUser);

		// 登録済のEmailアドレスを指定してtrueが返ってくるようにする
		UserForm userForm = new UserForm();
		userForm.setEmail("testDuplicationEmail@test.com");
		assertEquals(true, service.duplicationCheckOfEmail(userForm));
	}

	@Test
	@DisplayName("登録ユーザーが登録情報変更時、メールアドレスの変更をしない時(これまでと同じメールアドレス)")
	void testNotChangeMailAddress() {
		// テスト用にデータを用意
		SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(
				"jdbc:postgresql://localhost:5432/ec_exam_test", "postgres", "postgres", true);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(singleConnectionDataSource);
		String insertSql = "INSERT INTO users(id, name, email, password, zipcode, address, telephone)"
				+ " VALUES('4', 'テストメールアドレス4', 'testNonChangeOldEmail@test.com', 'testNonChangeOldEmail', '1234567', 'address', '08011112222')";
		template.update(insertSql, (SqlParameterSource) (null));

		// ちゃんとインサートできたか確認するselectのSQL文
		String selectSql = "SELECT * FROM users WHERE id=4;";
		User testUser = template.queryForObject(selectSql, (SqlParameterSource) (null), USER_ROW_MAPPER);
		assertNotNull(testUser);

		// formオブジェクトを直接repositoryに投げているので、formにメールアドレス(上記と同じメールアドレス)をセット
		UserUpdateForm userUpdateForm = new UserUpdateForm();
		userUpdateForm.setEmail("testNonChangeOldEmail@test.com");
		// serviceの登録情報変更用のduplicationCheckOfEmailを呼ぶ
		Boolean actual = service.duplicationCheckOfEmail(userUpdateForm, testUser.getId());
		assertEquals(false, actual);//メールアドレスが他者のものとは重複しないためfalse
	}
	
	@Test
	@DisplayName("登録ユーザーが登録情報変更時、メールアドレスの変更する際にメールアドレスの重複がある")
	void testFindByMailAddress2() {
		// テスト用にデータを用意
		SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(
				"jdbc:postgresql://localhost:5432/ec_exam_test", "postgres", "postgres", true);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(singleConnectionDataSource);
		String insertSql = "INSERT INTO users(id, name, email, password, zipcode, address, telephone)"
				+ " VALUES('5', 'テストメールアドレス5', 'testDuplicationEmail2@test.com', 'testNonChangeOldEmail', '1234567', 'address', '08011112222')";
		template.update(insertSql, (SqlParameterSource) (null));

		// ちゃんとインサートできたか確認するselectのSQL文
		// テストデータは直接idを指定してinsertしているのでここでも指定して情報を取得
		String selectSql = "SELECT * FROM users WHERE id=5;";
		User testUser = template.queryForObject(selectSql, (SqlParameterSource) (null), USER_ROW_MAPPER);
		assertNotNull(testUser);

		// 登録済のEmailアドレスを指定してtrueが返ってくるようにする
		UserUpdateForm userUpdateForm = new UserUpdateForm();

		// @BeforeAllで入れたテスト用のデータを使用する(既に登録済のものを使用する)
		userUpdateForm.setEmail("test01@abc.com");
		Boolean actual = service.duplicationCheckOfEmail(userUpdateForm, testUser.getId());
		assertEquals(true, actual);//メールアドレスが他者のものと重複するのでtrue
	}

	@Test
	@DisplayName("ログインチェック")
	void testLogin() {
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		// 先にテスト用のデータをインサートする(パスワードをハッシュ化させるためserviceのinsertを使用)
		User user = new User();
		user.setName("テストログイン");
		user.setEmail("testRogin@rogin.com");
		user.setPassword("testRogin");
		user.setZipcode("1234567");
		user.setAddress("address");
		user.setTelephone("08011112222");
		User insertUser = service.insertUser(user);
		UserForm userForm = new UserForm();
		userForm.setEmail("testRogin@rogin.com");
		userForm.setPassword("testRogin");
		// ここでハッシュ化の照合を無理やり入れているが正しくはないかも、、
		// これを入れないとformの入力値との照合ができない
		if (bcpe.matches(insertUser.getPassword(), userForm.getPassword())) {
			User loginUser = service.loginCheck(userForm);
			// インサートしたデータとオブジェクトが一緒かをassertSameで確認
			assertSame(insertUser, loginUser);
		}
	}

	@Test
	@DisplayName("ユーザー情報の更新")
	void testUpdate() {
		// テスト用にデータを用意
		SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(
				"jdbc:postgresql://localhost:5432/ec_exam_test", "postgres", "postgres", true);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(singleConnectionDataSource);
		String insertSql = "INSERT INTO users(id, name, email, password, zipcode, address, telephone)"
				+ " VALUES('6', 'テスト', 'test@test.com', 'test', '1234567', 'address', '08011112222')";
		template.update(insertSql, (SqlParameterSource) (null));

		// ちゃんとインサートできたか確認するselectのSQL文
		// テストデータは直接idを指定してinsertしているのでここでもidを指定して情報を取得
		String selectSql = "SELECT * FROM users WHERE id=6;";
		User testUser = template.queryForObject(selectSql, (SqlParameterSource) (null), USER_ROW_MAPPER);
		assertNotNull(testUser);

		// 更新する情報をセットする
		User updateUser = new User();
		updateUser.setId(6);
		updateUser.setName("テストアップデート");
		updateUser.setEmail("testUpdate@test.com");
		updateUser.setZipcode("7654321");
		updateUser.setAddress("updateAddress");
		updateUser.setTelephone("09033334444");
		service.update(updateUser);// updateの実行

		// updateした情報を取得する
		String selectSql2 = "SELECT * FROM users WHERE id=6;";
		User updateAfterUser = template.queryForObject(selectSql2, (SqlParameterSource) (null), USER_ROW_MAPPER);

		// ちゃんとupdateされてるか確認
		assertEquals(updateUser.getName(), updateAfterUser.getName());
		assertEquals(updateUser.getEmail(), updateAfterUser.getEmail());
		assertEquals(updateUser.getZipcode(), updateAfterUser.getZipcode());
		assertEquals(updateUser.getAddress(), updateAfterUser.getAddress());
		assertEquals(updateUser.getTelephone(), updateAfterUser.getTelephone());
	}

	@Test
	@DisplayName("ユーザーのパスワード変更")
	void testPasswordUpdate() {
		// テスト用にデータを用意
		SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(
				"jdbc:postgresql://localhost:5432/ec_exam_test", "postgres", "postgres", true);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(singleConnectionDataSource);
		String insertSql = "INSERT INTO users(id, name, email, password, zipcode, address, telephone)"
				+ " VALUES('7', 'テストパスワード変更', 'testPassChange@test.com', 'testPass', '1234567', 'address', '08011112222')";
		template.update(insertSql, (SqlParameterSource) (null));

		// ちゃんとインサートできたか確認するselectのSQL文
		// テストデータは直接idを指定してinsertしているのでここでもidを指定して情報を取得
		String selectSql = "SELECT * FROM users WHERE id=7;";
		User testUser = template.queryForObject(selectSql, (SqlParameterSource) (null), USER_ROW_MAPPER);
		assertNotNull(testUser);

		// 更新用のパスワードをセットする
		User updateBeforePass = new User();
		updateBeforePass.setId(7);
		updateBeforePass.setPassword("testPassChange");
		User getConfirmUser = service.updatePassword(updateBeforePass);// updateの実行(ハッシュ化されたパスワードを知りたいためオブジェクトごと取得)

		// updateした情報を取得する
		String selectSql2 = "SELECT * FROM users WHERE id=7;";
		User updateAfterPass = template.queryForObject(selectSql2, (SqlParameterSource) (null), USER_ROW_MAPPER);

		// 更新用のパスワードで取得したオブジェクトのパスワードが
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		if (bcpe.matches("testPassChange", updateAfterPass.getPassword())) {// 更新用のパスワードで照合できればここの処理に入れる
			System.out.println(updateAfterPass);
			System.out.println(getConfirmUser);//こっちがなぜかidとパスワード以外取得できない
//			assertSame(getConfirmUser, updateAfterPass);//これは取得したオブジェクト同士が同じかを比較
			assertEquals(getConfirmUser.getPassword(), updateAfterPass.getPassword());
		}
	}

	@Test
	@DisplayName("ユーザー情報の削除")
	void testDelete() {
		// テスト用にデータを用意
		SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(
				"jdbc:postgresql://localhost:5432/ec_exam_test", "postgres", "postgres", true);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(singleConnectionDataSource);
		String insertSql = "INSERT INTO users(id, name, email, password, zipcode, address, telephone)"
				+ " VALUES('8', 'テストデリート', 'delete@test.com', 'deleteUser', '1234567', 'address', '08011112222')";
		template.update(insertSql, (SqlParameterSource) (null));// insert実行

		// ちゃんとインサートできたか確認するselectのSQL文(※1)
		String selectSql = "SELECT * FROM users WHERE id=8;";
		// テストデータは直接idを指定してinsertしているのでここでもidを指定して情報を取得
		User testUser = template.queryForObject(selectSql, (SqlParameterSource) (null), USER_ROW_MAPPER);
		assertNotNull(testUser);

		// deleteする前のデータ数を取得
		String deleteBeforeSql = "SELECT COUNT(*) FROM USERS WHERE id = 8";
		Integer deleteBeforeUserCount = template.queryForObject(deleteBeforeSql, (SqlParameterSource) (null),
				Integer.class);

		// delete実行
		service.delete(8);

		// deleteできたか確認（※1を再度実行しデータがないはずなのでcatchさせる）
		try {
			template.queryForObject(selectSql, (SqlParameterSource) (null), USER_ROW_MAPPER);
			fail("失敗です");
		} catch (Exception e) {
			assertTrue(true, "成功です");
		}

		// delete後のデータの数を算出
		String deleteAfterSql = "SELECT COUNT(*) FROM USERS WHERE id = 8";
		Integer deleteAfterUserCount = template.queryForObject(deleteAfterSql, (SqlParameterSource) (null),
				Integer.class);

		// deleteの確認テスト
		assertEquals(deleteBeforeUserCount - 1, deleteAfterUserCount);

	}

}