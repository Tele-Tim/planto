package planto_project.dto.filters_dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "typeValue")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FilterStringDto.class, name ="string"),
        @JsonSubTypes.Type(value = FilterIntegerDto.class, name = "integer"),
        @JsonSubTypes.Type(value = FilterDoubleDto.class, name = "double")
        })
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class FilterDto {

    String field;
    Integer type;

    public abstract Object getValue();
    public abstract Object getValueFrom();
    public abstract Object getValueTo();
    public abstract List<?> getValueList();
}

