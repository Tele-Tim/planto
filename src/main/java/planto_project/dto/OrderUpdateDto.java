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
public class OrderUpdateDto {
    private List<OrderItemDto> items;

    public OrderUpdateDto(List<OrderItemDto> items) {
        this.items = items;
    }
}
