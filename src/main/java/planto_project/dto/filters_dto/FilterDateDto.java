package planto_project.dto.filters_dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FilterDateDto extends FilterDto{
    LocalDate value;
    LocalDate valueFrom;
    LocalDate valueTo;
    List<LocalDate> valueList;

    public FilterDateDto(String field, Integer type) {
        super(field, type);
    }

    public FilterDateDto(String field, Integer type, LocalDate value) {
        super(field, type);
        this.value = value;
    }

    public FilterDateDto(String field, Integer type, LocalDate valueFrom, LocalDate valueTo) {
        super(field, type);
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public FilterDateDto(String field, Integer type, List<LocalDate> valueList) {
        super(field, type);
        this.valueList = valueList;
    }
}
