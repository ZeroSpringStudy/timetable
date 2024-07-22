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
 */
@Entity
@DiscriminatorValue("L")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true) //For test.
public class EnrolledLecture extends LectureEntity {

    /**
     * It is float because 0.5 credit is possible.
     * Also possible to save doubled value for int.
     */
    @Column(nullable = false, updatable = false)
    private float credit;

    /**
     * Grade 0 means "all".
     * Range of values: 0~6
     */
    @Column(nullable = false, updatable = false)
    private int grade;

    /**
     * College where holding lecture
     */
    @Column(nullable = false, length = 42) // longest: 적십자간호대학(2011)
    private String college;

    /**
     * Department of college where holding lecture
     */
    @Column(nullable = false, length = 48) // longest: 게임·인터렉티브미디어융합전공
    private String dept;

    /**
     * 과목번호
     * Unsigned int that don't exceed 60000.
     */
    @Column(nullable = false, updatable = false)
    private int lectureCode;

    /**
     * 분반
     * Range of values: 1~74 (for this semester. doesn't exceed 99 in all semesters.)
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
     * Test only. Not for real use.
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
