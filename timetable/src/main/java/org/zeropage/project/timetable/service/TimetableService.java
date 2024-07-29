package org.zeropage.project.timetable.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeropage.project.timetable.domain.LectureTimetable;
import org.zeropage.project.timetable.domain.Member;
import org.zeropage.project.timetable.domain.Timetable;
import org.zeropage.project.timetable.domain.lecture.Lecture;
import org.zeropage.project.timetable.repository.LectureRepository;
import org.zeropage.project.timetable.repository.TimetableRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final LectureRepository lectureRepository;

    @Transactional
    public void saveTimetable(Timetable timetable, Member member) {
        for (LectureTimetable lectureTimetable : timetable.getLectures()) {
            Lecture lecture = lectureTimetable.getLecture();
            if (lecture.getId() == null) {
                lecture.setClassHours();
                lectureRepository.save(lecture);
            }
        }
        timetable.setMember(member);
        //viewOnlyKey 설정
        String randomAlphabetic;
        do {
            randomAlphabetic = RandomStringUtils.randomAlphabetic(Timetable.viewOnlyKeyLen - 1);
        } while (!timetableRepository.isViewOnlyKeyUseable(randomAlphabetic));
        timetable.setViewOnlyKey(randomAlphabetic);
        //cascade 처리로 인해 lectureTimetable은 상관하지 않아도 됨
        timetableRepository.save(timetable);
    }

    @Transactional
    public void deleteTimetable(Timetable timetable) {
        // cascade 처리로 인해 lectureTimetable은 상관하지 않아도 됨
        // 단 CustomLecture 삭제는 생각해야 함
        List<LectureTimetable> lectureTimetables = timetable.getLectures();
        ArrayList<Lecture> lecturesToDelete = new ArrayList<>();
        for (LectureTimetable lectureTimetable : lectureTimetables) {
            Lecture lecture = lectureTimetable.getLecture();
            if (lectureRepository.isLectureRemoveable(lecture))
                lecturesToDelete.add(lecture);
            //timetable 지우기 전에 lecture를 먼저 지우면 FK문제로 예외가 발생할 것을 대비해
            //lecture 삭제는 timetable, LectureTimetable 삭제 이후에 진행
        }
        timetableRepository.remove(timetable);
        for (Lecture lecture : lecturesToDelete)
            lectureRepository.remove(lecture);
    }

    public Timetable findById(Long id) {
        Timetable timetable = timetableRepository.findOne(id);
        for (LectureTimetable lectureTimetable : timetable.getLectures())
            lectureTimetable.getLecture().setClassHoursByList();
        return timetable;
    }

    public Timetable findByKey(String key) {
        Timetable timetable = timetableRepository.findByViewOnlyKey(key);
        for (LectureTimetable lectureTimetable : timetable.getLectures())
            lectureTimetable.getLecture().setClassHoursByList();
        return timetable;
    }
}
