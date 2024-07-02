package sample.cafekiosk.spring.domain.stock;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StockTest {

	@Test
	@DisplayName("요청된 수량보다 남아있는 재고가 작은지 확인한다.")
	void isQuantityLessThen() {
		//given
		Stock stock = Stock.create("001", 1);
		int requestedQuantity = 2;

		//when
		boolean result = stock.isQuantityLessThan(requestedQuantity);

		//then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("요청된 수량만큼 재고가 감소된다.")
	void deductQuantity() {
		//given
		Stock stock = Stock.create("001", 2);
		int requestedQuantity = 1;

		//when
		stock.deductQuantity(requestedQuantity);

		//then
		assertThat(stock.getQuantity()).isEqualTo(1);
	}

	@Test
	@DisplayName("요청된 수량 만큼 재고를 감소할 수 없다면 예외가 발생한다.")
	void deductQuantity2() {
		//given
		Stock stock = Stock.create("001", 1);
		int requestedQuantity = 2;

		//then
		assertThatThrownBy(() -> stock.deductQuantity(requestedQuantity))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("재고 부족으로 요청된 수량만큼 재고를 줄일 수 없습니다.");
	}
}