package org.zeropage.project.timetable.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.zeropage.project.timetable.domain.lecture.Classification;
import org.zeropage.project.timetable.domain.lecture.CourseType;
import org.zeropage.project.timetable.domain.lecture.RegisteredLecture;
import org.zeropage.project.timetable.domain.lecture.Lecture;
import org.zeropage.project.timetable.repository.LectureRepository;
import org.zeropage.project.timetable.repository.SearchEnrolledLecture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * IMPORTANT: Persist all data by persistDefaultLectureData() before testing, or it will fail.
 * 주의: 테스트 전 persistDefaultLectureData() 메서드를 이용해 데이터를 넣을 것, 아니면 테스트는 실패함.
 */
@SpringBootTest
@Transactional
public class LectureServiceTest {
    @Autowired
    LectureService lectureService;
    @Autowired
    LectureRepository lectureRepository;

    @DisplayName("Search Enrolled Lecture by name.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByName(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setName("객체지향프로그래밍");
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(2,searchResult.size());
        assertSame(lectureRepository.findOne(2L),searchResult.get(0));
        assertSame(lectureRepository.findOne(3L),searchResult.get(1));
        printResult(searchResult);
    }

    @DisplayName("Search Enrolled Lecture by college.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByCollege(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setCollege("교양");
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(3,searchResult.size());
        assertSame(lectureRepository.findOne(1L),searchResult.get(0));
        assertSame(lectureRepository.findOne(4L),searchResult.get(1));
        assertSame(lectureRepository.findOne(11L),searchResult.get(2));
        printResult(searchResult);
    }

    @DisplayName("Search Enrolled Lecture by college and dept.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByCollegeAndDept(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setCollege("창의ICT공과대학");
        searchOptions.setDept("전자전기공학부");
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(2,searchResult.size());
        assertSame(lectureRepository.findOne(3L),searchResult.get(0));
        assertSame(lectureRepository.findOne(10L),searchResult.get(1));
        printResult(searchResult);
    }

    @DisplayName("Search Enrolled Lecture by lecturer.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByLecturer(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setLecturer("김민진");
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(1,searchResult.size());
        assertSame(lectureRepository.findOne(6L),searchResult.get(0));
        printResult(searchResult);
    }

    @DisplayName("Search Enrolled Lecture by lectureCode.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByLectureCode(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setLectureCode(49156);
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(2,searchResult.size());
        assertSame(lectureRepository.findOne(2L),searchResult.get(0));
        assertSame(lectureRepository.findOne(3L),searchResult.get(1));
        printResult(searchResult);
    }

    @DisplayName("Search Enrolled Lecture by credit.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByCredit(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setCredit(3F);
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(8,searchResult.size());
        assertSame(lectureRepository.findOne(2L),searchResult.get(0));
        assertSame(lectureRepository.findOne(3L),searchResult.get(1));
        assertSame(lectureRepository.findOne(4L),searchResult.get(2));
        assertSame(lectureRepository.findOne(5L),searchResult.get(3));
        assertSame(lectureRepository.findOne(8L),searchResult.get(4));
        assertSame(lectureRepository.findOne(9L),searchResult.get(5));
        assertSame(lectureRepository.findOne(10L),searchResult.get(6));
        assertSame(lectureRepository.findOne(11L),searchResult.get(7));
        printResult(searchResult);
    }

    @DisplayName("Search Enrolled Lecture by grade.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByGrade(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setGrade(2);
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(4,searchResult.size());
        assertSame(lectureRepository.findOne(2L),searchResult.get(0));
        assertSame(lectureRepository.findOne(3L),searchResult.get(1));
        assertSame(lectureRepository.findOne(8L),searchResult.get(2));
        assertSame(lectureRepository.findOne(9L),searchResult.get(3));
        printResult(searchResult);
    }

