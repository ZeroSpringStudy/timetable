package org.zeropage.project.timetable.wizardoptions;

import lombok.Data;
import org.zeropage.project.timetable.domain.lecture.Lecture;

import java.util.ArrayList;
import java.util.List;

/**
 * Group of lectures. Uses in wizard.
 * 강의의 집합. 시간표 마법사에 사용.
 */
@Data
public class LectureGroup {
    private String name;
    private List<Lecture> lectureList = new ArrayList<>();
    private Integer numOfLecturePicks;

    public void addLecture(Lecture lecture){
        lectureList.add(lecture);
    }

    public void deleteLecture(Lecture lecture){
        lectureList.remove(lecture);
    }
}
