package sample.cafekiosk.spring.domain.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productNumber;

	private int quantity;

	@Builder
	private Stock(String productNumber, int quantity) {
		this.productNumber = productNumber;
		this.quantity = quantity;
	}

	public static Stock create(String productNumber, int quantity) {
		return Stock.builder()
			.productNumber(productNumber)
			.quantity(quantity)
			.build();
	}

	public boolean isQuantityLessThan(int requestedQuantity) {
		return this.quantity < requestedQuantity;
	}

	public void deductQuantity(int requestedQuantity) {
		if(isQuantityLessThan(requestedQuantity)) {
			throw new IllegalArgumentException("재고 부족으로 요청된 수량만큼 재고를 줄일 수 없습니다.");
		}
		this.quantity -= requestedQuantity;
	}
}
