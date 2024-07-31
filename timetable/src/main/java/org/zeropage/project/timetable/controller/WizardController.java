package org.zeropage.project.timetable.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.zeropage.project.timetable.domain.Timetable;
import org.zeropage.project.timetable.wizardoptions.Wizard;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("wizard")
public class WizardController {

    @GetMapping("/settings")
    public String getWizard(Model model) {
        model.addAttribute("pageContent", "wizard/settings :: wizardSettings");
        model.addAttribute("wizard", new Wizard());
        return "wizard/settings";
    }

    @PostMapping("/settings")
    public String getResult(@ModelAttribute Wizard wizard, Model model) {
        List<Timetable> result = wizard.result();
        model.addAttribute("pageContent", "timetable :: timetableView");
        model.addAttribute("result", result);
        return "fragments/layout";
    }


    @GetMapping("/addGroup")
    public String showAddGroupForm(Model model) {
        model.addAttribute("pageContent","");
        return "wizard/addgroup";  // Thymeleaf template for adding a group
    }

    @PostMapping("/addGroup")
    public RedirectView addGroup(@RequestParam String name) {
        // Save the group using service or repository
        // Example: groupService.addGroup(new GroupOfGroup(name));

        // Redirect to the main page or groups list
        return new RedirectView("/");  // Redirect to the main page
    }
}
