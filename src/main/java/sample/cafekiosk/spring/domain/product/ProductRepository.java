package sample.cafekiosk.spring.domain.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * select *
	 * from product
	 * where selling_status in ('SELLING', 'HOLD');
	 */
	List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

	/**
	 * select *
	 * from product
	 * where selling_status in ('NUM1', 'NUM2', ...);
	 */
	List<Product> findAllByProductNumberIn(List<String> productNumbers);

	@Query(value = "select * from product order by id desc limit 1", nativeQuery = true)
	Product findLatestProduct();

}
