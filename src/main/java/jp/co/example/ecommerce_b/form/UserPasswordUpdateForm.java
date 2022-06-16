package jp.co.example.ecommerce_b.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserPasswordUpdateForm {

	@NotBlank(message = "パスワードを入力してください")
	@Size(min = 8, max = 16, message = "パスワードは８文字以上１６文字以内で設定してください")
	String password;
	@NotBlank(message = "確認用パスワードを入力してください")
	// パスワードと確認用パスワードが一致しない場合のエラー判断はコントローラで行う。
	String confirmPassword;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "UserPasswordUpdateForm [password=" + password + ", confirmPassword=" + confirmPassword + "]";
	}
}
