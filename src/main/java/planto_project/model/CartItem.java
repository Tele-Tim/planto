package planto_project.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "productId")
public class CartItem {
    private String productId;
    private int quantity;

    public CartItem(String productId) {
        this.productId = productId;
        this.quantity = 1;
    }

    public void incrementItem() {
        this.quantity++;
    }

    public void decrementItem() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
    }

}
