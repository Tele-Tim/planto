package planto_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SortingDto {
    Integer page;
    Integer size;
    String field;
    Integer direction;
}
