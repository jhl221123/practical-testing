package sample.cafekiosk.spring.domain.order;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sample.cafekiosk.spring.domain.product.Product;

class OrderTest {

	@Test
	@DisplayName("주문 생성 시 상품 목록에 있는 상품의 총 금액을 계산한다.")
	void calculateTotalPrice() {
	    //given
		List<Product> products = List.of(
			createProduct(4000),
			createProduct(3000)
		);

		LocalDateTime registeredDateTime = LocalDateTime.now();

	    //when
		Order order = Order.create(products, registeredDateTime);

	    //then
		assertThat(order.getTotalPrice()).isEqualTo(7000);
	}

	@Test
	@DisplayName("주문 생성 시 주문 상태는 INIT이다.")
	void orderStatus() {
		//given
		List<Product> products = List.of(
			createProduct(4000),
			createProduct(3000)
		);

		LocalDateTime registeredDateTime = LocalDateTime.now();

		//when
		Order order = Order.create(products, registeredDateTime);

		//then
		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.INIT);
	}

	@Test
	@DisplayName("주문 생성 시 생성 일자가 입력된다.")
	void registeredDateTime() {
		//given
		List<Product> products = List.of(
			createProduct(4000),
			createProduct(3000)
		);

		LocalDateTime registeredDateTime = LocalDateTime.now();

		//when
		Order order = Order.create(products, registeredDateTime);

		//then
		assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
	}

	private Product createProduct(int price) {
		return Product.builder()
			.productNumber("001")
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("americano")
			.price(price)
			.build();
	}
}