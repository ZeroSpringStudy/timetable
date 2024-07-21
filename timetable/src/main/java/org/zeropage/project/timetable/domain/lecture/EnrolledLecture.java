package org.zeropage.project.timetable.domain.lecture;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;


/**
 * Lecture already enrolled by school
 */
@Entity
@DiscriminatorValue("L")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EnrolledLecture extends LectureEntity {
    private float credit;
    private int grade;

    /**
     * College where holding lecture
     */
    private String college;

    /**
     * Department of college where holding lecture
     */
    private String dept;
    private int lectureCode;
    private int lectureSection;
    @Enumerated(EnumType.STRING)
    private Classification classification;
    @Enumerated(EnumType.STRING)
    private CourseType courseType;
    private String lecturer;
    private String remark;

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
