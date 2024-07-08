package sample.cafekiosk.spring.api.service.user;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.service.user.request.UserCreateServiceRequest;
import sample.cafekiosk.spring.api.service.user.response.UserResponse;
import sample.cafekiosk.spring.domain.user.User;
import sample.cafekiosk.spring.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public UserResponse create(UserCreateServiceRequest request) {
		User user = request.toEntity();
		User savedUser =  userRepository.save(user);
		return UserResponse.of(savedUser);
	}
}