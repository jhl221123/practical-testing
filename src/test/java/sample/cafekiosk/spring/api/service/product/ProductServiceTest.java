package sample.cafekiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.controller.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	ProductRepository productRepository;

	@AfterEach
	void tearDown() {
		productRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("신규 상품을 등록한다. 이때, 상품 번호는 가장 최근 등록된 상품 번호 + 1 이다.")
	void createProduct() {
		//given
		Product product1 = createProduct("001", SELLING, "americano", 4000);
		Product product2 = createProduct("002", HOLD, "latte", 4500);
		Product latestProduct = createProduct("003", STOP_SELLING, "cookie", 3500);
		productRepository.saveAll(List.of(product1, product2, latestProduct));

		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("cake")
			.price(7000)
			.build();

		//when
		ProductResponse response = productService.createProduct(request);

		//then
		assertThat(response)
			.extracting("productNumber", "sellingStatus", "name", "price")
			.contains("004", SELLING, "cake", 7000);

		List<Product> products = productRepository.findAll();
		assertThat(products).hasSize(4)
			.extracting("productNumber", "sellingStatus", "name", "price")
			.containsExactlyInAnyOrder(
				tuple("001", SELLING, "americano", 4000),
				tuple("002", HOLD, "latte", 4500),
				tuple("003", STOP_SELLING, "cookie", 3500),
				tuple("004", SELLING, "cake", 7000)
			);
	}

	@Test
	@DisplayName("신규 상품을 등록한다. 현재 등록된 상품이 없다면, 상품 번호는 001 이다.")
	void createFirstProduct() {
		//given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("cake")
			.price(7000)
			.build();

		//when
		ProductResponse response = productService.createProduct(request);

		//then
		assertThat(response)
			.extracting("productNumber", "sellingStatus", "name", "price")
			.contains("001", SELLING, "cake", 7000);
	}

	private Product createProduct(String productNumber, ProductSellingStatus sellingStatus, String name, int price) {
		return Product.builder()
			.productNumber(productNumber)
			.type(HANDMADE)
			.sellingStatus(sellingStatus)
			.name(name)
			.price(price)
			.build();
	}
}