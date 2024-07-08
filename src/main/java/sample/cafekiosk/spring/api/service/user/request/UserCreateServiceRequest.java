package sample.cafekiosk.spring.api.service.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.user.User;

@Getter
@NoArgsConstructor
public class UserCreateServiceRequest {

	private String email;

	private String password;

	private String name;

	private int age;

	private String phoneNumber;

	@Builder
	private UserCreateServiceRequest(String email, String password, String name, int age, String phoneNumber) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.age = age;
		this.phoneNumber = phoneNumber;
	}

	public User toEntity() {
		return User.builder()
			.email(this.getEmail())
			.password(this.getPassword())
			.name(this.getName())
			.age(this.getAge())
			.phoneNumber(this.getPhoneNumber())
			.build();
	}
}
