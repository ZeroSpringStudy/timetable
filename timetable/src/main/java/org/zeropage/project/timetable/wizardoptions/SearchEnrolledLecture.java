package org.zeropage.project.timetable.wizardoptions;

import lombok.Data;
import org.zeropage.project.timetable.domain.lecture.Classification;
import org.zeropage.project.timetable.domain.lecture.EnrolledLecture;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchEnrolledLecture {
    private String college;
    private String dept;
    private String name;
    private String lecturer;
    private int lectureCode;
    // private int lectureSection;
    private List<Classification> classification = new ArrayList<>();
    // private List<CourseType> courseType;
    private float credit;
    private int grade;
    private List<Integer> classHours = new ArrayList<>();

    /**
     * Select mode searching classHours,
     * to search lectures all covered by set classhours,
     * or to search all lectures at least overlaped by set classhours.
     */
    private TimeSearchMode classHoursMode;

    /**
     * Search with options.
     * @return result of search
     */
    public List<EnrolledLecture> search(){
        //TODO
        throw new UnsupportedOperationException(); //temporal exception
    }
}
