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
    private String paymentMethod;
    private boolean paid;
    private UserAccount user;

    public Order() {
        status = OrderStatus.PENDING;
    }

    public Order(List<OrderItem> items, LocalDateTime orderDate,
                 String paymentMethod, boolean paid, UserAccount user) {
        this();
        this.items = items;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.paid = paid;
        this.user = user;
    }

    public BigDecimal calculateTotalPrice() {
        return items.stream()
                .map(i -> i.getPriceUnit().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
