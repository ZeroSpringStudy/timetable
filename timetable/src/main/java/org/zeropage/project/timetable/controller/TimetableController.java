package org.zeropage.project.timetable.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zeropage.project.timetable.domain.Member;
import org.zeropage.project.timetable.domain.Timetable;
import org.zeropage.project.timetable.service.TimetableService;

@RequestMapping
@Controller
@RequiredArgsConstructor
public class TimetableController {
    TimetableService timetableService;

    @PostMapping("/result/save")
    public void saveTimetable(@AuthenticationPrincipal Member member, Timetable timetable) {
        timetableService.saveTimetable(timetable, member);
    }

    @GetMapping("/timetable/viewOnly/{viewOnlyKey}")
    public String getTimetableByKey(@PathVariable("viewOnlyKey") String key, Model model) {
        model.addAttribute("pageContent", "timetable :: timetableView");
        model.addAttribute("timetable", timetableService.findByKey(key));
        return "fragments/layout";
    }

    @GetMapping("/timetable/byMember/{id}")
    public String getTimetable(@AuthenticationPrincipal Member member,
                               @PathVariable("id") Long id, Model model) {
        model.addAttribute("pageContent", "timetable :: timetableView");
        Timetable timetable = timetableService.findById(id);
        if (!timetable.getMember().equals(member)) {
            throw new IllegalStateException("해당 시간표의 소유자가 아닙니다.");
        }
        model.addAttribute("timetable", timetable);
        return "fragments/layout";
    }

    @DeleteMapping("/timetable/byMember/{id}")
    public String eraseTimetable(@AuthenticationPrincipal Member member,
                                 @PathVariable("id") Long id, Model model) {
        Timetable timetable = timetableService.findById(id);
        if (!timetable.getMember().equals(member)) {
            throw new IllegalStateException("해당 시간표의 소유자가 아닙니다.");
        }
        timetableService.deleteTimetable(timetable);
        return "home";
    }
}
