package pl.lawit.web.dto;

import lombok.Data;
import pl.lawit.web.controller.OrderStatus;

import java.util.List;

@Data
public class OrderNotificationDto {
    private OrderDto order;
    private String localReceiptDateTime;

    @Data
    public static class OrderDto {
        private String orderId;
        private String extOrderId;
        private String orderCreateDate;
        private String notifyUrl;
        private String customerIp;
        private String merchantPosId;
        private String description;
        private String currencyCode;
        private int totalAmount;
        private Buyer buyer;
        private PayMethod payMethod;
        private List<Product> products;
        private OrderStatus status;  // Use the enum

        @Data
        public static class Buyer {
            private String email;
            private String phone;
            private String firstName;
            private String lastName;
            private String language;
        }

        @Data
        public static class PayMethod {
            private String type;
        }

        @Data
        public static class Product {
            private String name;
            private int unitPrice;
            private int quantity;
        }
    }

    @Data
    public static class Property {
        private String name;
        private String value;
    }
}