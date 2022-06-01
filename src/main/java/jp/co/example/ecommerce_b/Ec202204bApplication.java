package jp.co.example.ecommerce_b;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Ec202204bApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ec202204bApplication.class, args);
	}

}
