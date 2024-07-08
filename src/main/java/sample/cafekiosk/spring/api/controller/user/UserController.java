package sample.cafekiosk.spring.api.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.controller.user.request.UserCreateRequest;
import sample.cafekiosk.spring.api.service.user.UserService;
import sample.cafekiosk.spring.api.service.user.response.UserResponse;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/users")
	public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
		UserResponse  response = userService.create(request.toServiceRequest());
		return ResponseEntity.ok(response);
	}
}
