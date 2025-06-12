package planto_project.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private String productId;
    private String name;
    private int quantity;
    private BigDecimal priceUnit;

}
