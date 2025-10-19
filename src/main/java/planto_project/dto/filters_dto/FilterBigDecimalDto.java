package planto_project.dto.filters_dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class FilterBigDecimalDto extends FilterDto{

    BigDecimal value;
    BigDecimal valueFrom;
    BigDecimal valueTo;
    List<BigDecimal> valueList;

    public FilterBigDecimalDto(String field, Integer type) {
        super(field, type);
    }

    public FilterBigDecimalDto(String field, Integer type, BigDecimal value) {
        super(field, type);
        this.value = value;
    }

    public FilterBigDecimalDto(String field, Integer type, BigDecimal valueFrom, BigDecimal valueTo) {
        super(field, type);
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public FilterBigDecimalDto(String field, Integer type, List<BigDecimal> valueList) {
        super(field, type);
        this.valueList = valueList;
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getValueFrom() {
        return valueFrom;
    }

    public BigDecimal getValueTo() {
        return valueTo;
    }

    public List<BigDecimal> getValueList() {
        return valueList;
    }
}
