package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTypeTest {

	@Test
	@DisplayName("재고와 관련이 있는 상품 타입인지 확인한다.")
	void containsStockType() {
	    //given
		ProductType type = ProductType.BOTTLE;

	    //when
		boolean result = ProductType.containsStockType(type);

		//then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("재고와 관련이 없는 상품 타입인지 확인한다.")
	void notContainsStockType() {
		//given
		ProductType type = ProductType.HANDMADE;

		//when
		boolean result = ProductType.containsStockType(type);

		//then
		assertThat(result).isFalse();
	}
}