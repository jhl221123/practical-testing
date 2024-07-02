package sample.cafekiosk.spring.api.controller.product;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;

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
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(HANDMADE)
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
			.andExpect(status().isOk());
			// .andDo(print())
			// .andExpect(jsonPath("$.code").value("200"))
			// .andExpect(jsonPath("$.status").value(HttpStatus.OK))
			// .andExpect(jsonPath("$.message").value(OK.name()))
			// .andExpect(jsonPath("$.data").isArray());
	}

	@Test
	@DisplayName("type이 null이면 상품을 생성하지 못한다.")
	void noCreateProductWithNullType() throws Exception {
		//given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(null)
			.sellingStatus(SELLING)
			.name("americano")
			.price(4000)
			.build();
		String requestJson = objectMapper.writeValueAsString(request);

		//when //then

	}
}