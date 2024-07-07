package sample.cafekiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@SpringBootTest
class OrderStatisticsServiceTest {

	@Autowired
	OrderStatisticsService orderStatisticsService;

	@Autowired
	OrderProductRepository orderProductRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	MailSendHistoryRepository mailSendHistoryRepository;

	@MockBean
	MailSendClient mailSendClient;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		mailSendHistoryRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
	void sendOrderStatisticsMail() {
		//given
		Product product1 = createProduct("001", "americano", 1000);
		Product product2 = createProduct("002", "latte", 2000);
		Product product3 = createProduct("003", "cake", 3000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		List<Product> targetProducts = List.of(product1, product2, product3, product3);
		Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2024, 7, 6, 23, 59, 59));
		Order order2 = createPaymentCompletedOrder(targetProducts, LocalDateTime.of(2024, 7, 7, 0, 0, 0));
		Order order3 = createPaymentCompletedOrder(targetProducts, LocalDateTime.of(2024, 7, 7, 23, 59, 59));
		Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2024, 7, 8, 0, 0, 0));
		orderRepository.saveAll(List.of(order1, order2, order3, order4));

		doReturn(true).when(mailSendClient).sendMail(anyString(), anyString(), anyString(), anyString());

		//when
		LocalDate searchDateTime = LocalDate.of(2024, 7, 7);
		boolean result = orderStatisticsService.sendOrderStatisticsMail(searchDateTime, "toMail@test.test");

		//then
		assertThat(result).isTrue();
		List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
		assertThat(histories).hasSize(1)
			.extracting("content")
			.contains("총 매출 합계는 18000원 입니다.");
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