    @DisplayName("Search Enrolled Lecture by classifications.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByClassifications(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setClassification(Arrays.asList(
                Classification.MAJOR,
                Classification.REQUIRED_MAJOR
        ));
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(4,searchResult.size());
        assertSame(lectureRepository.findOne(2L),searchResult.get(0));
        assertSame(lectureRepository.findOne(3L),searchResult.get(1));
        assertSame(lectureRepository.findOne(8L),searchResult.get(2));
        assertSame(lectureRepository.findOne(9L),searchResult.get(3));
        printResult(searchResult);
    }

    @DisplayName("Search Enrolled Lecture by dept and name.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByDeptAndName(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setCollege("소프트웨어대학");
        searchOptions.setDept("소프트웨어학부");
        searchOptions.setName("객체지향프로그래밍");
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(1,searchResult.size());
        assertSame(lectureRepository.findOne(2L),searchResult.get(0));
        printResult(searchResult);
    }

    @DisplayName("Search Enrolled Lecture by classification and lecturer.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByClassificationAndLecturer(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setClassification(List.of(Classification.MAJOR));
        searchOptions.setLecturer("김수진");
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(1,searchResult.size());
        assertSame(lectureRepository.findOne(8L),searchResult.get(0));
        printResult(searchResult);
    }

    @DisplayName("Search Enrolled Lecture by classification and lecturer.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByClassificationAndLectureCodeAndGrade(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setClassification(Arrays.asList(
                Classification.MAJOR,
                Classification.REQUIRED_MAJOR,
                Classification.TEACHING_MAJOR,
                Classification.ELECTIVE_MAJOR,
                Classification.BASIC_MAJOR
        ));
        searchOptions.setLectureCode(49156);
        searchOptions.setGrade(2);
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(2,searchResult.size());
        assertSame(lectureRepository.findOne(2L),searchResult.get(0));
        assertSame(lectureRepository.findOne(3L),searchResult.get(1));
        printResult(searchResult);
    }

    @DisplayName("Search Enrolled Lecture by all values.")
    @Test
    @Rollback(value = false)
    void searchEnrolledLectureByAllValues(){
        SearchEnrolledLecture searchOptions = new SearchEnrolledLecture();
        searchOptions.setName("CAU");
        searchOptions.setCollege("사범대학(2011)");
        searchOptions.setDept("유아교육과");
        searchOptions.setLecturer("김");
        searchOptions.setLectureCode(51097);
        searchOptions.setCredit(0.5F);
        searchOptions.setGrade(0);
        searchOptions.setClassification(Arrays.asList(
                Classification.MAJOR,
                Classification.REQUIRED_MAJOR,
                Classification.TEACHING_MAJOR,
                Classification.BASIC_MAJOR,
                Classification.ELECTIVE
        ));
        List<Lecture> searchResult = lectureService.search(searchOptions);
        assertEquals(1,searchResult.size());
        assertSame(lectureRepository.findOne(6L),searchResult.get(0));
        printResult(searchResult);
    }

    static void printResult(List<Lecture> result){
        for(Lecture lecture:result){
            System.out.println(lecture);
        }
    }

    /**
     * Save lecture data to test... if there is no data.
     * 테스트를 위해 데이터를 저장함. 다만 이미 데이터가 있다면 의미 없음
     */
    private void persistDefaultLectureData(){
        List<RegisteredLecture> defaultLectures = new ArrayList<>();

        defaultLectures.add(new RegisteredLecture(
                "앙트레프레너십시대의회계",
                "114,115,116,117", // Wed 09:00~11:00
                2F, 1,
                "교양",
                "공통교양",
                52533, 11,
                Classification.GENERAL_EDUCATION,
                CourseType.NONE,
                "박인선", "310관 726호 <강의실>",
                "공통교양 전문교양/소프트웨어대학 1학년/정정기간 자자:공학계열 전체 학년[[수강대상 : 소프트웨어학부1년 AI학과1년]]"
        )); //1

        defaultLectures.add(new RegisteredLecture(
                "객체지향프로그래밍 (영어A강의)",
                "22,23,24,25,118,119", // Mon 11:00~13:00, Wed 11:00~12:00
                3F, 2,
                "소프트웨어대학",
                "소프트웨어학부",
                49156, 1,
                Classification.MAJOR,
                CourseType.NONE,
                "손봉수", "310관 728호 <강의실>",
                "공학주제(설계2), 코드쉐어(전전,융합)"
        )); //2

        defaultLectures.add(new RegisteredLecture(
                "객체지향프로그래밍",
                "162,163,164,165,166,167", // Thu 09:00~12:00
                3F, 2,
                "창의ICT공과대학",
                "전자전기공학부",
                49156, 3,
                Classification.MAJOR,
                CourseType.NONE,
                "권금철", "310관 619호 <강의실>",
                "MSC,코드쉐어(소프트,융합)"
        )); //3

        defaultLectures.add(new RegisteredLecture(
                "인간행동과심리",
                "24,25,26,27,28,29", // Mon 12:00~15:00
                3F, 0,
                "교양",
                "핵심-신뢰",
                51382, 1,
                Classification.GENERAL_EDUCATION,
                CourseType.MIXED,
                "구재선", "00 000000",
                "핵심-신뢰"
        )); //4

        defaultLectures.add(new RegisteredLecture(
                "국어교과교재연구및지도법",
                "24,25,26,27,28,29", // Mon 12:00~15:00
                3F, 3,
                "인문대학(2011)",
                "국어국문학과",
                49933, 1,
                Classification.TEACHING_MAJOR,
                CourseType.NONE,
                "이은미", "203관(서라벌홀) 513호 <강의실>",
                ""
        )); //5

        defaultLectures.add(new RegisteredLecture(
                "CAU세미나(2)",
                "76,77", // Tue 14:00~15:00
                0.5F, 0,
                "사범대학(2011)",
                "유아교육과",
                51097, 1,
                Classification.ELECTIVE,
                CourseType.NONE,
                "김민진", "203관(서라벌홀) 405호 <강의실>",
                ""
        )); //6

        defaultLectures.add(new RegisteredLecture(
                "학교폭력예방및학생의이해",
                "178,179,180,181", // Thu 17:00~19:00
                2F, 0,
                "사범대학(2011)",
                "유아교육과",
                52371, 2,
                Classification.TEACHER_CERTIFICATE,
                CourseType.NONE,
                "우지향", "303관(법학관) 703호 <첨단강의실>",
                "교직소양(13학번 이후)"
        )); //7

        defaultLectures.add(new RegisteredLecture(
                "게임이론",
                "24,25,26,120,121,122", // Mon 12:00~13:30, Wed 12:00~13:30
                3F, 2,
                "경영경제대학(2011)",
                "경제학부(서울)",
                56798, 1,
                Classification.MAJOR,
                CourseType.VIDEO,
                "김수진", "00 000000",
                ""
        )); //8

        defaultLectures.add(new RegisteredLecture(
                "관리회계 (영어A강의)",
                "81,82,83,177,178,179", // Tue 16:30~18:00, Thu 16:30~18:00
                3F, 2,
                "경영경제대학(2011)",
                "경영학부(서울)",
                2578, 3,
                Classification.REQUIRED_MAJOR,
                CourseType.LIVE_VIDEO,
                "현정훈", "00 000000",
                ""
        )); //9

        defaultLectures.add(new RegisteredLecture(
                "공업수학 (영어A강의)",
                "162,163,164,165,68,69", // Tue 10:00~11:00, Thu 09:00~11:00
                3F, 1,
                "창의ICT공과대학",
                "전자전기공학부",
                41144, 4,
                Classification.BASIC_MAJOR,
                CourseType.BLENDED,
                "전웅선", "207관(봅스트홀) 234호 <강의실>, 310관 616호 <강의실>",
                "MSC"
        )); //10

        defaultLectures.add(new RegisteredLecture(
                "신화적상상력과판타지",
                "280,281", // Sat 20:00~21:00
                3F, 0,
                "교양",
                "선택-창의",
                56422, 2,
                Classification.GENERAL_EDUCATION,
                CourseType.VIDEO,
                "이명현", "00 000000",
                "선택-창의 지식경영학부 대상/정정기간에는 통합여석 적용 예정/동영상100%(원격수업 고정운영)[[수강대상 : 지식경영학부]]"
        )); //11

        defaultLectures.add(new RegisteredLecture(
                "의약품설계합성학 (영어A강의)",
                "34,35,36,37", // Mon 20:00~21:00
                2F, 3,
                "약학대학(2011)",
                "약학부",
                41302, 1,
                Classification.ELECTIVE_MAJOR,
                CourseType.NONE,
                "오경수", "102관(약학대학 및 R&D센터) 206호 <강의실>",
                "자과생만 수강가능(4,5학년 수강)"
        )); //12

        for(RegisteredLecture lecture:defaultLectures){
            lectureRepository.save(lecture);
        }
    }
}