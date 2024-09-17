package pl.lawit.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lawit.web.dto.OrderNotificationDto;
import pl.lawit.web.handler.OrderHandler;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController implements BaseController  {

    private final OrderHandler orderHandler;

    @PostMapping("/notify")
    public ResponseEntity<String> handleOrderNotification(@RequestBody OrderNotificationDto orderNotification) {

        orderHandler.saveOrder(orderNotification);

        return new ResponseEntity<>("Notification received and processed.", HttpStatus.OK);
    }

}
