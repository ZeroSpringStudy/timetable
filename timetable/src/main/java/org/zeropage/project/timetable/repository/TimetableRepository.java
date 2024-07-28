package org.zeropage.project.timetable.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.zeropage.project.timetable.domain.Timetable;

@Repository
@RequiredArgsConstructor
public class TimetableRepository {
    private final EntityManager em;

    public Timetable save(Timetable timetable) {
        em.persist(timetable);
        return timetable;
    }

    public void remove(Timetable timetable) {
        em.createQuery("delete Timetable t where id=:id")
                .setParameter("id", timetable.getId())
                .executeUpdate();
        em.clear();
    }

    public Timetable findOne(Long id) {
        //fetch join 때문에 find로 가져오기 불가
        return em.createQuery("select t from Timetable t join fetch t.lectures lt" +
                        " join fetch lt.lecture l where t.id=:id", Timetable.class)
                .setParameter("id", id).getSingleResult();
    }

    public Timetable findByViewOnlyKey(String key) {
        return em.createQuery("select t from Timetable t join fetch t.lectures lt" +
                        " join fetch lt.lecture l where t.viewOnlyKey=:key", Timetable.class)
                .setParameter("key", key).getSingleResult();
    }
}
