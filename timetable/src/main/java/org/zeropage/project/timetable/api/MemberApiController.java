package org.zeropage.project.timetable.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zeropage.project.timetable.controller.MemberCreateForm;
import org.zeropage.project.timetable.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody @Valid MemberCreateForm memberCreateForm) {
        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            return new ResponseEntity<>("2개의 패스워드가 일치하지 않습니다.", HttpStatus.NOT_ACCEPTABLE);
        }

        memberService.create(memberCreateForm.getUsername(),
                memberCreateForm.getPassword1());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
