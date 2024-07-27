package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class TimeTable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "timeTable")
    private List<LectureTimeTable> lectureTimeTables = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void addLectureTimeTable(LectureTimeTable lectureTimeTable){
        this.lectureTimeTables.add(lectureTimeTable);
        lectureTimeTable.setTimeTable(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getTimeTables().add(this);
    }
}
