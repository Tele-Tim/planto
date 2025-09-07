package planto_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import planto_project.dto.filters_dto.FilterDto;

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
