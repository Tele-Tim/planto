package planto_project.dto;

import lombok.*;
import planto_project.model.OrderItem;
import planto_project.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class OrderCreateDto {
    private List<OrderItemDto> items;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private AddressDto shippingAddress;
    private String paymentMethod;
    private boolean paid;

    public OrderCreateDto(List<OrderItemDto> items, OrderStatus status,
                          LocalDateTime orderDate, AddressDto shippingAddress, String paymentMethod, boolean paid) {
        this.items = items;
        this.status = status;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.paid = paid;
    }
}
