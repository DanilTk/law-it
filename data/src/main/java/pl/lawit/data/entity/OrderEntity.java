package pl.lawit.data.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED, force = true)
@Table(name = "order")
public class OrderEntity {
    @Id
    private String id;
    private String orderId;
    private String extOrderId;
    private LocalDateTime orderCreateDate;
    private String notifyUrl;
    private String customerIp;
    private String merchantPosId;
    private String description;
    private String currencyCode;
    private int totalAmount;
    @Embedded
    private Buyer buyer;
    @Embedded
    private PayMethod payMethod;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")  // Foreign key reference
    private List<Product> products;
    private String status;
    private LocalDateTime localReceiptDateTime;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")  // Foreign key reference
    private List<Property> properties;


    @Embeddable
    @Data
    public static class Buyer {
        private String email;
        private String phone;
        private String firstName;
        private String lastName;
        private String language;
    }
    @Embeddable
    @Data
    public static class PayMethod {
        private String type;
    }
    @Entity
    @Data
    public static class Product {
        private String name;
        private int unitPrice;
        private int quantity;
        @Id
        private Long id;

    }

    @Entity
    @Data
    public static class Property {
        private String name;
        private String value;
        @Id
        private Long id;

    }
}
