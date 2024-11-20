package org.zeropage.project.timetable.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.zeropage.project.timetable.domain.Member;
import org.zeropage.project.timetable.domain.Timetable;
import org.zeropage.project.timetable.service.TimetableService;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class TimetableApiController {
    TimetableService timetableService;

    @PostMapping("/result/save")
    public void saveTimetable(@AuthenticationPrincipal Member member, @RequestBody Timetable timetable) {
        timetableService.saveTimetable(timetable, member);
    }

    @GetMapping("/timetable/viewOnly/{viewOnlyKey}")
    public Timetable getTimetableByKey(@PathVariable("viewOnlyKey") String key) {
        return timetableService.findByKey(key);
    }

    @GetMapping("/timetable/byMember/{id}")
    public ResponseEntity<Object> getTimetable(@AuthenticationPrincipal Member member,
                                       @PathVariable("id") Long id) {
        Timetable timetable = timetableService.findById(id);
        if (!timetable.getMember().equals(member)) {
            return new ResponseEntity<>("해당 시간표의 소유자가 아닙니다.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(timetable, HttpStatus.OK);
    }

    @DeleteMapping("/timetable/byMember/{id}")
    public ResponseEntity<Object> eraseTimetable(@AuthenticationPrincipal Member member,
                                 @PathVariable("id") Long id) {
        Timetable timetable = timetableService.findById(id);
        if (!timetable.getMember().equals(member)) {
            return new ResponseEntity<>("해당 시간표의 소유자가 아닙니다.", HttpStatus.FORBIDDEN);
        }
        timetableService.deleteTimetable(timetable);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}