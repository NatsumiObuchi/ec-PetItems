package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {

	@Autowired
	private MailSender sender;

	/**
	 * 注文確定用のメールを送信するメソッド
	 * 
	 * @param email
	 */
	public void sendEmail(String email) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setFrom("rakuraku.pet@gmail.com");
		mailMessage.setTo(email);
		mailMessage.setSubject("注文内容の確認");
		mailMessage.setText("" + "　---------------------------------------\n" + "　この度は、らくらくペットをご利用いただきありがとうございました。\n"
				+ "　ご注文番号「XXXX-XXXX-XXXX」で受け付けいたしました。\n" + "　本メール到着後は、商品や本サービスにおけるご注文はキャンセル・変更できません。\n"
				+ "　ご不明な点がございましたら、下記からお問い合わせください。\n" + "　連絡先：XXX-XXXX-XXXX\n"
				+ " ---------------------------------------");

		try {
			sender.send(mailMessage);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
