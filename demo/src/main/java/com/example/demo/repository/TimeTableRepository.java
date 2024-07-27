package com.example.demo.repository;

import com.example.demo.domain.Timetable;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TimeTableRepository {
    private final EntityManager em;

    public void save(Timetable timeTable){
        if(timeTable.getId() == null){
            em.persist(timeTable);
        }
        else{
            em.merge(timeTable);
        }
    }
}
