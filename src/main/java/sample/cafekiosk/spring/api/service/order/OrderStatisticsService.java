package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

@Service
@RequiredArgsConstructor
public class OrderStatisticsService {

	private final OrderRepository orderRepository;
	private final MailService mailService;

	public boolean sendOrderStatisticsMail(LocalDate orderDate, String toEmail) {
		List<Order> orders = orderRepository.findOrderBy(
			orderDate.atStartOfDay(),
			orderDate.plusDays(1).atStartOfDay(),
			OrderStatus.PAYMENT_COMPLETED
		);

		int totalAmount = orders.stream()
			.mapToInt(Order::getTotalPrice)
			.sum();

		boolean isSuccess = mailService.sendMail(
			"no-reply@cafekiosk.com",
			toEmail,
			String.format("[매출통계] %s", orderDate),
			String.format("총 매출 합계는 %s원 입니다.", totalAmount)
		);

		if(!isSuccess) {
			throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
		}
		return true;
	}
}
