package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final StockRepository stockRepository;

	@Transactional
	public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredDateTime) {
		List<String> productNumbers = request.getProductNumbers();
		List<Product> products = findProductsBy(productNumbers);
		deductStockQuantities(products);
		Order order = Order.create(products, registeredDateTime);
		Order savedOrder = orderRepository.save(order);
		return OrderResponse.of(savedOrder);
	}

	private List<Product> findProductsBy(List<String> productNumbers) {
		// products는 중복 상품 제외
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
		Map<String, Product> productMap = products.stream()
			.collect(Collectors.toMap(Product::getProductNumber, p -> p));
		// productMap을 활용해서 각 상품들을 중복된 만큼 추가해준다.
		return productNumbers.stream()
			.map(productMap::get)
			.toList();
	}

	private void deductStockQuantities(List<Product> products) {
		List<String> stockProductNumbers = extractStockProductNumbers(products);
		Map<String, Long> stockProductCountMap = createStockProductCountMapBy(stockProductNumbers);
		List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
		for(Stock stock : stocks) {
			deductStockQuantity(stock, stockProductCountMap);
		}
	}

	private static void deductStockQuantity(Stock stock, Map<String, Long> stockProductCountMap) {
		String stockProductNumber = stock.getProductNumber();
		int requestedQuantity = stockProductCountMap.get(stockProductNumber).intValue();
		if(stock.isQuantityLessThan(requestedQuantity)) {
			throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
		}
		stock.deductQuantity(requestedQuantity);
	}

	private static List<String> extractStockProductNumbers(List<Product> products) {
		return products.stream()
			.filter(product -> ProductType.containsStockType(product.getType()))
			.map(Product::getProductNumber)
			.toList();
	}

	private static Map<String, Long> createStockProductCountMapBy(List<String> stockProductNumbers) {
		return stockProductNumbers.stream()
			.collect(Collectors.groupingBy(num -> num, Collectors.counting()));
	}
}
