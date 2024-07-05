package sample.cafekiosk.spring.api.controller.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

	@MockBean
	private OrderService orderService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("신규 주문을 등록한다.")
	void createOrder() throws Exception {
		//given
		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001", "002"))
			.build();
		String requestJson = objectMapper.writeValueAsString(request);

		//when //then
		mockMvc.perform(
				post("/api/v1/orders")
					.contentType(APPLICATION_JSON)
					.content(requestJson)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.message").value(OK.name()));
		verify(orderService, times(1)).createOrder(any(OrderCreateServiceRequest.class), any(LocalDateTime.class));
	}

	@Test
	@DisplayName("주문할 때, 상품 번호는 1개 이상이어야 한다.")
	void createOrderWithoutProductNumber() throws Exception {
		//given
		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of())
			.build();
		String requestJson = objectMapper.writeValueAsString(request);

		//when //then
		mockMvc.perform(
				post("/api/v1/orders")
					.contentType(APPLICATION_JSON)
					.content(requestJson)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 번호 목록은 필수입니다."));
	}
}