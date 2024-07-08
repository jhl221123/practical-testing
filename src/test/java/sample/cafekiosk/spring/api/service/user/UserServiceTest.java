package sample.cafekiosk.spring.api.service.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sample.cafekiosk.spring.api.service.user.request.UserCreateServiceRequest;
import sample.cafekiosk.spring.api.service.user.response.UserResponse;

@SpringBootTest
class UserServiceTest {

	@Autowired
	UserService userService;

	@Test
	@DisplayName("전달받은 정보로 회원을 동록한다.")
	void create() {
		// given
		UserCreateServiceRequest request = createUserCreateServiceRequest();

		// when
		UserResponse response = userService.create(request);

		//then
		assertThat(response)
			.extracting("email", "password", "name", "age", "phoneNumber")
			.containsExactly("test@email.com", "password", "name", 22, "010-1234-1234");
	}

	private static UserCreateServiceRequest createUserCreateServiceRequest() {
		return UserCreateServiceRequest.builder()
			.email("test@email.com")
			.password("password")
			.name("name")
			.age(22)
			.phoneNumber("010-1234-1234")
			.build();
	}
}