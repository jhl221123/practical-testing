package sample.cafekiosk.spring.api.controller.order;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;

@RestController
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/api/v1/orders")
	public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
		LocalDateTime registeredDateTime = LocalDateTime.now();
		OrderResponse response = orderService.createOrder(request.toServiceRequest(), registeredDateTime);
		return ApiResponse.ok(response);
	}
}
