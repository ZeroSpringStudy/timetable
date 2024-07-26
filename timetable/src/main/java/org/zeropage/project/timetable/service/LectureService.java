package org.zeropage.project.timetable.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeropage.project.timetable.domain.lecture.EnrolledLecture;
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
    public void saveLecture(EnrolledLecture lecture){
        lectureRepository.save(lecture);
    }

    public List<Lecture> search(SearchEnrolledLecture option){
        return lectureRepository.find(option);
    }
}
