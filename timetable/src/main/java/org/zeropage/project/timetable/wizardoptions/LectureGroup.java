package org.zeropage.project.timetable.wizardoptions;

import lombok.Data;
import org.zeropage.project.timetable.domain.Timetable;
import org.zeropage.project.timetable.domain.lecture.CustomLecture;
import org.zeropage.project.timetable.domain.lecture.Lecture;
import org.zeropage.project.timetable.domain.lecture.RegisteredLecture;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Group of lectures. Uses in wizard.
 * 강의의 집합. 시간표 마법사에 사용.
 */
@Data
public class LectureGroup {
    private String name;
    private List<Lecture> lectureList = new ArrayList<>();
    private Integer numOfLecturePicks;

    public void addLecture(Lecture lecture){
        lectureList.add(lecture);
    }

    public void deleteLecture(Lecture lecture){
        lectureList.remove(lecture);
    }

    void calculateResult(Wizard options, List<Timetable> result,
                                  List<Lecture> currentLectureList,
                                  List<LectureGroup> nextGroups) {
        calculateResult(options, 0, numOfLecturePicks,
                currentLectureList, result, nextGroups);
    }

    void calculateResult(Wizard options, int startIndex, int numOfLeftGroup,
                         List<Lecture> currentTimetableLectureList,
                         List<Timetable> result, List<LectureGroup> remainingGroups) {
        for (int i = startIndex; i < lectureList.size() - numOfLeftGroup; i++) {
            Lecture targetLecture = lectureList.get(i);

            //강의 추가 가능한지(다른 강의와 겹치거나 option에 맞지 않는 게 있는지) 확인
            //강의 추가 불가시 continue

            //options 확인 및 overlap 점검
            boolean isNotAbleToAdd = false;
            for (Lecture lecture : currentTimetableLectureList) {
                if (targetLecture.isClassOverlaped(lecture)) {
                    isNotAbleToAdd = true;
                    break;
                }
            }
            if (isNotAbleToAdd) continue;

            if (targetLecture instanceof RegisteredLecture registeredLecture) {
                if ((!registeredLecture.getCourseType().isVideo() ||
                        !Objects.requireNonNullElse(options.getExcludeVideoLecture(),false)) &&
                        (!registeredLecture.getCourseType().isUntact() ||
                        !Objects.requireNonNullElse(options.getExcludeLivevideoLectureAll(),
                                false))) {
                    //조건에 무시되는 게 아니라면
                    isNotAbleToAdd = checkIfNotAbleToAddByTime(options, registeredLecture);
                    if (isNotAbleToAdd) continue;

                    //공강 option
                    if (!registeredLecture.getCourseType().isUntact() ||
                            Objects.requireNonNullElse(
                                    options.getExcludeLivevideoLectureOnEmpty(), false)) {
                        //공강 조건 제외가 아니면(이게 걸려 있어서 시작, 종료시간을 먼저 계산한 것)
                        isNotAbleToAdd = checkIfNotAbleToAddByEmptyDay(options, registeredLecture);
                    }
                }
            } else if (targetLecture instanceof CustomLecture) {
                isNotAbleToAdd = checkIfNotAbleToAddByTime(options, targetLecture);
                if (isNotAbleToAdd) continue;
                isNotAbleToAdd = checkIfNotAbleToAddByEmptyDay(options, targetLecture);
            }

            if(isNotAbleToAdd) continue;

            ArrayList<Lecture> newTimetableLectureList =
                    new ArrayList<>(currentTimetableLectureList);
            newTimetableLectureList.add(targetLecture);
            if (numOfLeftGroup != 0) {
                calculateResult(options, i + 1, numOfLeftGroup - 1,
                        newTimetableLectureList, result, remainingGroups);
            } else {
                if (remainingGroups.size() != 0) {
                    ArrayList<LectureGroup> nextRemainingGroups =
                            new ArrayList<>(remainingGroups);
                    LectureGroup nextGroup = nextRemainingGroups.remove(0);
                    nextGroup.calculateResult(options, result,
                            newTimetableLectureList, nextRemainingGroups);
                } else {
                    //이제 시간표 하나가 완성됨

                    //공강 날짜 수 세는 건 여기서
                    //검사 과정이 복잡하니 검사 필요없을 때는 별도로 뺌
                    if (options.getNumOfLeastEmptyDay() == 0)
                        result.add(new Timetable(newTimetableLectureList, 0));

                    int numOfEmptyDay = getNumOfEmptyDay(options, newTimetableLectureList);

                    if (numOfEmptyDay >= options.getNumOfLeastEmptyDay())
                        result.add(new Timetable(newTimetableLectureList, 0));
                }
            }
        }
    }

