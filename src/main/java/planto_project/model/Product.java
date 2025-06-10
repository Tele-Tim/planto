package planto_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    String id;
    @Setter
    String name;
    @Setter
    String category;
    @Setter
    String description;
    @Setter
    BigDecimal price;
    @Setter
    String imageUrl;
    @Setter
    Integer quantity;

    public Integer changeQuantity(Integer amount, boolean isAddition) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (isAddition) {
            this.quantity += amount;
        } else {
        if (amount > this.quantity) {
            throw new IllegalArgumentException("Amount exceeds quantity");
        }
            this.quantity -= amount;
        }
        return this.quantity;
    }

}
