package planto_project.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvents implements Serializable {

    private String orderId;
    private String userLogin;
    private String userEmail;
    private List<OrderItemInfo> items;
    private String paymentMethod;
    private LocalDateTime orderDate;
    private Double totalAmount;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemInfo implements Serializable {
        private String productId;
        private String productName;
        private Integer quantity;
        private Double priceUnit;
    }
}