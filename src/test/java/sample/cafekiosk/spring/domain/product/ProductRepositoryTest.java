package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

// @SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

	@Autowired
	ProductRepository productRepository;

	@Test
	@DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
	void test() {
		//given
		Product product1 = createProduct("001", SELLING, "americano");
		Product product2 = createProduct("002", HOLD, "latte");
		Product product3 = createProduct("003", STOP_SELLING, "cookie");
		productRepository.saveAll(List.of(product1, product2, product3));

		//when
		List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

		//then
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "sellingStatus")
			.containsExactlyInAnyOrder(
				tuple("001", "americano", SELLING),
				tuple("002", "latte", HOLD)
			);
	}

	@Test
	@DisplayName("선택한 상품 번호로 상품들을 조회한다.")
	void test2() {
		//given
		Product product1 = createProduct("001", SELLING, "americano");
		Product product2 = createProduct("002", HOLD, "latte");
		Product product3 = createProduct("003", STOP_SELLING, "cookie");
		productRepository.saveAll(List.of(product1, product2, product3));

		//when
		List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "001", "002"));

		//then
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "sellingStatus")
			.containsExactlyInAnyOrder(
				tuple("001", "americano", SELLING),
				tuple("002", "latte", HOLD)
			);
	}

	@Test
	@DisplayName("가장 최근 등록된 상품 1개를 조회한다.")
	void findLatestProduct() {
		//given
		Product product1 = createProduct("001", SELLING, "americano");
		Product product2 = createProduct("002", HOLD, "latte");
		Product latestProduct = createProduct("003", STOP_SELLING, "cookie");
		productRepository.saveAll(List.of(product1, product2, latestProduct));

		//when
		Product findLatestProduct = productRepository.findLatestProduct();

		//then
		assertThat(findLatestProduct)
			.extracting("productNumber", "name", "sellingStatus")
			.contains("003", "cookie", STOP_SELLING);
	}

	@Test
	@DisplayName("가장 최근 등록된 상품이 없다면 null을 반환한다.")
	void notFoundLatestProduct() {
		//given //when
		Product notExistProduct = productRepository.findLatestProduct();

		//then
		assertThat(notExistProduct).isNull();
	}

	private Product createProduct(String productNumber, ProductSellingStatus sellingStatus, String name) {
		return Product.builder()
			.productNumber(productNumber)
			.type(HANDMADE)
			.sellingStatus(sellingStatus)
			.name(name)
			.price(4000)
			.build();
	}
}