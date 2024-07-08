package sample.cafekiosk.spring.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String password;

	private String name;

	private int age;

	private String phoneNumber;

	@Builder
	private User(Long id, String email, String password, String name, int age, String phoneNumber) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.age = age;
		this.phoneNumber = phoneNumber;
	}
}
