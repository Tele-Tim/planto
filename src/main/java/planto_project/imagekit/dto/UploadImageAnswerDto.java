package planto_project.imagekit.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UploadImageAnswerDto {
    private String url;
    private boolean success;
    private String msg;
}