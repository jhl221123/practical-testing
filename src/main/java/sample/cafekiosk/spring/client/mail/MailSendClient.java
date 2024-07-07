package sample.cafekiosk.spring.client.mail;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailSendClient {

	public boolean sendMail(String fromMail, String toMail, String subject, String content) {
		log.info("메일 전송");
		throw new IllegalArgumentException("메일 전송");
	}
}
