package org.zeropage.project.timetable.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeropage.project.timetable.domain.lecture.RegisteredLecture;
import org.zeropage.project.timetable.domain.lecture.Lecture;
import org.zeropage.project.timetable.repository.LectureRepository;
import org.zeropage.project.timetable.repository.SearchEnrolledLecture;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;

    @Transactional
    public void saveLecture(RegisteredLecture lecture){
        lectureRepository.save(lecture);
    }

    public Lecture findById(long id) {
        Lecture result = lectureRepository.findOne(id);
        result.setClassHoursByList();
        return result;
    }

    public List<RegisteredLecture> search(SearchEnrolledLecture option){
        List<RegisteredLecture> result = lectureRepository.find(option);
        for (RegisteredLecture lecture : result)
            lecture.setClassHoursByList();
        return result;
    }
}
