package org.zeropage.project.timetable.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.zeropage.project.timetable.domain.lecture.Lecture;

import java.util.List;

/**
 * Can be replaced to interface which extends JPARepository&lt;LectureEntity, Long&gt;
 * JPARepository&lt;LectureEntity, Long&gt;를 상속한 인터페이스로 변경될 수 있음.
 */
@Repository
@RequiredArgsConstructor
public class LectureRepository {
    private final EntityManager em;

    public Lecture save(Lecture lecture){
        em.persist(lecture);
        return lecture;
    }

    /**
     * Not for user to use. Only for staff.
     * 관리용. 사용자가 사용하라고 만든 것이 아님.
     */
    public Lecture findOne(Long id){
        return em.find(Lecture.class, id);
    }

    /**
     * Search enrolled lectures by options.
     * Set of options are saved in SearchEnrolledLecture class.
     * 검색조건에 따라 학교에서 등록한 강의를 검색함.
     * 검색조건은 SearchEnrolledLecture class에 저장되어 있음.
     */
    public List<Lecture> find(SearchEnrolledLecture options){
        String jpql = "select l from Lecture l where type(l) in (RegisteredLecture)";

        if (StringUtils.hasText(options.getCollege()))
            jpql+=" and treat(l as EnrolledLecture).college=:college";
        if (StringUtils.hasText(options.getDept()))
            jpql+=" and treat(l as EnrolledLecture).dept=:dept";
        if (StringUtils.hasText(options.getName()))
            jpql+=" and treat(l as EnrolledLecture).name like :name";
        if (StringUtils.hasText(options.getLecturer()))
            jpql+=" and treat(l as EnrolledLecture).lecturer like :lecturer";
        if (options.getLectureCode() != null)
            jpql += " and treat(l as EnrolledLecture).lectureCode=:lectureCode";
        if (options.getCredit() != null)
            jpql += " and treat(l as EnrolledLecture).credit=:credit";
        if (options.getGrade() != null)
            jpql += " and treat(l as EnrolledLecture).grade=:grade";
        if (options.getClassification().size() != 0)
            jpql += " and treat(l as EnrolledLecture).classification in :classification";
        // TODO Need query for time search.

        TypedQuery<Lecture> query = em.createQuery(jpql, Lecture.class);

        if (StringUtils.hasText(options.getCollege()))
            query = query.setParameter("college", options.getCollege());
        if (StringUtils.hasText(options.getDept()))
            query = query.setParameter("dept", options.getDept());
        if (StringUtils.hasText(options.getName()))
            query = query.setParameter("name", '%'+options.getName()+'%');
        if (StringUtils.hasText(options.getLecturer()))
            query = query.setParameter("lecturer", '%'+options.getLecturer()+'%');
        if (options.getLectureCode() != null)
            query = query.setParameter("lectureCode", options.getLectureCode());
        if (options.getCredit() != null)
            query = query.setParameter("credit", options.getCredit());
        if (options.getGrade() != null)
            query = query.setParameter("grade", options.getGrade());
        if (options.getClassification().size() != 0)
            query = query.setParameter("classification", options.getClassification());
        // TODO Need query for time search.

        return query.getResultList();
    }
}
