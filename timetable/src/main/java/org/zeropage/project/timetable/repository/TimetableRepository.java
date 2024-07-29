package org.zeropage.project.timetable.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.zeropage.project.timetable.domain.Member;
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
        em.remove(em.find(Timetable.class, timetable.getId()));
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

    public boolean isViewOnlyKeyUseable(String key) {
        return em.createQuery("select count(t) from Timetable t" +
                        " where t.viewOnlyKey=:key", Long.class)
                .setParameter("key", key).getSingleResult().equals(0L);
    }

    /**
     * Member를 그냥 삭제하면 timetable에서의 reference로 문제가 생기므로,
     * 그 전에 reference만 다 지워주는 작업
     * 저장된 timetable은 지우지 않음
     * @param member 삭제할 member
     */
    public void eraseMember(Member member) {
        em.createQuery("update Timetable t set t.member=null" +
                        " where t.member=:member")
                .setParameter("member", member)
                .executeUpdate();
    }
}
