package org.zeropage.project.timetable.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.zeropage.project.timetable.domain.lecture.Classification;
import org.zeropage.project.timetable.domain.lecture.CourseType;
import org.zeropage.project.timetable.domain.lecture.EnrolledLecture;
import org.zeropage.project.timetable.domain.lecture.LectureEntity;
import org.zeropage.project.timetable.repository.LectureEntityRepository;
import org.zeropage.project.timetable.repository.SearchEnrolledLecture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
@Transactional
public class LectureEntityServiceTest {
    @Autowired
    LectureEntityService lectureEntityService;
    @Autowired
    LectureEntityRepository lectureEntityRepository;

    /**
     * Search Enrolled Lecture by name.
     */
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByName(){
        persistDefaultLectureData();
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setName("객체지향프로그래밍");
        List<LectureEntity> searchResult = lectureEntityService.search(searchOptions);
        assertEquals(2,searchResult.size());
        assertSame(lectureEntityRepository.findOne(2L),searchResult.get(0));
        assertSame(lectureEntityRepository.findOne(3L),searchResult.get(1));
        System.out.println(searchResult.get(0));
        System.out.println(searchResult.get(1));
    }

    private void persistDefaultLectureData(){
        List<EnrolledLecture> defaultLectures = new ArrayList<>();

        defaultLectures.add(new EnrolledLecture(
                "앙트레프레너십시대의회계",
                Arrays.asList(114,115,116,117), // Wed 09:00~11:00
                2F, 1,
                "교양",
                "공통교양",
                52533, 11,
                Classification.GENERAL_EDUCATION,
                CourseType.NONE,
                "박인선",
                "공통교양 전문교양/소프트웨어대학 1학년/정정기간 자자:공학계열 전체 학년[[수강대상 : 소프트웨어학부1년 AI학과1년]]"
        ));

        defaultLectures.add(new EnrolledLecture(
                "객체지향프로그래밍 (영어A강의)",
                Arrays.asList(22,23,24,25,118,119), // Mon 11:00~13:00, Wed 11:00~12:00
                3F, 2,
                "소프트웨어대학",
                "소프트웨어학부",
                49156, 1,
                Classification.MAJOR,
                CourseType.NONE,
                "손봉수",
                "공학주제(설계2), 코드쉐어(전전,융합)"
        ));

        defaultLectures.add(new EnrolledLecture(
                "객체지향프로그래밍",
                Arrays.asList(162,163,164,165,166,167), // Thu 09:00~12:00
                3F, 2,
                "창의ICT공과대학",
                "전자전기공학부",
                49156, 3,
                Classification.MAJOR,
                CourseType.NONE,
                "권금철",
                "MSC,코드쉐어(소프트,융합)"
        ));

        defaultLectures.add(new EnrolledLecture(
                "인간행동과심리",
                Arrays.asList(24,25,26,27,28,29), // Mon 12:00~15:00
                3F, 0,
                "교양",
                "핵심-신뢰",
                51382, 1,
                Classification.GENERAL_EDUCATION,
                CourseType.MIXED,
                "구재선",
                "핵심-신뢰"
        ));

        defaultLectures.add(new EnrolledLecture(
                "국어교과교재연구및지도법",
                Arrays.asList(24,25,26,27,28,29), // Mon 12:00~15:00
                3F, 3,
                "인문대학(2011)",
                "국어국문학과",
                49933, 1,
                Classification.TEACHING_MAJOR,
                CourseType.NONE,
                "이은미",
                ""
        ));

        defaultLectures.add(new EnrolledLecture(
                "CAU세미나(2)",
                Arrays.asList(76,77), // Tue 14:00~15:00
                0.5F, 0,
                "사범대학(2011)",
                "유아교육과",
                51097, 1,
                Classification.ELECTIVE,
                CourseType.NONE,
                "김민진",
                ""
        ));

        defaultLectures.add(new EnrolledLecture(
                "학교폭력예방및학생의이해",
                Arrays.asList(178,179,180,181), // Thu 17:00~19:00
                2F, 0,
                "사범대학(2011)",
                "유아교육과",
                52371, 1,
                Classification.TEACHER_CERTIFICATE,
                CourseType.NONE,
                "우지향",
                "교직소양(13학번 이후)"
        ));

        defaultLectures.add(new EnrolledLecture(
                "게임이론",
                Arrays.asList(24,25,26,120,121,122), // Mon 12:00~13:30, Wed 12:00~13:30
                3F, 2,
                "경영경제대학(2011)",
                "경제학부(서울)",
                56798, 1,
                Classification.MAJOR,
                CourseType.VIDEO,
                "김수진",
                ""
        ));

        defaultLectures.add(new EnrolledLecture(
                "관리회계 (영어A강의)",
                Arrays.asList(81,82,83,177,178,179), // Tue 16:30~18:00, Thu 16:30~18:00
                3F, 2,
                "경영경제대학(2011)",
                "경영학부(서울)",
                2578, 3,
                Classification.REQUIRED_MAJOR,
                CourseType.LIVE_VIDEO,
                "현정훈",
                ""
        ));

        defaultLectures.add(new EnrolledLecture(
                "공업수학 (영어A강의)",
                Arrays.asList(68,69,162,163,164,165), // Tue 10:00~11:00, Thu 09:00~11:00
                3F, 1,
                "창의ICT공과대학",
                "전자전기공학부",
                41144, 4,
                Classification.BASIC_MAJOR,
                CourseType.BLENDED,
                "전웅선",
                "MSC"
        ));

        for(EnrolledLecture lecture:defaultLectures){
            lectureEntityRepository.save(lecture);
        }
    }
}