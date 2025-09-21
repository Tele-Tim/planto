package planto_project.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import planto_project.dao.ChapterRepository;
import planto_project.dto.ChapterDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("chapter")
public class ChapterController {
    private final ChapterRepository chapterRepository;
    private final ModelMapper modelMapper;

    @GetMapping("all")
    public List<ChapterDto> findAllChapters() {
        return chapterRepository.findAllBy().stream().map(chapter->modelMapper.map(chapter, ChapterDto.class)).toList();
    }

}
