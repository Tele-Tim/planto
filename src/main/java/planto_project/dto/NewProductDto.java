package planto_project.dto;

import lombok.Getter;

@Getter
public class NewProductDto {
    String name;
    String category;
    String quantity;
    String price;
    String imageUrl;
    String description;
}
