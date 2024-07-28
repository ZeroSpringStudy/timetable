package org.zeropage.project.timetable.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeropage.project.timetable.domain.LectureTimetable;
import org.zeropage.project.timetable.domain.Timetable;
import org.zeropage.project.timetable.domain.lecture.Lecture;
import org.zeropage.project.timetable.repository.LectureRepository;
import org.zeropage.project.timetable.repository.TimetableRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final LectureRepository lectureRepository;

    @Transactional
    public void saveTimetable(Timetable timetable) {
        for (LectureTimetable lectureTimetable : timetable.getLectures()) {
            Lecture lecture = lectureTimetable.getLecture();
            if (lecture.getId() == null) {
                lectureRepository.save(lecture);
            }
        }
        //cascade 처리로 인해 lectureTimetable은 상관하지 않아도 됨
        timetableRepository.save(timetable);
    }

    @Transactional
    public void deleteTimetable(Timetable timetable) {
        // cascade 처리로 인해 lectureTimetable은 상관하지 않아도 됨
        // 단 CustomLecture 삭제는 생각해야 함
        List<LectureTimetable> lectureTimetables = timetable.getLectures();
        for (LectureTimetable lectureTimetable : lectureTimetables) {
            Lecture lecture = lectureTimetable.getLecture();
            if (lectureRepository.getNumOfReferencedLecture(lecture) == 0)
                lectureRepository.remove(lecture);
        }
        timetableRepository.remove(timetable);
    }

    public Timetable findById(Long id) {
        return timetableRepository.findOne(id);
    }

    public Timetable findByKey(String key) {
        return timetableRepository.findByViewOnlyKey(key);
    }
}
