package org.zeropage.project.timetable.domain.lecture;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Lecture that customed by member
 * 사용자가 직접 만든 강의(일정)
 */
@Entity
@DiscriminatorValue("N")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomLecture extends Lecture{
    public CustomLecture(String name, String classHours) {
        super(name, classHours);
    }
    public CustomLecture(String name, List<Integer> classHours) {
        super(name, classHours);
    }
}
