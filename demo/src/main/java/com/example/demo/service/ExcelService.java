package com.example.demo.service;

import com.example.demo.domain.lecture.Classification;
import com.example.demo.domain.lecture.CourseType;
import com.example.demo.domain.lecture.RegisteredLecture;
import com.example.demo.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final LectureRepository lectureRepository;

    @Transactional
    public void saveExcelData(File file, String college, String dept) throws IOException {
        Workbook workbook = new XSSFWorkbook(new BufferedInputStream(new FileInputStream(file)));
        Sheet sheet = workbook.getSheetAt(0);

        for (Row currentRow : sheet) {
            if (currentRow.getCell(9).getStringCellValue().equals("폐강"))
                continue; //폐강된 강의는 저장하지 않음, 헤더 행도 폐강이라 적혀있어 같이 걸러짐
            //과목번호 및 분반설정, MM분반을 걸러야 하니 먼저 시행
            String CodeNumTmp = currentRow.getCell(5).getStringCellValue();
            String[] CodeNumTmpList = CodeNumTmp.split("-");
            if(CodeNumTmpList[1].equals("MM"))
                continue;

            int grade;
            try {
                grade = Integer.parseInt(currentRow.getCell(2).getStringCellValue());
            } catch (NumberFormatException e) {
                grade = 0;
            }
            // excelData.setDegree(currentRow.getCell(3).getStringCellValue());

            String classificationTypeStr = currentRow.getCell(4).getStringCellValue();
            Classification classificationType = Classification.parse(classificationTypeStr);

            int lectureCode = Integer.parseInt(CodeNumTmpList[0]);
            int lectureSection = Integer.parseInt(CodeNumTmpList[1]);

            String lectureName = currentRow.getCell(6).getStringCellValue();

            String[] CreditClasstimeTmpList = currentRow.getCell(7).getStringCellValue()
                    .split("-");
            float credit = Float.parseFloat('0' + CreditClasstimeTmpList[0]);
            // ".5" parse하면 예외가 발생할 수 있어 '0'을 앞에 붙임
            // setClassTime(Integer.valueOf(CreditClasstimeTmpList[1]));
            String lecturer = currentRow.getCell(8).getStringCellValue();

            //강의시간
            String[] timeTmpList = currentRow.getCell(10).getStringCellValue().split("/");
            for (int i = 0; i < timeTmpList.length; i++)
                timeTmpList[i] = timeTmpList[i].trim();

            String lectureTime = ","; //시간정보 String, 검색 때문에 맨 앞과 뒤 ','을 넣음
            String lecturePlace = ""; //강의실정보 String
            for (String timeTmp : timeTmpList) {
                if (timeTmp.isBlank()) continue;
                DayOfWeek day = parseDay(timeTmp.substring(0, 1));
                if (day != null) {
                    if (timeTmp.contains("~")) { // 수업시간 표시가 시간으로 이루어져 있는 것
                        String[] tmp = timeTmp.replace(" ", "").substring(1)
                                .replace("(", "")
                                .replace(")", "")
                                .split("~");
                        LocalTime startTime = LocalTime.parse(tmp[0], DateTimeFormatter.ofPattern("HH:mm"));
                        LocalTime endTime = LocalTime.parse(tmp[1], DateTimeFormatter.ofPattern("HH:mm"));
                        //정각이나 30분 단위로 끊기지 않는 상황 대비(특히 종료시간이 15분 일찍 잡혀 있다는 점 고려)
                        if (startTime.getMinute() > 0 && startTime.getMinute() < 30) {
                            startTime = startTime.withMinute(0);
                        } else if (startTime.getMinute() > 30) {
                            startTime = startTime.withMinute(30);
                        }
                        if (endTime.getMinute() > 0 && endTime.getMinute() < 30) {
                            endTime = endTime.withMinute(30);
                        } else if (endTime.getMinute() > 30) {
                            endTime = endTime.plusHours(1).withMinute(0);
                        }
                        List<Integer> timeList = new ArrayList<>();
                        int startTimeToInt = (2 * startTime.getHour());
                        if (startTime.getMinute() == 30) startTimeToInt++;
                        int endTimeToInt = (2 * endTime.getHour());
                        if (endTime.getMinute() == 30) endTimeToInt++;
                        for (int i = startTimeToInt; i < endTimeToInt; i++) timeList.add(i);
                        int addValue = getAddValue(day);
                        timeList = timeList.stream()
                                .map(t -> t + 24 * 2 * addValue)
                                .collect(Collectors.toList());
                        for (Integer time : timeList) {
                            lectureTime += time.toString() + ',';
                        }
                    } else { // 수업시간 표시가 교시 단위로 이루어져 있는 것, ',' 없어도 해당할 수 있기에 else로 설정
                        String[] tmp = timeTmp.replace(" ", "").substring(1).split(",");
                        List<Integer> timeList = new ArrayList<>();
                        for (String s : tmp) {
                            int recordedTime = (8 + Integer.parseInt(s)) * 2;
                            timeList.add(recordedTime);
                            timeList.add(recordedTime + 1);
                        }
                        int addValue = getAddValue(day);
                        timeList = timeList.stream()
                                .map(t -> t + 24 * 2 * addValue)
                                .collect(Collectors.toList());
                        for (Integer time : timeList) {
                            lectureTime += time.toString() + ',';
                        }
                    }
                } else { //강의실 정보
                    lecturePlace += timeTmp + ", ";
                }
            }
            if (lectureTime.equals(",")) lectureTime = ""; //아무것도 없는 건 별도로 처리함
            if (!lecturePlace.isEmpty())
                lecturePlace = lecturePlace.substring(0, lecturePlace.length() - 2);
            /*
            String yuyeonStr = currentRow.getCell(11).getStringCellValue();
            Integer yuyeonInfo = switch (yuyeonStr) {
                case "Term3" -> 3;
                case "Term4" -> 4;
                default -> null;
            };
             */
            String remark = currentRow.getCell(12).getStringCellValue();
            CourseType courseType = CourseType.parse(currentRow.getCell(13).getStringCellValue());

            RegisteredLecture lecture = new RegisteredLecture(lectureName, lectureTime,
                    credit, grade, college, dept, lectureCode, lectureSection, classificationType,
                    courseType, lecturer, lecturePlace, remark);
            lectureRepository.save(lecture);
        }

        workbook.close();
    }

    private DayOfWeek parseDay(String weekStr) {
        DayOfWeek result = null;
        switch (weekStr) {
            case "월" -> result = DayOfWeek.MONDAY;
            case "화" -> result = DayOfWeek.TUESDAY;
            case "수" -> result = DayOfWeek.WEDNESDAY;
            case "목" -> result = DayOfWeek.THURSDAY;
            case "금" -> result = DayOfWeek.FRIDAY;
            case "토" -> result = DayOfWeek.SATURDAY;
            case "일" -> result = DayOfWeek.SUNDAY;
        }
        return result;
    }

    private int getAddValue(DayOfWeek day) {
        return day.getValue() - 1;
    }
}