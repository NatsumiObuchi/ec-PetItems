package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.User;
import jp.co.example.ecommerce_b.form.UserForm;
import jp.co.example.ecommerce_b.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public void insertUser(User user) {
		userRepository.insertUser(user);
	}

	public Boolean duplicationCheckOfEmail(UserForm form) {
		return userRepository.findByMailAddress(form);// メールアドレスが重複していればtrue
	}
}
