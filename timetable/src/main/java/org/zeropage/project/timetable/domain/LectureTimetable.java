package org.zeropage.project.timetable.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.zeropage.project.timetable.domain.lecture.Lecture;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString //For test. 테스트용.
public class LectureTimetable {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    //Maybe EAGER can be better choice than LAZY in this time... but set to LAZY, however.
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;
    @ManyToOne(fetch = FetchType.LAZY)
    //Maybe EAGER can be better choice than LAZY in this time... but set to LAZY, however.
    @JoinColumn(name = "timetable_id")
    @ToString.Exclude //이거 안 하면 서로 참조가 되어 StackOverflow 발생
    private Timetable timetable;

    /**
     * Uses to see all last priority in one timetable, to decrease meaningless result.
     * 의미없는 결과를 줄이기 위해 마지막 우선순위를 한번에 시간표에 표시할 때 사용.
     * Low priority. 후순위 구현 대상.
     */
    private boolean isLastPriority;

    public LectureTimetable(Lecture lecture, Timetable timetable, boolean isLastPriority) {
        this.lecture = lecture;
        this.timetable = timetable;
        this.isLastPriority = isLastPriority;
    }

    public void setTimetable(Timetable timeTable){
        this.timetable = timeTable;
        timeTable.getLectures().add(this);
    }
}
