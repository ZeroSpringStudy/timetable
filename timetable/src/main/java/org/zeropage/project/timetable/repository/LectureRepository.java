package org.zeropage.project.timetable.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.zeropage.project.timetable.domain.lecture.Lecture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            jpql+=" and treat(l as RegisteredLecture).college=:college";
        if (StringUtils.hasText(options.getDept()))
            jpql+=" and treat(l as RegisteredLecture).dept=:dept";
        if (StringUtils.hasText(options.getName()))
            jpql+=" and l.name like :name";
        if (StringUtils.hasText(options.getLecturer()))
            jpql+=" and treat(l as RegisteredLecture).lecturer like :lecturer";
        if (options.getLectureCode() != null)
            jpql += " and treat(l as RegisteredLecture).lectureCode=:lectureCode";
        if (options.getCredit() != null)
            jpql += " and treat(l as RegisteredLecture).credit=:credit";
        if (options.getGrade() != null)
            jpql += " and treat(l as RegisteredLecture).grade=:grade";
        if (options.getClassification().size() != 0)
            jpql += " and treat(l as RegisteredLecture).classification in :classification";
        if (StringUtils.hasText(options.getLecturePlace()))
            jpql += " and treat(l as RegisteredLecture).lecturePlace like :lecturePlace";

        int classHoursSize = options.getClassHours().size();
        int classHoursQuerySize = 0; // 아래 if문 이후로는 0이면 classHours 조건이 없는 것임
        if (classHoursSize != 0 && classHoursSize != 24 * 2 * 7
                // 모든 시간을 전부 선택하면 의미가 없으므로 그것도 넘김
                && options.getTimeSearchMode() != null) {

            jpql += " and not l.classHours=:emptyClassHours";
            switch (options.getTimeSearchMode()) {
                case COVER -> {
                    // 이 이외의 시간을 전부 빼는 방법으로 구현
                    classHoursQuerySize = 24 * 2 * 7 - classHoursSize;
                    for (int i = 0; i < classHoursQuerySize; i++)
                        jpql += " and l.classHours not like :classHours" + i;
                }
                case INCLUDE -> {
                    classHoursQuerySize = classHoursSize;
                    jpql += " and (l.classHours like :classHours0";
                    for (int i = 1; i < classHoursQuerySize; i++)
                        jpql += " or l.classHours like :classHours" + i;
                    jpql += ")";
                }
            }
        }

        TypedQuery<Lecture> query = em.createQuery(jpql, Lecture.class);

        if (StringUtils.hasText(options.getCollege()))
            query = query.setParameter("college", options.getCollege());
        if (StringUtils.hasText(options.getDept()))
            query = query.setParameter("dept", options.getDept());
        if (StringUtils.hasText(options.getName()))
            query = query.setParameter("name", '%' + options.getName() + '%');
        if (StringUtils.hasText(options.getLecturer()))
            query = query.setParameter("lecturer", '%' + options.getLecturer() + '%');
        if (options.getLectureCode() != null)
            query = query.setParameter("lectureCode", options.getLectureCode());
        if (options.getCredit() != null)
            query = query.setParameter("credit", options.getCredit());
        if (options.getGrade() != null)
            query = query.setParameter("grade", options.getGrade());
        if (options.getClassification().size() != 0)
            query = query.setParameter("classification", options.getClassification());
        if (StringUtils.hasText(options.getLecturePlace()))
            query = query.setParameter("lecturePlace", '%' + options.getLecturePlace() + '%');
        if (classHoursQuerySize != 0) {
            query = query.setParameter("emptyClassHours", "");
            switch (options.getTimeSearchMode()) {
                case COVER -> {
                    Set<Integer> targetClassHours = new HashSet<>(options.getClassHours());
                    Integer targetClassHoursConut = 0;
                    for (int i = 0; i < classHoursQuerySize; i++, targetClassHoursConut++) {
                        while (targetClassHours.contains(targetClassHoursConut))
                            targetClassHoursConut++;
                        query = query.setParameter("classHours" + i,
                                "%," + targetClassHoursConut.toString() + ",%");
                    }
                }
                case INCLUDE -> {
                    List<Integer> classHours = options.getClassHours();
                    for (int i = 0; i < classHoursQuerySize; i++)
                        query = query.setParameter("classHours" + i,
                                "%," + classHours.get(i).toString() + ",%");
                }
            }
        }

        return query.getResultList();
    }
}