    private static int getNumOfEmptyDay(Wizard options, ArrayList<Lecture> newTimetableLectureList) {
        int numOfEmptyDay = 0;
        DayOfWeek endOfCount;
        if (Objects.requireNonNullElse(options.getCalculateSaturday(), false))
            endOfCount = DayOfWeek.SATURDAY;
        else endOfCount = DayOfWeek.FRIDAY;
        for (DayOfWeek day = DayOfWeek.MONDAY;
             day.getValue() <= endOfCount.getValue();
             day = day.plus(1)) {
            List<Integer> fullDay = IntStream
                    .range(24 * 2 * (day.getValue() - 1), 24 * 2 * day.getValue())
                    .boxed().collect(Collectors.toList());
            ArrayList<Integer> fullClassHours = new ArrayList<>();

            for (Lecture lecture : newTimetableLectureList) {
                if (lecture instanceof RegisteredLecture registeredLecture) {
                    if (registeredLecture.getCourseType().isVideo() &&
                            Objects.requireNonNullElse(
                                    options.getExcludeVideoLecture(),false))
                        continue;

                    else if (registeredLecture.getCourseType().isUntact() &&
                            (Objects.requireNonNullElse(
                                    options.getExcludeLivevideoLectureAll(),
                                    false) ||
                                    Objects.requireNonNullElse(
                                            options.getExcludeLivevideoLectureOnEmpty(),
                                            false)))
                        continue;

                }
                fullClassHours.addAll(lecture.getClassHoursByList());
            }

            fullDay.retainAll(fullClassHours);
            if (fullDay.size() == 0)
                numOfEmptyDay++;
        }
        return numOfEmptyDay;
    }

    private static boolean checkIfNotAbleToAddByEmptyDay(Wizard options, Lecture lecture) {
        for (DayOfWeek day : options.getEmptyLecturesList()) {
            List<Integer> fullDay = IntStream
                    .range(24 * 2 * (day.getValue() - 1), 24 * 2 * day.getValue())
                    .boxed().collect(Collectors.toList());
            fullDay.retainAll(lecture.getClassHoursByList());
            if (fullDay.size() != 0)
                return true;
        }
        return false;
    }

    private static boolean checkIfNotAbleToAddByTime
            (Wizard options, Lecture lecture) {
        if (options.getLectureStartTime() != null) { //시작 시간 조건
            for (int j = 0; j < 7; j++) {
                LocalDateTime lectureStartTime = options.getLectureStartTime();
                int lectureStartTimeForInt = lectureStartTime.getHour() * 2;
                if (lectureStartTime.getMinute() > 30) {
                    lectureStartTimeForInt += 2;
                } else if (lectureStartTime.getMinute() > 0) {
                    lectureStartTimeForInt++;
                }
                List<Integer> beforeStart = IntStream
                        .range(24 * 2 * j, 24 * 2 * j + lectureStartTimeForInt)
                        .boxed().collect(Collectors.toList());
                beforeStart.retainAll(lecture.getClassHoursByList());
                if (beforeStart.size() != 0)
                    return true;
            }
        }
        if (options.getLectureEndTime() != null) { //종료 시간 조건
            for (int j = 0; j < 7; j++) {
                LocalDateTime lectureEndTime = options.getLectureEndTime();
                int lectureEndTimeForInt = lectureEndTime.getHour() * 2;
                if (lectureEndTime.getMinute() > 30) {
                    lectureEndTimeForInt++;
                }
                List<Integer> afterEnd = IntStream
                        .range(24 * 2 * j + lectureEndTimeForInt, 24 * 2 * (j + 1))
                        .boxed().collect(Collectors.toList());
                afterEnd.retainAll(lecture.getClassHoursByList());
                if (afterEnd.size() != 0)
                    return true;
            }
        }
        return false;
    }
}