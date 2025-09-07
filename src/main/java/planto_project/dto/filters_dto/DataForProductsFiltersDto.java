package planto_project.dto.filters_dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DataForProductsFiltersDto {
    BigDecimal price;
    List<String> categories;
}
