package org.zeropage.project.timetable.controller;

import lombok.Data;
import org.zeropage.project.timetable.domain.lecture.LectureStartTimeAndEndTime;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomLectureDto {
    private String name;
    private List<LectureStartTimeAndEndTime> classHours = new ArrayList<>();
}
