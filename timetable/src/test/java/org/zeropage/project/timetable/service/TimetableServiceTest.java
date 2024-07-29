package org.zeropage.project.timetable.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zeropage.project.timetable.domain.Member;
import org.zeropage.project.timetable.domain.Timetable;
import org.zeropage.project.timetable.domain.lecture.CustomLecture;
import org.zeropage.project.timetable.domain.lecture.Lecture;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TimetableServiceTest {
    @Autowired
    TimetableService timetableService;
    @Autowired
    LectureService lectureService;
    @Autowired
    MemberService memberService;

    @Test
    void saveAndLoadAndDeleteExceptLastPriority() {
        CustomLecture capstoneMeeting =
                new CustomLecture("캡스톤회의", List.of(232, 233, 234, 235, 236, 237));
        CustomLecture ZPMeeting = new CustomLecture("제로페이지 정모", List.of(132, 133));
        Member member = new Member("cau20232907", "비밀번호");
        memberService.save(member);
        Timetable timetable1 = new Timetable(List.of(
                lectureService.findById(3910L), //데이터베이스설계 2분반
                lectureService.findById(3906L), //객체지향프로그래밍 1분반
                lectureService.findById(3914L), //오토마타와형식언어 1분반
                lectureService.findById(3918L), //컴퓨터구조 3분반
                lectureService.findById(3924L), //프로그래밍언어론 3분반
                lectureService.findById(3946L), //컴퓨터통신 1분반
                lectureService.findById(3943L), //캡스톤디자인 7분반
                capstoneMeeting, ZPMeeting
        ), 0);
        Timetable timetable2 = new Timetable(List.of(
                lectureService.findById(3910L), //데이터베이스설계 2분반
                lectureService.findById(3906L), //객체지향프로그래밍 1분반
                lectureService.findById(3914L), //오토마타와형식언어 1분반
                lectureService.findById(3918L), //컴퓨터구조 3분반
                lectureService.findById(3924L), //프로그래밍언어론 3분반
                lectureService.findById(3946L), //컴퓨터통신 1분반
                lectureService.findById(3732L), //알고리즘 1분반
                ZPMeeting
        ), 0);
        timetableService.saveTimetable(timetable1, member);
        timetableService.saveTimetable(timetable2, member);
        assertEquals(timetable1.getId(), timetableService.findById(timetable1.getId()).getId());
        assertEquals(timetable2.getId(), timetableService.findById(timetable2.getId()).getId());
        assertEquals(timetable1.getId(), timetableService.findByKey(timetable1.getViewOnlyKey()).getId());
        assertEquals(timetable2.getId(), timetableService.findByKey(timetable2.getViewOnlyKey()).getId());

        timetableService.deleteTimetable(timetable1);
        assertThrows(NullPointerException.class, () -> {
            Lecture lecture = lectureService.findById(capstoneMeeting.getId());
            System.out.println("lecture = " + lecture);
        },"객체가 없으므로 예외가 던져져야 함");
        lectureService.findById(ZPMeeting.getId()); //이건 남아있어야 함

        timetableService.deleteTimetable(timetable2);
        assertThrows(NullPointerException.class, () -> {
            lectureService.findById(ZPMeeting.getId());
        },"이제는 객체가 없으므로 예외가 던져져야 함");

        //rollback
        memberService.delete(member);
    }

    @Test
    void TimetableOverlapTest() {
        assertThrows(IllegalStateException.class, () -> {
            new Timetable(List.of(
                    lectureService.findById(3732L), //알고리즘 1분반
                    lectureService.findById(3907L) //객체지향프로그래밍 2분반
            ), 0);
        }, "시간이 겹치므로 예외가 던져져야 함");
    }

    /* lastPriority 구현되면 그때 추가하는 게 좋을 듯함
    void TimetableSameLectureTest() {
        assertThrows(IllegalStateException.class, () -> {
            new Timetable(List.of(
                    lectureService.findById(3906L), //객체지향프로그래밍 1분반
                    lectureService.findById(3907L) //객체지향프로그래밍 2분반
            ), 0);
        }, "같은 강의이므로 예외가 던져져야 함");
    }*/
}
