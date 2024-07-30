package org.zeropage.project.timetable.wizardoptions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zeropage.project.timetable.domain.Timetable;
import org.zeropage.project.timetable.service.LectureService;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WizardTest {
    @Autowired
    LectureService lectureService;

    @Test
    void noOptionsAndOneGroupInAGroupCaseTest() {
        //이건 에타와 완전히 동일한 기능
        Wizard wizard = getOneGroupInGroupWizard();

        List<Timetable> result = wizard.result();
        printTimetable(result);
        assertEquals(131, result.size());
    }

    private Wizard getOneGroupInGroupWizard() {
        Wizard wizard = new Wizard();
        //객체지향프로그래밍 1~3분반
        wizard.addGroup();
        wizard.getGroups().get(0).getLectureGroups().get(0).addLecture(
                lectureService.findById(3906L)
        );
        wizard.getGroups().get(0).getLectureGroups().get(0).addLecture(
                lectureService.findById(3907L)
        );
        wizard.getGroups().get(0).getLectureGroups().get(0).addLecture(
                lectureService.findById(3908L)
        );

        //데이터베이스설계 1~4분반
        wizard.addGroup();
        wizard.getGroups().get(1).getLectureGroups().get(0).addLecture(
                lectureService.findById(3909L)
        );
        wizard.getGroups().get(1).getLectureGroups().get(0).addLecture(
                lectureService.findById(3910L)
        );
        wizard.getGroups().get(1).getLectureGroups().get(0).addLecture(
                lectureService.findById(3911L)
        );
        wizard.getGroups().get(1).getLectureGroups().get(0).addLecture(
                lectureService.findById(3912L)
        );

        //오토마타와형식언어 1~2분반
        wizard.addGroup();
        wizard.getGroups().get(2).getLectureGroups().get(0).addLecture(
                lectureService.findById(3914L)
        );
        wizard.getGroups().get(2).getLectureGroups().get(0).addLecture(
                lectureService.findById(3915L)
        );

        //컴퓨터구조 1~4분반
        wizard.addGroup();
        wizard.getGroups().get(3).getLectureGroups().get(0).addLecture(
                lectureService.findById(3916L)
        );
        wizard.getGroups().get(3).getLectureGroups().get(0).addLecture(
                lectureService.findById(3917L)
        );
        wizard.getGroups().get(3).getLectureGroups().get(0).addLecture(
                lectureService.findById(3918L)
        );
        wizard.getGroups().get(3).getLectureGroups().get(0).addLecture(
                lectureService.findById(3919L)
        );

        //프로그래밍언어론 1~3분반
        wizard.addGroup();
        wizard.getGroups().get(4).getLectureGroups().get(0).addLecture(
                lectureService.findById(3921L)
        );
        wizard.getGroups().get(4).getLectureGroups().get(0).addLecture(
                lectureService.findById(3922L)
        );
        wizard.getGroups().get(4).getLectureGroups().get(0).addLecture(
                lectureService.findById(3924L)
        );
        return wizard;
    }

    @Test
    void GroupsOfGroupTest() {
        Wizard wizard = getGroupsOfGroupWizard();

        List<Timetable> result = wizard.result();
        printTimetable(result);
        assertEquals(89+77+39, result.size());
    }

    private Wizard getGroupsOfGroupWizard() {
        Wizard wizard = new Wizard();
        //객체지향프로그래밍 1~3분반
        wizard.addGroup();
        wizard.getGroups().get(0).getLectureGroups().get(0).addLecture(
                lectureService.findById(3906L)
        );
        wizard.getGroups().get(0).getLectureGroups().get(0).addLecture(
                lectureService.findById(3907L)
        );
        wizard.getGroups().get(0).getLectureGroups().get(0).addLecture(
                lectureService.findById(3908L)
        );

        //데이터베이스설계 1~4분반
        wizard.getGroups().get(0).addGroup();
        wizard.getGroups().get(0).getLectureGroups().get(1).addLecture(
                lectureService.findById(3909L)
        );
        wizard.getGroups().get(0).getLectureGroups().get(1).addLecture(
                lectureService.findById(3910L)
        );
        wizard.getGroups().get(0).getLectureGroups().get(1).addLecture(
                lectureService.findById(3911L)
        );
        wizard.getGroups().get(0).getLectureGroups().get(1).addLecture(
                lectureService.findById(3912L)
        );

        //오토마타와형식언어 1~2분반
        wizard.getGroups().get(0).addGroup();
        wizard.getGroups().get(0).getLectureGroups().get(2).addLecture(
                lectureService.findById(3914L)
        );
        wizard.getGroups().get(0).getLectureGroups().get(2).addLecture(
                lectureService.findById(3915L)
        );
        wizard.getGroups().get(0).setNumOfGroups(2); //3C2

        //컴퓨터구조 1~4분반
        wizard.addGroup();
        wizard.getGroups().get(1).getLectureGroups().get(0).addLecture(
                lectureService.findById(3916L)
        );
        wizard.getGroups().get(1).getLectureGroups().get(0).addLecture(
                lectureService.findById(3917L)
        );
        wizard.getGroups().get(1).getLectureGroups().get(0).addLecture(
                lectureService.findById(3918L)
        );
        wizard.getGroups().get(1).getLectureGroups().get(0).addLecture(
                lectureService.findById(3919L)
        );

        //프로그래밍언어론 1~3분반
        wizard.addGroup();
        wizard.getGroups().get(2).getLectureGroups().get(0).addLecture(
                lectureService.findById(3921L)
        );
        wizard.getGroups().get(2).getLectureGroups().get(0).addLecture(
                lectureService.findById(3922L)
        );
        wizard.getGroups().get(2).getLectureGroups().get(0).addLecture(
                lectureService.findById(3924L)
        );
        return wizard;
    }

    @Test
    void numOfEmptyDayWithOneGroupOfGroupTest() {
        Wizard wizard = getOneGroupInGroupWizard();
        wizard.setNumOfLeastEmptyDay(1);
        assertEquals(53+4, wizard.result().size());
        wizard.setNumOfLeastEmptyDay(2);
        assertEquals(4, wizard.result().size());
        wizard.setNumOfLeastEmptyDay(3);
        List<Timetable> result = wizard.result();
        printTimetable(result);
        assertEquals(2, result.size());
        wizard.setNumOfLeastEmptyDay(4);
        assertEquals(0, wizard.result().size());
        wizard.setCalculateSaturday(true);
        assertEquals(2, wizard.result().size());
    }

    @Test
    void setEmptyDayWithVideoLectureIgnoreTest() {
        Wizard wizard = getGroupsOfGroupWizard();
        wizard.setEmptyDaysList(List.of(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        assertEquals(0, wizard.result().size());
        wizard.setExcludeVideoLecture(true);
        List<Timetable> result = wizard.result();
        printTimetable(result);
        assertEquals(3, result.size());
        wizard.setEmptyDaysList(List.of(DayOfWeek.WEDNESDAY, DayOfWeek.MONDAY, DayOfWeek.FRIDAY));
        assertEquals(2, wizard.result().size());
        wizard.setEmptyDaysList(List.of(DayOfWeek.WEDNESDAY));
        wizard.setNumOfLeastEmptyDay(2);
        // 컴구 월요일, DB설계 금요일 허용, 단 둘 중 하나만 허용
        assertEquals(3+2+2, wizard.result().size());
    }

    @Test
    void startTimeAndEndTimeTest() {
        Wizard wizard = getOneGroupInGroupWizard();
        wizard.setLectureStartTime(LocalTime.of(10, 0));
        assertEquals(9, wizard.result().size());
        wizard.setLectureEndTime(LocalTime.of(18, 0));
        assertEquals(6, wizard.result().size());
        wizard.setLectureStartTime(null);
        assertEquals(94, wizard.result().size());
    }

    @Test
    void untactLectureIgnoreTest() {
        Wizard wizard = getOneGroupInGroupWizard();
        wizard.addGroup();
        wizard.getGroups().get(5).getLectureGroups().get(0).addLecture(
                lectureService.findById(378L)
        ); //음악의세계와감상(수10,11,12, 혼합형)
        wizard.getGroups().get(5).getLectureGroups().get(0).addLecture(
                lectureService.findById(376L)
        ); //생물의기원과진화(토8,9,10, 혼합형)
        wizard.getGroups().get(5).getLectureGroups().get(0).addLecture(
                lectureService.findById(374L)
        ); //문화콘텐츠인사이트(목4,5,6, 혼합형)
        wizard.setCalculateSaturday(true);
        assertEquals(328, wizard.result().size());
        wizard.setExcludeLivevideoLectureOnEmpty(true);
        wizard.setLectureEndTime(LocalTime.of(19, 30));
        assertEquals(197, wizard.result().size());
        wizard.setExcludeLivevideoLectureAll(true);
        assertEquals(328, wizard.result().size());
        wizard.setExcludeLivevideoLectureOnEmpty(false);
        assertEquals(328, wizard.result().size());

        wizard.setExcludeLivevideoLectureAll(false);
        wizard.setExcludeLivevideoLectureOnEmpty(false);
        wizard.setLectureEndTime(null);
        wizard.setCalculateSaturday(true);
        wizard.setNumOfLeastEmptyDay(4);
        assertEquals(2, wizard.result().size());
        wizard.setExcludeLivevideoLectureOnEmpty(true);
        assertEquals(6, wizard.result().size());
        wizard.setLectureEndTime(LocalTime.of(19, 30));
        assertEquals(4, wizard.result().size());
        wizard.setExcludeLivevideoLectureAll(true);
        assertEquals(6, wizard.result().size());
        wizard.setExcludeLivevideoLectureOnEmpty(false);
        assertEquals(6, wizard.result().size());
        wizard.setLectureEndTime(null);
        assertEquals(6, wizard.result().size());
    }

    private void printTimetable(List<Timetable> timetables) {
        for (Timetable timetable : timetables) {
            System.out.println(timetable);
        }
    }
}
