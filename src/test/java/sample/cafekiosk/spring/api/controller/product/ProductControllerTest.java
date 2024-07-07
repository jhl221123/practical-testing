package sample.cafekiosk.spring.api.controller.product;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	ProductService productService;

	@Test
	@DisplayName("신규 상품을 생성하면 http 상태코드 200을 응답받는다.")
	void createProduct() throws Exception {
		//given
		ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("americano")
			.price(4000)
			.build();
		String requestJson = objectMapper.writeValueAsString(request);

		ProductResponse response = ProductResponse.builder()
			.productNumber("001")
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("americano")
			.price(4000)
			.build();
		doReturn(response).when(productService).createProduct(any(ProductCreateServiceRequest.class));

		//when //then
		mockMvc.perform(
				post("/api/v1/products")
					.contentType(APPLICATION_JSON)
					.content(requestJson)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.message").value(OK.name()))
			.andExpect(jsonPath("$.data").isMap());
	}

	@Test
	@DisplayName("신규 상품을 등록할 때, 상품 타입은 필수값이다.")
	void createProductWithoutType() throws Exception {
		//given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(null)
			.sellingStatus(SELLING)
			.name("americano")
			.price(4000)
			.build();
		String requestJson = objectMapper.writeValueAsString(request);

		//when //then
		mockMvc.perform(
				post("/api/v1/products")
					.contentType(APPLICATION_JSON)
					.content(requestJson)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("신규 상품을 등록할 때, 상품 판매 상태는 필수값이다.")
	void createProductWithoutSellingStatus() throws Exception {
		//given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingStatus(null)
			.name("americano")
			.price(4000)
			.build();
		String requestJson = objectMapper.writeValueAsString(request);

		//when //then
		mockMvc.perform(
				post("/api/v1/products")
					.contentType(APPLICATION_JSON)
					.content(requestJson)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("신규 상품을 등록할 때, 상품 이름은 비어있거나 공백일 수 없다.")
	void createProductWithoutName() throws Exception {
		//given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name(" ")
			.price(4000)
			.build();
		String requestJson = objectMapper.writeValueAsString(request);

		//when //then
		mockMvc.perform(
				post("/api/v1/products")
					.contentType(APPLICATION_JSON)
					.content(requestJson)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("신규 상품을 등록할 때, 상품 가격은 양수여야만 한다.")
	void createProductWithNegativePrice() throws Exception {
		//given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("americano")
			.price(-1)
			.build();
		String requestJson = objectMapper.writeValueAsString(request);

		//when //then
		mockMvc.perform(
				post("/api/v1/products")
					.contentType(APPLICATION_JSON)
					.content(requestJson)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 가격은 음수일 수 없습니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("판매 중인 상품을 조회한다.")
	void getSellingProduct() throws Exception {
		//given
		List<ProductResponse> response = List.of();
		doReturn(response).when(productService).getSellingProducts();

		//when //then
		mockMvc.perform(
				get("/api/v1/products/selling")
					.contentType(APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.message").value(OK.name()))
			.andExpect(jsonPath("$.data").isArray());
	}
}