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
     */
    @Column(nullable = false, updatable = false)
    private float credit;

    /**
     * Grade 0 means "all".
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
     */
    @Column(nullable = false, updatable = false)
    private int lectureCode;

    /**
     * 분반
     * default value is for forgetting to put this by staff because there is only one lecture.
     */
    @Column(nullable = false, updatable = false)
    private int lectureSection;

    @Enumerated(EnumType.STRING)
    private Classification classification;
    @Enumerated(EnumType.STRING)
    private CourseType courseType;
    @Column(nullable = true, length = 48) // Unexpected longest but it cannot be too long.
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
     * They are same lecture, but in many depts to let many students of various depts register.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrolledLecture that)) return false;
        return getLectureCode() == that.getLectureCode() &&
                getLectureSection() == that.getLectureSection() &&
                Objects.equals(getCollege(), that.getCollege()) &&
                Objects.equals(getDept(), that.getDept());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCollege(), getDept(), getLectureCode(), getLectureSection());
    }
}
