package org.zeropage.project.timetable.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zeropage.project.timetable.domain.Timetable;
import org.zeropage.project.timetable.wizardoptions.Wizard;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wizard")
public class WizardApiController {

    @PostMapping("/settings")
    public List<Timetable> getResult(@RequestBody Wizard wizard) {
        return wizard.result();
    }
}