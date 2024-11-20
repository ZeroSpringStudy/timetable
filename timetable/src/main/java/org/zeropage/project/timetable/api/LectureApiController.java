package org.zeropage.project.timetable.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zeropage.project.timetable.controller.CustomLectureDto;
import org.zeropage.project.timetable.domain.lecture.CustomLecture;
import org.zeropage.project.timetable.domain.lecture.RegisteredLecture;
import org.zeropage.project.timetable.repository.SearchEnrolledLecture;
import org.zeropage.project.timetable.service.LectureService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LectureApiController {
    LectureService lectureService;

    @PostMapping("/addCustomLecture")
    public CustomLecture addLecture(CustomLectureDto dto) {
        return new CustomLecture(dto);
    }

    @GetMapping("/search")
    public SearchEnrolledLecture getSearchOptions() {
        return new SearchEnrolledLecture();
    }

    @PostMapping("/search")
    public List<RegisteredLecture> search(SearchEnrolledLecture searchEnrolledLecture) {
        return lectureService.search(searchEnrolledLecture);
    }
}
