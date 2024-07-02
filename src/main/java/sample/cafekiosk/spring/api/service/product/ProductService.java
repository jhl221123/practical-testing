package sample.cafekiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.controller.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public ProductResponse createProduct(ProductCreateRequest request) {
		String nextProductNumber = createNextProductNumber();
		Product product = request.toEntity(nextProductNumber);
		Product savedProduct = productRepository.save(product);
		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> getSellingProducts() {
		List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
		return products.stream()
			.map(ProductResponse::of)
			.collect(Collectors.toList());
	}

	private String createNextProductNumber() {
		Product latestProduct = productRepository.findLatestProduct();
		String latestProductNumber = (latestProduct == null) ? "000" : latestProduct.getProductNumber();
		int nextProductNumber = Integer.parseInt(latestProductNumber) + 1;
		return String.format("%03d", nextProductNumber);
	}
}
