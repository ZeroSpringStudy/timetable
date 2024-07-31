package org.zeropage.project.timetable.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.zeropage.project.timetable.service.MemberService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String signin(Model model) {
        model.addAttribute("pageContent", "login :: login");
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("pageContent", "signup_form :: signup");
        model.addAttribute("memberCreateForm", new MemberCreateForm());
        return "fragments/layout";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "fragments/layout";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "fragments/layout";
        }

        memberService.create(memberCreateForm.getUsername(),
                memberCreateForm.getPassword1());

        return "redirect:/";
    }
}
