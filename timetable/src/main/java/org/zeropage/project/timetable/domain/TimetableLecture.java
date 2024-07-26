package org.zeropage.project.timetable.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.zeropage.project.timetable.domain.lecture.Lecture;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString //For test. 테스트용.
public class TimetableLecture {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    //Maybe EAGER can be better choice than LAZY in this time... but set to LAZY, however.
    private Lecture lecture;
    @ManyToOne(fetch = FetchType.LAZY)
    //Maybe EAGER can be better choice than LAZY in this time... but set to LAZY, however.
    private TimetableEntity timetable;

    /**
     * Uses to see all last priority in one timetable, to decrease meaningless result.
     * 의미없는 결과를 줄이기 위해 마지막 우선순위를 한번에 시간표에 표시할 때 사용.
     * Low priority. 후순위 구현 대상.
     */
    private boolean isLastPriority;

    public TimetableLecture(Lecture lecture, TimetableEntity timetable, boolean isLastPriority) {
        this.lecture = lecture;
        this.timetable = timetable;
        this.isLastPriority = isLastPriority;
    }
}
