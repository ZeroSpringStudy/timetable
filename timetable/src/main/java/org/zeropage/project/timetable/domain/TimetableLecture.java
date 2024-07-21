package org.zeropage.project.timetable.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.zeropage.project.timetable.domain.lecture.LectureEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimetableLecture {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    //Maybe EAGER can be better choice than LAZY in this time... but set to LAZY, however.
    private LectureEntity lecture;
    @ManyToOne(fetch = FetchType.LAZY)
    //Maybe EAGER can be better choice than LAZY in this time... but set to LAZY, however.
    private TimetableEntity timetable;

    /**
     * Uses to see all last priority in one timetable, to decrease meaningless result.
     * Low priority. 후순위 구현 대상.
     */
    private boolean isLastPriority;

    public TimetableLecture(LectureEntity lecture, TimetableEntity timetable, boolean isLastPriority) {
        this.lecture = lecture;
        this.timetable = timetable;
        this.isLastPriority = isLastPriority;
    }
}
