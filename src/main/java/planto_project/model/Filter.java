package planto_project.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.regex.Pattern;

@NoArgsConstructor
@Getter
@Setter
public class Filter<T> {
    private String field;
    private Integer type;
    private T value;
    private T valueFrom;
    private T valueTo;
    private List<T> valueList;


    public Filter(String field, Integer type, T value) {
        this.field = field;
        this.type = type;
        this.value = value;
    }

    public Filter(String field, Integer type, T valueFrom, T valueTo) {
        this.field = field;
        this.type = type;
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public Filter(String field, Integer type, List<T> valueList) {
        this.field = field;
        this.type = type;
        this.valueList = valueList;
    }

    public Criteria getCriteria() {
        if (this.type == 0 && this.value != null) {
            return Criteria.where(this.field).is(this.value);
        }

        if (this.type == 1
                && this.value != null) {

            String strLow = ((String) this.value).toLowerCase();
            String strUp = ((String) this.value).toUpperCase();

            String regex = ".*" + this.value + ".*";
            regex = Pattern.quote("" + this.value);
            return Criteria.where(this.field).regex(regex, "i");
        }

        if (this.type == 2) {

            Criteria criteria = Criteria.where(this.field);

            if (this.valueFrom != null) {
                return criteria.gte(this.valueFrom);
            }

            if (this.valueTo != null) {
                return criteria.lte(this.valueTo);
            }

            return criteria;

        }

        if (this.type == 3
                && !this.valueList.isEmpty()) {
            return Criteria.where(this.field).in(this.valueList);
        }

        return null;
    }
}
