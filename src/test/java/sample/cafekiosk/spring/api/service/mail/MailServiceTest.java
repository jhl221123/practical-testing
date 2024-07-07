package sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

	@InjectMocks
	MailService mailService;

	@Mock
	MailSendClient mailSendClient;

	@Mock
	MailSendHistoryRepository mailSendHistoryRepository;

	@Test
	@DisplayName("메일 전송에 성공한다.")
	void test() {
	    //given
		// BDDMockito.doReturn(true).when(mailSendClient).sendMail(anyString(), anyString(), anyString(), anyString());
		doReturn(true).when(mailSendClient).sendMail(anyString(), anyString(), anyString(), anyString());

	    //when
		boolean result = mailService.sendMail("fromEmail", "toEmail", "subject", "content");

		//then
		assertThat(result).isTrue();
		verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
	}

}