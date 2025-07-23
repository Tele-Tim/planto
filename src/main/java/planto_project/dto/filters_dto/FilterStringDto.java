package planto_project.dto.filters_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FilterStringDto extends FilterDto{

    String value;
    String valueFrom;
    String valueTo;
    List<String> valueList;

    public FilterStringDto(String field, Integer type) {
        super(field, type);
    }

    public FilterStringDto(String field, Integer type, String value) {
        super(field, type);
        this.value = value;
    }

    public FilterStringDto(String field, Integer type, String valueFrom, String valueTo) {
        super(field, type);
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public FilterStringDto(String field, Integer type, List<String> valueList) {
        super(field, type);
        this.valueList = valueList;
    }


    public String getValue() {
        return value;
    }

    public String getValueFrom() {
        return valueFrom;
    }

    public String getValueTo() {
        return valueTo;
    }

    public List<String> getValueList() {
        return valueList;
    }
}
