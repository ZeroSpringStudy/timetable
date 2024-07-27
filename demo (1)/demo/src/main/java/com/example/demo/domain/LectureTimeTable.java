package com.example.demo.domain;

import com.example.demo.domain.lecture.Lecture;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter @Setter
public class LectureTimeTable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "lecture_timetable_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private TimeTable timeTable;

    public void setLecture(Lecture lecture){
        this.lecture = lecture;
        lecture.getLectureTimeTables().add(this);
    }

    public void setTimeTable(TimeTable timeTable){
        this.timeTable = timeTable;
        timeTable.getLectureTimeTables().add(this);
    }

}
