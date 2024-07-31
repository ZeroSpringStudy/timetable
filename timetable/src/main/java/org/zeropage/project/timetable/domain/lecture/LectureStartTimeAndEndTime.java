package org.zeropage.project.timetable.domain.lecture;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * DTO로 쓸 예정이라 추가
 */
@AllArgsConstructor
@Data
public class LectureStartTimeAndEndTime implements Comparable<LectureStartTimeAndEndTime> {
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * 쓴 만큼 인자로 받은 List에서 지우기 때문에 미리 deepcopy를 진행할 필요가 있음
     */
    LectureStartTimeAndEndTime(List<Integer> classHours) {
        ArrayList<Integer> targetClassHours = new ArrayList<>();
        do {
            targetClassHours.add(classHours.remove(0));
        } while (classHours.size() != 0 &&
                classHours.get(0) % (24 * 2) != 0 && //날짜가 바뀌는 경우 제외
                targetClassHours.get(targetClassHours.size() - 1) + 1 == classHours.get(0));
        int startVal = targetClassHours.get(0);
        int dayVal = startVal / (24 * 2) + 1;
        this.day = DayOfWeek.of(dayVal);
        startVal %= (24 * 2);
        this.startTime = LocalTime.of(startVal / 2, 0);
        if (startVal % 2 == 1) {
            this.startTime = startTime.withMinute(30);
        }
        int endVal = targetClassHours.get(targetClassHours.size() - 1);
        endVal %= (24 * 2);
        this.endTime = LocalTime.of(endVal / 2, 0);
        if (endVal % 2 == 1) {
            this.endTime = endTime.withMinute(30);
        }
    }

    public List<Integer> toIntegerArray() {
        int startVal = (day.getValue() - 1) * 24 * 2;
        int endVal = startVal;
        startVal += startTime.getHour() * 2;
        if (startTime.getMinute() > 30) {
            startVal++;
        }
        if (endTime.getMinute() > 30) {
            endVal += 2;
        } else if (endTime.getMinute() > 0) {
            endVal++;
        }
        return IntStream.range(startVal, endVal).boxed().collect(Collectors.toList());
    }

    @Override
    public int compareTo(LectureStartTimeAndEndTime o) {
        if (!this.day.equals(o.day)) {
            return this.day.compareTo(o.day);
        }
        else if (!this.startTime.equals(o.startTime)) {
            return this.startTime.compareTo(o.startTime);
        }
        else if (!this.endTime.equals(o.endTime)) {
            return this.endTime.compareTo(o.endTime);
        }
        else return 0;
    }

    @Override
    public String toString() {
        String dayVal = switch (day) {
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
            case SUNDAY -> "일";
        };
        return dayVal + '(' + startTime.toString() + '~' + endTime.toString() + ')';
    }
}
