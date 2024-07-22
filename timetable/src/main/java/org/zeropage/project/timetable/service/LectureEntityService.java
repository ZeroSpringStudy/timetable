package org.zeropage.project.timetable.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeropage.project.timetable.domain.lecture.CustomLectureEntity;
import org.zeropage.project.timetable.domain.lecture.EnrolledLecture;
import org.zeropage.project.timetable.domain.lecture.LectureEntity;
import org.zeropage.project.timetable.repository.LectureEntityRepository;
import org.zeropage.project.timetable.wizardoptions.CustomLecture;
import org.zeropage.project.timetable.repository.SearchEnrolledLecture;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureEntityService {
    private final LectureEntityRepository lectureEntityRepository;

    @Transactional
    public void saveLecture(CustomLecture lecture){
        CustomLectureEntity lectureEntity =
                new CustomLectureEntity(null, lecture.getName(), lecture.getClassHours());
        lectureEntityRepository.save(lectureEntity);
    }

    /**
     * Test only. Not for real use.
     */
    @Transactional
    public void saveLecture(EnrolledLecture lecture){
        lectureEntityRepository.save(lecture);
    }

    public List<LectureEntity> search(SearchEnrolledLecture option){
        return lectureEntityRepository.find(option);
    }
}
