package com.example.demo.domain.lecture;

import com.example.demo.domain.LectureTimeTable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Lecture {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lectureName; // 강의명

    private Integer classTime;  // 시간

    private String lectureTime; // String으로 변환된 String 배열

    @OneToMany(mappedBy = "lecture")
    private List<LectureTimeTable> lectureTimeTables = new ArrayList<>();

    public void addLectureTimeTable(LectureTimeTable lectureTimeTable){
        lectureTimeTables.add(lectureTimeTable);
        lectureTimeTable.setLecture(this);
    }
}
