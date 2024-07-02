package sample.cafekiosk.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;

class CafeKioskTest {

	@Test
	void add_manual_test() {
		CafeKiosk kiosk = new CafeKiosk();
		kiosk.add(new Americano());

		System.out.println("담긴 음료수 >> " + kiosk.getBeverages().size());
		System.out.println("담긴 음료 >> " + kiosk.getBeverages().get(0).getName());
	}

	@Test
	void add() {
		CafeKiosk kiosk = new CafeKiosk();
		kiosk.add(new Americano());

		assertThat(kiosk.getBeverages()).hasSize(1);
		assertThat(kiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
	}

	@Test
	void remove() {
		CafeKiosk kiosk = new CafeKiosk();
		Americano americano = new Americano();

		kiosk.add(americano);
		assertThat(kiosk.getBeverages()).hasSize(1);

		kiosk.remove(americano);
		assertThat(kiosk.getBeverages()).isEmpty();
	}

	@Test
	void clear() {
		CafeKiosk kiosk = new CafeKiosk();
		Americano americano = new Americano();
		Latte latte = new Latte();

		kiosk.add(americano);
		kiosk.add(latte);
		assertThat(kiosk.getBeverages()).hasSize(2);

		kiosk.clear();
		assertThat(kiosk.getBeverages()).isEmpty();
	}
}