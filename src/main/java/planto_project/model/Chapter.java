package planto_project.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chapters")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {
    @Id
    private String id;
    @Setter
    private String name;
    @Indexed
    @Setter
    private String parent;
    @Indexed
    @Setter
    private String[] children;
}
