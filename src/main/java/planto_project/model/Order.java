package planto_project.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private List<OrderItem> items;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private Address shippingAddress;
    private String paymentMethod;
    private boolean paid;

    public Order() {
        status = OrderStatus.PENDING;
    }

    public Order(List<OrderItem> items, LocalDateTime orderDate,
                 Address shippingAddress, String paymentMethod, boolean paid) {
        this();
        this.items = items;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.paid = paid;
    }

    public BigDecimal calculateTotalPrice() {
        return items.stream()
                .map(i -> i.getPriceUnit().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
