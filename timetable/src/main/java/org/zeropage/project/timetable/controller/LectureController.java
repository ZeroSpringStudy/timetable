package org.zeropage.project.timetable.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zeropage.project.timetable.domain.lecture.CustomLecture;
import org.zeropage.project.timetable.repository.SearchEnrolledLecture;
import org.zeropage.project.timetable.service.LectureService;

@Controller
@RequiredArgsConstructor
public class LectureController {
    LectureService lectureService;

    @GetMapping("/addCustomLecture")
    public String addLectureForm(Model model) {
        model.addAttribute("pageContent", "wizard/addCustomLecture :: addCustomLecture");
        model.addAttribute("customLecture", new CustomLectureDto());
        return "wizard/addCustomLecture";
    }

    @PostMapping("/addCustomLecture")
    @ResponseBody
    public CustomLecture addLecture(CustomLectureDto dto) {
        return new CustomLecture(dto);
    }

    @GetMapping("/search")
    public String getSearchOptions(Model model) {
        model.addAttribute("pageContent", "wizard/search :: searchLectures");
        model.addAttribute("searchEnrolledLecture", new SearchEnrolledLecture());
        return "wizard/search";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute SearchEnrolledLecture searchEnrolledLecture, Model model) {
        model.addAttribute("pageContent", "wizard/search :: searchLectures");
        model.addAttribute("result", lectureService.search(searchEnrolledLecture));
        return "wizard/search";
    }
}
