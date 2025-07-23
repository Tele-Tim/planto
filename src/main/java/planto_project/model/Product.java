package planto_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Document(collection = "product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;
    @Indexed
    @Setter
    private String name;
    @Indexed
    @Setter
    private String category;
    @Setter
    private String description;
    @Indexed
    @Setter
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;
    @Setter
    private String imageUrl;
    @Setter
    private Integer quantity;

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
