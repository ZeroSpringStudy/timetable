package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String userName;

    private String userPW;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Timetable> timeTables = new ArrayList<>();

    public void addTimeTable(Timetable timeTable){
        this.timeTables.add(timeTable);
        timeTable.setMember(this);
    }
}
