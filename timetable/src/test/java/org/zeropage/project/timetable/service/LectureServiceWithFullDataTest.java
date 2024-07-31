package org.zeropage.project.timetable.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.zeropage.project.timetable.domain.lecture.Classification;
import org.zeropage.project.timetable.domain.lecture.Lecture;
import org.zeropage.project.timetable.domain.lecture.RegisteredLecture;
import org.zeropage.project.timetable.repository.LectureRepository;
import org.zeropage.project.timetable.repository.SearchEnrolledLecture;
import org.zeropage.project.timetable.repository.TimeSearchMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * 데이터가 있을 때 테스트
 */
@SpringBootTest
@Transactional(readOnly = true)
public class LectureServiceWithFullDataTest {
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private LectureService lectureService;

    @Test
    void timeSearchByIncludeTest() {
        SearchEnrolledLecture options = new SearchEnrolledLecture();
        options.setClassHours(List.of(280));
        options.setTimeSearchMode(TimeSearchMode.INCLUDE);
        List<RegisteredLecture> result = lectureRepository.find(options);
        assertEquals(2, result.size());
        assertSame(lectureRepository.findOne(508L), result.get(0));
        assertSame(lectureRepository.findOne(4837L), result.get(1));
        LectureServiceTest.printResult(result);
    }

    @Test
    void timeSearchByCoverTest() {
        SearchEnrolledLecture options = new SearchEnrolledLecture();
        options.setClassHours(List.of(280));
        options.setTimeSearchMode(TimeSearchMode.COVER);
        List<RegisteredLecture> result = lectureRepository.find(options);
        assertEquals(0, result.size());
        LectureServiceTest.printResult(result);
    }

    @Test
    void lecturePlaceSearchTest() {
        SearchEnrolledLecture options = new SearchEnrolledLecture();
        options.setLecturePlace("309관");
        List<RegisteredLecture> result = lectureRepository.find(options);
        assertEquals(9, result.size());
        assertSame(lectureRepository.findOne(877L), result.get(0));
        assertSame(lectureRepository.findOne(890L), result.get(1));
        assertSame(lectureRepository.findOne(5262L), result.get(2));
        assertSame(lectureRepository.findOne(5263L), result.get(3));
        assertSame(lectureRepository.findOne(5265L), result.get(4));
        assertSame(lectureRepository.findOne(5266L), result.get(5));
        assertSame(lectureRepository.findOne(5270L), result.get(6));
        assertSame(lectureRepository.findOne(5271L), result.get(7));
        assertSame(lectureRepository.findOne(5272L), result.get(8));
        LectureServiceTest.printResult(result);
    }

    @Test
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
        searchOptions.setLecturePlace("203관");
        searchOptions.setClassHours(List.of(76, 77, 78, 79));
        searchOptions.setTimeSearchMode(TimeSearchMode.INCLUDE);
        List<RegisteredLecture> searchResult = lectureService.search(searchOptions);
        assertEquals(1,searchResult.size());
        assertSame(lectureRepository.findOne(2537L),searchResult.get(0));
        LectureServiceTest.printResult(searchResult);
    }
}
