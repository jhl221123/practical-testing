package sample.cafekiosk.spring.domain.stock;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import sample.cafekiosk.spring.domain.product.ProductRepository;

@DataJpaTest
class StockRepositoryTest {

	@Autowired
	ProductRepository productRepository;
	@Autowired
	StockRepository stockRepository;

	@Test
	@DisplayName("선택한 상품 번호로 재고들을 조회한다.")
	void findAllBy() {
		//given
		Stock stock1 = Stock.create("001", 2);
		Stock stock2 = Stock.create("002", 1);
		Stock stock3 = Stock.create("003", 5);
		stockRepository.saveAll(List.of(stock1, stock2, stock3));

		//when
		List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of("001", "001", "002"));

		//then
		assertThat(stocks).hasSize(2)
			.extracting("productNumber", "quantity")
			.containsExactlyInAnyOrder(
				tuple("001", 2),
				tuple("002", 1)
			);
	}
}