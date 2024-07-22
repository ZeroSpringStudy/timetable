package org.zeropage.project.timetable.domain.lecture;

import jakarta.persistence.*;
import lombok.*;
import org.zeropage.project.timetable.wizardoptions.Lecture;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString //For test.
public abstract class LectureEntity implements Lecture {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    /**
     * Save by splitting one week by 30 minutes.
     * 0 is Monday 00:00~00:30,
     * 1 is Monday 00:30~01:00,
     * 47 is Monday 23:30~24:00,
     * 48 is Tuesday 00:00~00:30,
     * 95 is Tuesday 23:30~24:00,
     * 143 is Wednesday 23:30~24:00,
     * 191 is Thursday 23:30~24:00,
     * 239 is Friday 23:30~24:00,
     * 287 is Saturday 23:30~24:00.
     * Didn't expected Sunday because there are no enrolled lecture on Sunday.
     */
    @ElementCollection
    private List<Integer> classHours = new ArrayList<>();


    /**
     * Get credit of lecture. If lecture is customed, then it returns 0.
     */
    public float getCredit(){
        return 0F;
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
