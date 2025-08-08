package planto_project.imagekit.service;

import org.springframework.stereotype.Service;
import planto_project.dto.FileDto;
import planto_project.imagekit.dto.UploadImageAnswerDto;

@Service
public interface ImageKitService {
    public UploadImageAnswerDto uploadImage(FileDto fileDto);
}
