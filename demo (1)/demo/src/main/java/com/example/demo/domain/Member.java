package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String userPW;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TimeTable> timeTables = new ArrayList<>();

    public void addTimeTable(TimeTable timeTable){
        this.timeTables.add(timeTable);
        timeTable.setMember(this);
    }
}
