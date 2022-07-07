package jp.co.example.ecommerce_b.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import jp.co.example.ecommerce_b.domain.User;

@SpringBootTest
class UserServiceTest {

	@Autowired
	private NamedParameterJdbcTemplate template;

	@Autowired
	private UserService service;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * 
	 * テスト用にDBにデータを挿入
	 * 
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		User user = new User();
		// userテーブルの情報をサラの状態にする
		String deleteSql = "DELETE FROM users";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		template.update(deleteSql, param);
		// テスト用に1件だけデータを用意
		String sql = "INSERT INTO USERS(name, email, password, zipcode, address, telephone)"
				+ " VALUES('testName01', 'test01@abc.com', 'abcdABCD', '1111111', '東京都', '09011111111')";
		template.update(sql, param);
	}

	/**
	 * 上記データの削除
	 * 
	 * @throws Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		User user = new User();
		String deleteSql = "DELETE FROM users";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		template.update(deleteSql, param);
	}

	/**
	 * userServiceクラスのinsertメソッドのテスト。 データ挿入前と挿入後でDBのデータ数が変わっているかどうかをテスト。
	 */
	@Test
	void test() {
		User user = new User();
		user.setName("name");
		user.setEmail("email@email.com");
		user.setPassword("password");
		user.setZipcode("1234567");
		user.setAddress("address");
		user.setTelephone("08011112222");
		service.insertUser(user);
		Integer actual = template.queryForObject("SELECT COUNT(*) FROM users", (SqlParameterSource) (null),
				Integer.class);
		assertEquals(2, actual, "正しくありません。");
	}

}
