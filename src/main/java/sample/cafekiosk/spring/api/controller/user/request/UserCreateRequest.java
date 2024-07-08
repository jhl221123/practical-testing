package sample.cafekiosk.spring.api.controller.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.service.user.request.UserCreateServiceRequest;

@Getter
@RequiredArgsConstructor
public class UserCreateRequest {

	@NotBlank
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String name;

	@Positive
	private int age;

	@NotBlank
	private String phoneNumber;

	@Builder
	private UserCreateRequest(String email, String password, String name, int age, String phoneNumber) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.age = age;
		this.phoneNumber = phoneNumber;
	}

	public UserCreateServiceRequest toServiceRequest() {
		return UserCreateServiceRequest.builder()
			.email(email)
			.password(password)
			.name(name)
			.age(age)
			.phoneNumber(phoneNumber)
			.build();
	}
}
