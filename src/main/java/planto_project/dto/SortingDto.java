package planto_project.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import planto_project.dto.filters_dto.FilterDoubleDto;
import planto_project.dto.filters_dto.FilterDto;
import planto_project.dto.filters_dto.FilterStringDto;

import java.util.List;

@AllArgsConstructor
@Getter
public class SortingDto {
    Integer page;
    Integer size;
    String field;
    Integer direction;
    List<FilterDto> criteria;
}
