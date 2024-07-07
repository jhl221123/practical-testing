package sample.cafekiosk.spring.domain.order;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@DataJpaTest
class OrderRepositoryTest {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	OrderRepository orderRepository;

	@Test
	@DisplayName("기간과 주문 상태를 만족하는 주문들을 조회한다.")
	void findOrderByLocalDateTimeAndOrderStatus() {
		//given
		Product product1 = createProduct("001", "americano", 1000);
		Product product2 = createProduct("002", "latte", 2000);
		Product product3 = createProduct("003", "cookie", 3000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		List<Product> targetProducts = List.of(product1, product2, product3, product3);
		Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2024, 7, 6, 23, 59, 59));
		Order order2 = createPaymentCompletedOrder(targetProducts, LocalDateTime.of(2024, 7, 7, 0, 0, 0));
		Order order3 = createPaymentCompletedOrder(targetProducts, LocalDateTime.of(2024, 7, 7, 23, 59, 59));
		Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2024, 7, 8, 0, 0, 0));
		orderRepository.saveAll(List.of(order1, order2, order3, order4));

		//when
		LocalDate searchDateTime = LocalDate.of(2024, 7, 7);
		List<Order> orders = orderRepository.findOrderBy(
			searchDateTime.atStartOfDay(),
			searchDateTime.plusDays(1).atStartOfDay(),
			OrderStatus.PAYMENT_COMPLETED
		);

		//then
		assertThat(orders).hasSize(2)
			.extracting("totalPrice")
			.containsExactlyInAnyOrder(9000, 9000);
	}

	@Test
	@DisplayName("기간과 주문 상태를 만족하는 주문이 없다면 비어있는 목록이 반환된다.")
	void notFoundOrderWithNotSameLocalDateTimeAndOrderStatus() {
		//given
		Product product1 = createProduct("001", "americano", 1000);
		Product product2 = createProduct("002", "latte", 2000);
		Product product3 = createProduct("003", "cookie", 3000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2024, 7, 6, 23, 59, 59));
		Order order2 = createPaymentCompletedOrder(products, LocalDateTime.of(2024, 7, 8, 0, 0, 0));
		orderRepository.saveAll(List.of(order1, order2));

		//when
		LocalDate searchDateTime = LocalDate.of(2024, 7, 7);
		List<Order> orders = orderRepository.findOrderBy(
			searchDateTime.atStartOfDay(),
			searchDateTime.plusDays(1).atStartOfDay(),
			OrderStatus.PAYMENT_COMPLETED
		);

		//then
		assertThat(orders).hasSize(0);
	}

	private Order createPaymentCompletedOrder(List<Product> products, LocalDateTime registerdDateTime1) {
		return Order.builder()
			.orderStatus(OrderStatus.PAYMENT_COMPLETED)
			.products(products)
			.registeredDateTime(registerdDateTime1)
			.build();
	}

	private Product createProduct(String productNumber, String name, int price) {
		return Product.builder()
			.productNumber(productNumber)
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name(name)
			.price(price)
			.build();
	}
}