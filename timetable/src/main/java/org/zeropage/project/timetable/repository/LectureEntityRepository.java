package org.zeropage.project.timetable.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.zeropage.project.timetable.domain.lecture.LectureEntity;

import java.util.List;

/**
 * Can be replaced to interface which extends JPARepository&lt;LectureEntity, Long&gt;
 */
@Repository
@RequiredArgsConstructor
public class LectureEntityRepository {
    private final EntityManager em;

    public LectureEntity save(LectureEntity lectureEntity){
        em.persist(lectureEntity);
        return lectureEntity;
    }

    /**
     * Test only. Not for real use.
     */
    public LectureEntity findOne(Long id){
        return em.find(LectureEntity.class, id);
    }

    /**
     * Search enrolled lectures by options.
     * Set of options are saved in SearchEnrolledLecture class.
     */
    public List<LectureEntity> find(SearchEnrolledLecture options){
        String jpql = "select l from LectureEntity l where type(l) in (EnrolledLecture)";

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
        // Need query for time search.

        TypedQuery<LectureEntity> query = em.createQuery(jpql, LectureEntity.class);

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
        // Need query for time search.

        return query.getResultList();
    }
}
