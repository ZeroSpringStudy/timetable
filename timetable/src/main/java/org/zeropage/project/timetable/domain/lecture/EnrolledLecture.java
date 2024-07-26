package org.zeropage.project.timetable.domain.lecture;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;


/**
 * Lecture already enrolled by school
 * 학교에서 등록한 강의
 */
@Entity
@DiscriminatorValue("L")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true) //For test. 테스트용
public class EnrolledLecture extends Lecture {

    /**
     * It is float because 0.5 credit lecture exists.
     * Also possible to save doubled value for int.
     * Avaliable values: 0.5, 1, 2, 3, 4, 4.5, 5, 6, 6.5, 8, 9, 10
     * 0.5학점 강의가 존재해 float으로 설정함.
     * 원래 학점에서 2를 곱하여 정수로 저장하는 것 또한 가능
     * 가능한 값: 0.5, 1, 2, 3, 4, 4.5, 5, 6, 6.5, 8, 9, 10
     */
    @Column(nullable = false, updatable = false)
    private float credit;

    /**
     * Grade 0 means "all".
     * Range of values: 0~6
     * 0은 전체를 의미함.
     * 값의 범위: 0~6
     */
    @Column(nullable = false, updatable = false)
    private int grade;

    /**
     * College where holding lecture
     * 대학(원)/교양/연계/융합
     */
    @Column(nullable = false, length = 42) // longest: 적십자간호대학(2011)
    private String college;

    /**
     * Department of college where holding lecture
     * 학부(과)/전공/영역
     */
    @Column(nullable = false, length = 48) // longest: 게임·인터렉티브미디어융합전공
    private String dept;

    /**
     * 과목번호
     * Unsigned int that don't exceed 60000.
     * 60000을 넘지 않는 양의 정수
     */
    @Column(nullable = false, updatable = false)
    private int lectureCode;

    /**
     * 분반
     * Range of values: 1~74 (for this semester. doesn't exceed 99 in all semesters.)
     * 값의 범위: 1~74 (이번 학기에는. 모든 학기에서 99를 넘지는 않음)
     */
    @Column(nullable = false, updatable = false)
    private int lectureSection;

    @Enumerated(EnumType.STRING)
    private Classification classification;
    @Enumerated(EnumType.STRING)
    private CourseType courseType;
    @Column(nullable = true, length = 64) // Longest unexpected but it cannot be too long.
    private String lecturer;

    @Lob // Can be too long to save by varchar.
    private String remark;

    /**
     * Not for user to use. Only for staff.
     * 관리용. 사용자가 사용하라고 만든 것이 아님.
     */
    public EnrolledLecture(String name, List<Integer> classHours, float credit, int grade,
                           String college, String dept, int lectureCode, int lectureSection,
                           Classification classification, CourseType courseType,
                           String lecturer, String remark) {
        super(null, name, classHours);
        this.credit = credit;
        this.grade = grade;
        this.college = college;
        this.dept = dept;
        this.lectureCode = lectureCode;
        this.lectureSection = lectureSection;
        this.classification = classification;
        this.courseType = courseType;
        this.lecturer = lecturer;
        this.remark = remark;
    }

    /**
     * Uses to check lecture from two or more different dept are same each other.
     * 다른 학과/학부에서 열린 두 강의가 같은지 확인할 때 사용.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrolledLecture lecture)) return false;
        return Float.compare(lecture.getCredit(), getCredit()) == 0 &&
                getGrade() == lecture.getGrade() &&
                getLectureCode() == lecture.getLectureCode() &&
                getLectureSection() == lecture.getLectureSection() &&
                getClassification() == lecture.getClassification() &&
                getCourseType() == lecture.getCourseType() &&
                Objects.equals(getLecturer(), lecture.getLecturer()) &&
                Objects.equals(getRemark(), lecture.getRemark());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCredit(), getGrade(), getLectureCode(), getLectureSection(), getClassification(), getCourseType(), getLecturer(), getRemark());
    }
}