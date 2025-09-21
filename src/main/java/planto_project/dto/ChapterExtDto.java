package planto_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChapterExtDto {
    private String id;
    private String name;
    private String parent;
    private String[] children;
}
