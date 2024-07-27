package com.example.demo.repository;

import com.example.demo.domain.lecture.Lecture;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LectureRepository {
    private final EntityManager em;

    public void save(Lecture lecture){
        if(lecture.getId() == null){
            em.persist(lecture);
        }
        else{
            em.merge(lecture);
        }
    }
}
