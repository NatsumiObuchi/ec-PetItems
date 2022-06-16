package jp.co.example.ecommerce_b.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserUpdateForm {
	@NotBlank(message = "名前を入力してください")
	String name;
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "メールアドレスの形式が不正です")
	// 登録済みのパスワードかどうかはコントローラ側で判断する。
	String email;
	@NotBlank(message = "郵便番号を入力してください")
	@Pattern(regexp = "^[0-9]{3}[0-9]{4}$", message = "郵便番号はハイフンなしの形式で入力してください")
	String zipcode;
	@NotBlank(message = "住所を入力してください")
	String address;
	@NotBlank(message = "電話番号を入力してください")
	// 市外局番や機種（携帯・固定）によって区切りの桁数が異なるため、桁数に遊びを持たせた。
	@Pattern(regexp = "^[0-9]{2,4}-[0-9]{2,4}-[0-9]{4}$", message = "電話番号はXXXX-XXXX-XXXXの形式で入力してください")
	String telephone;

	public UserUpdateForm() {
		// TODO Auto-generated constructor stub
	}

	public UserUpdateForm(@NotBlank(message = "名前を入力してください") String name,
			@NotBlank(message = "メールアドレスを入力してください") @Email(message = "メールアドレスの形式が不正です") String email,
			@NotBlank(message = "郵便番号を入力してください") @Pattern(regexp = "^[0-9]{3}[0-9]{4}$", message = "郵便番号はハイフンなしの形式で入力してください") String zipcode,
			@NotBlank(message = "住所を入力してください") String address,
			@NotBlank(message = "電話番号を入力してください") @Pattern(regexp = "^[0-9]{2,4}-[0-9]{2,4}-[0-9]{4}$", message = "電話番号はXXXX-XXXX-XXXXの形式で入力してください") String telephone) {
		super();
		this.name = name;
		this.email = email;
		this.zipcode = zipcode;
		this.address = address;
		this.telephone = telephone;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getZipcode() {
		return zipcode;
	}

	public String getAddress() {
		return address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public String toString() {
		return "UserUpdateForm [name=" + name + ", email=" + email + ", zipcode=" + zipcode + ", address=" + address
				+ ", telephone=" + telephone + "]";
	}

}
