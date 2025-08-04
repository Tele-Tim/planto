package planto_project.imagekit.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import planto_project.dto.FileDto;
import planto_project.imagekit.dto.UploadImageAnswerDto;
import planto_project.imagekit.service.ImageKitService;

@RestController
@RequestMapping("/uploadImage")
@RequiredArgsConstructor
public class ImageKitController {

    final ImageKitService imageKitService;

    @PostMapping("")
    public UploadImageAnswerDto uploadImage(@ModelAttribute FileDto fileDto) {
        return imageKitService.uploadImage(fileDto);
    }

}
