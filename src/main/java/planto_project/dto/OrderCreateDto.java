package planto_project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String paymentMethod;
    private boolean paid;
    private UserDto user;
    private String deliveryAddress;
    private String deliveryMethod;

    public OrderCreateDto(List<OrderItemDto> items, OrderStatus status, LocalDateTime orderDate, String paymentMethod, boolean paid, UserDto user, String deliveryAddress, String deliveryMethod) {
        this.items = items;
        this.status = status;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.paid = paid;
        this.user = user;
        this.deliveryAddress = deliveryAddress;
        this.deliveryMethod = deliveryMethod;
    }
}
