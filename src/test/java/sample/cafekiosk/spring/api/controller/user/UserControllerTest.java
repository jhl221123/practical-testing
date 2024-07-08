package sample.cafekiosk.spring.api.controller.user;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import sample.cafekiosk.spring.api.controller.user.request.UserCreateRequest;
import sample.cafekiosk.spring.api.service.user.UserService;
import sample.cafekiosk.spring.api.service.user.request.UserCreateServiceRequest;
import sample.cafekiosk.spring.api.service.user.response.UserResponse;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("전달된 정보로 회원을 등록한다.")
	void createUser() throws Exception {
		// given
		UserCreateRequest request = createUserCreateRequest("test@email.com", "password", "name", 22, "010-1234-1234");
		String requestJson = objectMapper.writeValueAsString(request);
		UserResponse response = createUserResponse();

		doReturn(response).when(userService).create(any(UserCreateServiceRequest.class));

		// when //then
		mockMvc.perform(post("/users").contentType(APPLICATION_JSON).content(requestJson))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("회원 등록 시, 이메일은 공백일 수 없다.")
	void notCreateUserWithoutEmail() throws Exception {
		// given
		UserCreateRequest request = createUserCreateRequest("", "password", "name", 22, "010-1234-1234");
		String requestJson = objectMapper.writeValueAsString(request);
		UserResponse response = createUserResponse();

		doReturn(response).when(userService).create(any(UserCreateServiceRequest.class));

		// when //then
		mockMvc.perform(post("/users").contentType(APPLICATION_JSON).content(requestJson))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("회원 등록 시, 비밀번호는 공백일 수 없다.")
	void notCreateUserWithoutPassword() throws Exception {
		// given
		UserCreateRequest request = createUserCreateRequest("test@email.com", "", "name", 22, "010-1234-1234");
		String requestJson = objectMapper.writeValueAsString(request);
		UserResponse response = createUserResponse();

		doReturn(response).when(userService).create(any(UserCreateServiceRequest.class));

		// when //then
		mockMvc.perform(post("/users").contentType(APPLICATION_JSON).content(requestJson))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("회원 등록 시, 이름은 공백일 수 없다.")
	void notCreateUserWithoutName() throws Exception {
		// given
		UserCreateRequest request = createUserCreateRequest("test@email.com", "password", "", 22, "010-1234-1234");
		String requestJson = objectMapper.writeValueAsString(request);
		UserResponse response = createUserResponse();

		doReturn(response).when(userService).create(any(UserCreateServiceRequest.class));

		// when //then
		mockMvc.perform(post("/users").contentType(APPLICATION_JSON).content(requestJson))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("회원 등록 시, 나이는 양수여야 한다.")
	void notCreateUserWithoutAge() throws Exception {
		// given
		UserCreateRequest request = createUserCreateRequest("test@email.com", "password", "name", -1, "010-1234-1234");
		String requestJson = objectMapper.writeValueAsString(request);
		UserResponse response = createUserResponse();

		doReturn(response).when(userService).create(any(UserCreateServiceRequest.class));

		// when //then
		mockMvc.perform(post("/users").contentType(APPLICATION_JSON).content(requestJson))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("회원 등록 시, 휴대폰 번호는 공백일 수 없다.")
	void notCreateUserWithoutPhoneNumber() throws Exception {
		// given
		UserCreateRequest request = createUserCreateRequest("test@email.com", "password", "name", 22, "");
		String requestJson = objectMapper.writeValueAsString(request);
		UserResponse response = createUserResponse();

		doReturn(response).when(userService).create(any(UserCreateServiceRequest.class));

		// when //then
		mockMvc.perform(post("/users").contentType(APPLICATION_JSON).content(requestJson))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isBadRequest());
	}

	private UserCreateRequest createUserCreateRequest(String email, String password, String name, int age,
		String phoneNumber) {
		return UserCreateRequest.builder()
			.email(email)
			.password(password)
			.name(name)
			.age(age)
			.phoneNumber(phoneNumber)
			.build();
	}

	private UserResponse createUserResponse() {
		return UserResponse.builder()
			.id(1L)
			.email("test@email.com")
			.password("password")
			.name("name")
			.age(22)
			.phoneNumber("010-1234-1234")
			.build();
	}
}