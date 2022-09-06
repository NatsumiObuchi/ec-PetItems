package jp.co.example.ecommerce_b.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import jp.co.example.ecommerce_b.domain.Addressee;
import jp.co.example.ecommerce_b.repository.AddresseeRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class AddresseeServiceTest {

	private static final RowMapper<Addressee> ADDRESSEE_ROW_MAPPER = new BeanPropertyRowMapper<>(Addressee.class);

	@Autowired
	private AddresseeRepository addresseeRepository;

	@Autowired
	private AddresseeService addresseeService;

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
	@DisplayName("お届け先情報を新規登録する処理（インサート）")
	// addresseeRegisterできちんとインサート出来ることは確認できたが、Service内のif分岐の処理はできていない。paramで引数に入れる・・・？
	void test1() throws Exception {

		Addressee addressee = new Addressee();
		addressee.setUserId(1000);
		addressee.setZipCode("1111111");
		addressee.setAddress("東京都テスト区");
		addressee.setAddresseeId(1);

		addresseeRepository.addresseeRegister(addressee);

		List<Addressee> addList = addresseeRepository.addressees(1000);
		System.out.println("111" + addList);

		for (Addressee address : addList) {
			assertEquals(address.getAddress(), "東京都テスト区");
		}

	}

	@Test
	@DisplayName("デフォルトで表示するお届け先を検索")
	// テストするごとにユーザーidを変えないとどんどんListが積み重なってしまう。要修正。
	void test2() throws Exception {
		Addressee address1 = new Addressee();
		address1.setUserId(1002);
		address1.setZipCode("1111111");
		address1.setAddress("東京都テスト区");
		address1.setAddresseeId(1);

		addresseeRepository.addresseeRegister(address1);

		addresseeRepository.settingAddrssee(1002, 1, true);

//		List<Addressee> addList = addresseeService.findAddresseeByUserId(1002);
//
//		System.out.println("0" + addList);
//		for (Addressee address : addList) {
//			System.out.println("1" + address);
//		}

		Addressee address = addresseeRepository.findByUserIdandSettingAddresseeTrue(1002);
		System.out.println("2" + address);

		assertEquals("1111111", address.getZipCode());
		assertEquals("東京都テスト区", address.getAddress());

	}

}