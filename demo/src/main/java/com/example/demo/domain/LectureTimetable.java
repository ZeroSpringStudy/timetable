package com.example.demo.domain;

import com.example.demo.domain.lecture.Lecture;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LectureTimetable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "lecture_timetable_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
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
