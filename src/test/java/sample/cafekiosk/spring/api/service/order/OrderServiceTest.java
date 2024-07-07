package sample.cafekiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

	@Autowired
	OrderService orderService;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderProductRepository orderProductRepository;
	@Autowired
	StockRepository stockRepository;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("상품번호를 받아 주문을 생성한다.")
	void order() {
	    //given
		Product product1 = createProduct(HANDMADE, "001", 4000);
		Product product2 = createProduct(HANDMADE, "002", 4500);
		productRepository.saveAll(List.of(product1, product2));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "002"))
			.build();

		LocalDateTime registeredDateTime = LocalDateTime.now();

		//when
		OrderResponse response = orderService.createOrder(request, registeredDateTime);

		//then
		assertThat(response.getId()).isNotNull();
		assertThat(response)
			.extracting("registeredDateTime", "totalPrice")
			.contains(registeredDateTime, 8500);
		assertThat(response.getProducts()).hasSize(2)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 4000),
				tuple("002", 4500)
			);
	}

	@Test
	@DisplayName("중복되는 상품번호 목록으로 주문을 생성할 수 있다.")
	void createOrderWithDuplicatedProductNumbers() {
		//given
		Product product = createProduct(HANDMADE, "001", 4000);
		productRepository.save(product);

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "001"))
			.build();

		LocalDateTime registeredDateTime = LocalDateTime.now();

		//when
		OrderResponse response = orderService.createOrder(request, registeredDateTime);

		//then
		assertThat(response.getId()).isNotNull();
		assertThat(response)
			.extracting("registeredDateTime", "totalPrice")
			.contains(registeredDateTime, 8000);
		assertThat(response.getProducts()).hasSize(2)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 4000),
				tuple("001", 4000)
			);
	}

	@Test
	@DisplayName("재고와 관련된 상품의 번호를 받아 주문을 생성한다.")
	void orderWithStockProduct() {
		//given
		Product product1 = createProduct(BOTTLE, "001", 4000);
		Product product2 = createProduct(BAKERY, "002", 3500);
		Product product3 = createProduct(HANDMADE, "003", 4500);
		productRepository.saveAll(List.of(product1, product2, product3));

		Stock stock1 = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 2);
		stockRepository.saveAll(List.of(stock1, stock2));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "001", "002", "003"))
			.build();

		LocalDateTime registeredDateTime = LocalDateTime.now();

		//when
		OrderResponse response = orderService.createOrder(request, registeredDateTime);

		//then
		assertThat(response.getId()).isNotNull();
		assertThat(response)
			.extracting("registeredDateTime", "totalPrice")
			.contains(registeredDateTime, 16000);
		assertThat(response.getProducts()).hasSize(4)
			.extracting("productNumber", "price")
			.containsExactlyInAnyOrder(
				tuple("001", 4000),
				tuple("001", 4000),
				tuple("002", 3500),
				tuple("003", 4500)
			);
		List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of("001", "002"));
		assertThat(stocks).hasSize(2);
		assertThat(stocks).extracting("productNumber", "quantity")
			.containsExactlyInAnyOrder(
				tuple("001", 0),
				tuple("002", 1)
			);
	}

	@Test
	@DisplayName("재고가 부족한 상품으로 주문할 경우, 예외가 발생한다.")
	void noOrderWithStockProduct() {
		//given
		Product product1 = createProduct(BOTTLE, "001", 4000);
		Product product2 = createProduct(BAKERY, "002", 3500);
		Product product3 = createProduct(HANDMADE, "003", 4500);
		productRepository.saveAll(List.of(product1, product2, product3));

		Stock stock1 = Stock.create("001", 1);
		Stock stock2 = Stock.create("002", 1);
		stockRepository.saveAll(List.of(stock1, stock2));

		OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
			.productNumbers(List.of("001", "001", "002", "003"))
			.build();

		LocalDateTime registeredDateTime = LocalDateTime.now();

		//when //then
		assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("재고가 부족한 상품이 있습니다.");
	}

	private Product createProduct(ProductType productType, String productNumber, int price) {
		return Product.builder()
			.productNumber(productNumber)
			.type(productType)
			.sellingStatus(SELLING)
			.name("americano")
			.price(price)
			.build();
	}
}