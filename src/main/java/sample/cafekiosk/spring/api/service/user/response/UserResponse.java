package sample.cafekiosk.spring.api.service.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.user.User;

@Getter
@NoArgsConstructor
public class UserResponse {

	private Long id;

	private String email;

	private String password;

	private String name;

	private int age;

	private String phoneNumber;

	@Builder
	private UserResponse(Long id, String email, String password, String name, int age, String phoneNumber) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.age = age;
		this.phoneNumber = phoneNumber;
	}

	public static UserResponse of(User user) {
		return UserResponse.builder()
			.id(user.getId())
			.email(user.getEmail())
			.password(user.getPassword())
			.name(user.getName())
			.age(user.getAge())
			.phoneNumber(user.getPhoneNumber())
			.build();
	}
}
