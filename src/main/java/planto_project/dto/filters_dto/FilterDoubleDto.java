package planto_project.dto.filters_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class FilterDoubleDto extends FilterDto{

    Double value;
    Double valueFrom;
    Double valueTo;
    List<Double> valueList;

    public FilterDoubleDto(String field, Integer type) {
        super(field, type);
    }

    public FilterDoubleDto(String field, Integer type, Double value) {
        super(field, type);
        this.value = value;
    }

    public FilterDoubleDto(String field, Integer type, Double valueFrom, Double valueTo) {
        super(field, type);
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public FilterDoubleDto(String field, Integer type, List<Double> valueList) {
        super(field, type);
        this.valueList = valueList;
    }

    public Double getValue() {
        return value;
    }

    public Double getValueFrom() {
        return valueFrom;
    }

    public Double getValueTo() {
        return valueTo;
    }

    public List<Double> getValueList() {
        return valueList;
    }
}
