package planto_project.dto.filters_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FilterIntegerDto extends FilterDto {

    Integer value;
    Integer valueFrom;
    Integer valueTo;
    List<Integer> valueList;

    public FilterIntegerDto(String field, Integer type) {
        super(field, type);
    }

    public FilterIntegerDto(String field, Integer type, Integer value) {
        super(field, type);
        this.value = value;
    }

    public FilterIntegerDto(String field, Integer type, Integer valueFrom, Integer valueTo) {
        super(field, type);
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public FilterIntegerDto(String field, Integer type, List<Integer> valueList) {
        super(field, type);
        this.valueList = valueList;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getValueFrom() {
        return valueFrom;
    }

    public Integer getValueTo() {
        return valueTo;
    }

    public List<Integer> getValueList() {
        return valueList;
    }
}
