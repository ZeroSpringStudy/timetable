package org.zeropage.project.timetable.repository;

import lombok.Data;
import org.zeropage.project.timetable.domain.lecture.Classification;
import org.zeropage.project.timetable.domain.lecture.EnrolledLecture;
import org.zeropage.project.timetable.wizardoptions.TimeSearchMode;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchEnrolledLecture {
    private String college;
    private String dept;
    private String name;
    private String lecturer;
    private Integer lectureCode;
    // private int lectureSection;
    private List<Classification> classification;
    // private List<CourseType> courseType;
    private Float credit;
    private Integer grade;
    private List<Integer> classHours;

    /**
     * Select mode searching classHours,
     * to search lectures all covered by set classhours,
     * or to search all lectures at least overlaped by set classhours.
     */
    private TimeSearchMode classHoursMode;

    public SearchEnrolledLecture() {
        this.college = "";
        this.dept = "";
        this.name = "";
        this.lecturer = "";
        this.lectureCode = null;
        this.classification = new ArrayList<>();
        this.credit = null;
        this.grade = null;
        this.classHours = new ArrayList<>();
        this.classHoursMode = TimeSearchMode.COVER;
    }

    /**
     * Search with options.
     * @return result of search
     */
    public List<EnrolledLecture> search(){
        //TODO
        throw new UnsupportedOperationException(); //temporal exception
    }
}
