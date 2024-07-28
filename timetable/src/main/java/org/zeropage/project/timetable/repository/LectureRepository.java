package org.zeropage.project.timetable.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.zeropage.project.timetable.domain.lecture.CustomLecture;
import org.zeropage.project.timetable.domain.lecture.Lecture;
import org.zeropage.project.timetable.domain.lecture.RegisteredLecture;

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

    /**
     * CustomLecture을 어디서도 사용하지 않을 때 삭제하기 위해,
     * lecture column 기준 검색 후 결과 산출
     *
     * @param lecture 검사 대상 객체
     * @return 이 lecture의 reference 수
     */
    public int getNumOfReferencedLecture(Lecture lecture) {
        Integer refCount = em.createQuery("select count(lt) from LectureTimetable lt" +
                        " where lt.lecture=:lecture", Integer.class)
                .setParameter("lecture", lecture)
                .getSingleResult();
        // 그냥 반환을 int로 하기 때문에, 등록된 강의면 reference를 그 자체로 하나 있는 것으로 취급하여 해결
        // 혹시 쓸일 있을까 싶어 boolean이 아니라 int로 하긴 했는데,
        // CustomLecture의 삭제 여부만을 보는 지금은 오히려 복잡해지기한 하는 코드임
        if(lecture instanceof RegisteredLecture) refCount++;
        return refCount;
    }

    /**
     * CustomLecture 객체를 삭제함
     * 다른 Lecture 객체가 들어오면 예외를 던짐
     * 별도의 reference 확인은 하지 않으므로, getNumOfReferencedLecture 메서드 먼저 이용 필수
     *
     * @param lecture 삭제할 강의
     * @throws AccessDeniedException CustomLecture 객체가 아닌 다른 객체를 삭제하려 할 때
     */
    public void remove(Lecture lecture) {
        if (!(lecture instanceof CustomLecture)) {
            throw new AccessDeniedException("학교에서 등록한 강의를 삭제하려 했습니다. 올바른 작동인지 확인해 주세요." +
                    " 폐강으로 인해 강의를 삭제하는 것이면, 다른 프로젝트나 메서드 수정," +
                    " 또는 DB에 직접 쿼리를 날리는 방식으로 진행해 주시기 바랍니다.");
        }
        em.createQuery("delete Lecture l where id=:id")
                .setParameter("id", lecture.getId())
                .executeUpdate();
        em.clear();
    }
}
