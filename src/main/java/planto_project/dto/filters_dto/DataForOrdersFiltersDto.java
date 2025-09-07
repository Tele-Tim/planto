package planto_project.dto.filters_dto;

import lombok.Getter;
import planto_project.model.OrderStatus;

import java.util.Arrays;
import java.util.List;

@Getter
public class DataForOrdersFiltersDto {
    private final List<String> statuses;

    public DataForOrdersFiltersDto() {
        this.statuses = Arrays.stream(OrderStatus.values()).map(Enum::toString).toList();
    }
}
