package org.zeropage.project.timetable.repository;

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
     * 설정한 강의시간을 전부 포함하는 강의만 검색할 것인지,
     * 시간이 조금이라도 겹치는 모든 강의를 검색할 것인지
     * 검색 방법을 설정함.
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
     * 조건에 따라 검색함.
     * @return result of search 검색 결과
     */
    public List<EnrolledLecture> search(){
        //TODO
        throw new UnsupportedOperationException(); //temporal exception 임시 예외
    }
}
