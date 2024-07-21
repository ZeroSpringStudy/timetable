package org.zeropage.project.timetable.domain.lecture;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Lecture that customed by member
 */
@Entity
@DiscriminatorValue("C")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomLectureEntity extends LectureEntity{
    public CustomLectureEntity(Long id, String name, List<Integer> classHours) {
        super(id, name, classHours);
    }
}
