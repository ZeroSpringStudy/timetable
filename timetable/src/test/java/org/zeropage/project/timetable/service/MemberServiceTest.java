package org.zeropage.project.timetable.service;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.zeropage.project.timetable.domain.Member;
import org.zeropage.project.timetable.domain.Timetable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    TimetableService timetableService;
    @Autowired
    LectureService lectureService;

    @Test
    void sameUserNameTest() {
        Member member1 = new Member("cau20232907", "비밀번호");
        memberService.save(member1);
        Member member2 = new Member("cau20232907", "password");
        assertThrows(NonUniqueResultException.class, () -> {
            memberService.save(member2);
        });
        memberService.delete(member1);
    }

    @Test
    void tooLongUserNameTest() {
        Member member = new Member("a".repeat(Member.userNameLen + 1), "password");
        //길이가 null문자 제외라 정확히 맞으면 문제 없음
        assertThrows(DataIntegrityViolationException.class, () -> {
            memberService.save(member);
            memberService.delete(member);
        });
    }
/*
    @Test
    void loginSuccessTest() {
        Member member1 = new Member("cau20232907", "비밀번호");
        memberService.save(member1);
        Member member2 = new Member("cau", "비밀번호");
        memberService.save(member2);
        assertEquals(member1.getId(),
                memberService.loadUserByUsername("cau20232907","비밀번호").getId());
        memberService.delete(member1);
        memberService.delete(member2);
    }

    @Test
    void loginDeniedTest() {
        Member member1 = new Member("cau20232907", "비밀번호");
        memberService.save(member1);
        Member member2 = new Member("cau", "password");
        memberService.save(member2);
        assertThrows(NoResultException.class, () -> {
            memberService.loadUserByUsername("cau20232907", "password");
        });
        assertThrows(NoResultException.class, () -> {
            memberService.loadUserByUsername("cau20232097", "비밀번호");
        });
        assertThrows(NoResultException.class, () -> {
            memberService.loadUserByUsername("cau20232097", "password");
        });
        assertThrows(NoResultException.class, () -> {
            memberService.loadUserByUsername("cau", "비밀번호");
        });
        assertThrows(NoResultException.class, () -> {
            memberService.loadUserByUsername("cau0", "password");
        });
        memberService.delete(member1);
        memberService.delete(member2);
    }
*/
    @Test
    void seeableAfterMemberDeletedTest() {
        Member member = new Member("cau20232907", "비밀번호");
        memberService.save(member);
        Timetable timetable = new Timetable(List.of(
                lectureService.findById(3910L), //데이터베이스설계 2분반
                lectureService.findById(3906L), //객체지향프로그래밍 1분반
                lectureService.findById(3914L), //오토마타와형식언어 1분반
                lectureService.findById(3918L), //컴퓨터구조 3분반
                lectureService.findById(3924L) //프로그래밍언어론 3분반
        ), 0);
        timetableService.saveTimetable(timetable, member);

        memberService.delete(member);
        assertEquals(timetable.getId(), timetableService.findByKey(timetable.getViewOnlyKey()).getId());
        assertNull(timetableService.findByKey(timetable.getViewOnlyKey()).getMember());

        //원래는 실행할 수 없는 것, rollback을 위함
        timetableService.deleteTimetable(timetable);
    }
}
