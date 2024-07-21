package org.zeropage.project.timetable.domain.lecture;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zeropage.project.timetable.wizardoptions.Lecture;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class LectureEntity implements Lecture {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ElementCollection
    private List<Integer> classHours = new ArrayList<>();


    /**
     * Get credit of lecture. If lecture is customed, then it returns 0.
     */
    public int getCredit(){
        return 0;
    }

    public boolean isClassOverlaped(LectureEntity lecture){
        ArrayList<Integer> overlapHours = new ArrayList<>(this.getClassHours());
        overlapHours.retainAll(lecture.getClassHours());
        return overlapHours.size() == 0;
    }


    /**
     * If two lectures are all enrolled by school and name is same,
     * then two lectures are same,
     * which means student cannot register both lectures at once.
     *
     * Uses to avoid two same lectures located at one timetable.
     *
     * Always returns false least one of lecture is customed.
     */
    public boolean equalsByNameAndType(Lecture lecture){
        return this instanceof EnrolledLecture &&
                lecture instanceof EnrolledLecture &&
                this.getName().equals(lecture.getName());
    }
}
