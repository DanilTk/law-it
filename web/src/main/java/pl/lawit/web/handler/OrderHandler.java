package pl.lawit.web.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.lawit.domain.service.OrderService;
import pl.lawit.web.controller.OrderStatus;
import pl.lawit.web.dto.OrderNotificationDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderHandler {

    private final OrderService orderService;

    public boolean saveOrder (OrderNotificationDto orderNotificationDto) {
        OrderStatus status = orderNotificationDto.getOrder().getStatus();

        switch (status) {
            case COMPLETED:
                handleOrderCompleted(orderNotificationDto);
                break;
            case CANCELED:
                handleOrderCanceled(orderNotificationDto);
                break;
            default:
                handleOtherStatuses(orderNotificationDto);
        }
        return true;
    }


    private void handleOrderCompleted(OrderNotificationDto orderNotification) {
        log.info("Processing completed order: {}", orderNotification.getOrder().getOrderId());
    }

    private void handleOrderCanceled(OrderNotificationDto orderNotification) {
        log.info("Processing canceled order: {}" , orderNotification.getOrder().getOrderId());
    }

    private void handleOtherStatuses(OrderNotificationDto orderNotification) {
        log.info("Processing other status: {}", orderNotification.getOrder().getStatus());
    }

}
