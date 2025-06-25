package planto_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import planto_project.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private String id;
    private List<OrderItemDto> items;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private String paymentMethod;
    private boolean paid;
    private UserDto user;
}

