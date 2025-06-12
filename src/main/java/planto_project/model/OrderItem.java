package planto_project.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    private String productId;
    private String name;
    private int quantity;
    private BigDecimal priceUnit;

}
