package planto_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    String id;
    String name;
    String category;
    String idChapter;
    String description;
    BigDecimal price;
    String imageUrl;
    Integer quantity;
}
