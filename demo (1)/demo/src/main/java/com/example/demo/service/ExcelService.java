package com.example.demo.service;

import com.example.demo.domain.lecture.RegisteredLecture;
import com.example.demo.repository.LectureRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;

@Service
@Transactional
public class ExcelService {

    @Autowired
    private LectureRepository lectureRepository;
    public void saveExcelData(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        while (rows.hasNext()) {
            Row currentRow = rows.next();

            if (currentRow.getRowNum() == 0) { // 헤더 행 건너뜀
                continue;
            }


            RegisteredLecture excelData = new RegisteredLecture();
            excelData.setCommunity(currentRow.getCell(0).getStringCellValue());
            excelData.setDepartment(currentRow.getCell(1).getStringCellValue());
            excelData.setGrade(Integer.valueOf(currentRow.getCell(2).getStringCellValue()));
            excelData.setDegree(currentRow.getCell(3).getStringCellValue());
            excelData.setLectureType(currentRow.getCell(4).getStringCellValue());

            String CodeNumTmp = currentRow.getCell(5).getStringCellValue();
            String[] CodeNumTmpList = CodeNumTmp.split("-");
            excelData.setLectureCode(CodeNumTmpList[0]);
            excelData.setLectureNum(Integer.valueOf(CodeNumTmpList[1]));

            excelData.setLectureName(currentRow.getCell(6).getStringCellValue());

            String CreditClasstimeTmp = currentRow.getCell(7).getStringCellValue();
            String[] CreditClasstimeTmpList = CreditClasstimeTmp.split("-");
            excelData.setCredit(Integer.valueOf(CreditClasstimeTmpList[0]));
            excelData.setClassTime(Integer.valueOf(CreditClasstimeTmpList[1]));
            excelData.setProfessor(currentRow.getCell(8).getStringCellValue());
            excelData.setIsClosed(currentRow.getCell(9).getStringCellValue());

            // 10 Time 넣기 로직 생각하기
            String timeTmp = currentRow.getCell(10).getStringCellValue();
            String[] timeTmpList = timeTmp.replaceAll(" ", "").split("/");
            String timeRst = "";
            for(int i=0; i<timeTmpList.length -1; i++){
                String rst = "";
                String[] tmp = timeTmpList[i].substring(1).split(",");
                switch (timeTmpList[i].substring(0, 1)) {
                    case "월" -> {
                        for (String s : tmp) {
                            rst += s;
                            rst += " ";
                        }
                    }
                    case "화" -> {
                        for (String s : tmp) {
                            int cnt = Integer.parseInt(s);
                            cnt += 10;
                            rst = rst + cnt;
                            rst += " ";
                        }
                    }
                    case "수" -> {
                        for (String s : tmp) {
                            int cnt = Integer.parseInt(s);
                            cnt += 20;
                            rst = rst + cnt;
                            rst += " ";
                        }
                    }
                    case "목" -> {
                        for (String s : tmp) {
                            int cnt = Integer.parseInt(s);
                            cnt += 30;
                            rst = rst + cnt;
                            rst += " ";
                        }
                    }
                    case "금" -> {
                        for (String s : tmp) {
                            int cnt = Integer.parseInt(s);
                            cnt += 40;
                            rst = rst + cnt;
                            rst += " ";
                        }
                    }
                }
                timeRst += rst;
            }
            excelData.setLectureTime(timeRst);




            excelData.setIsFlexible(currentRow.getCell(11).getStringCellValue());
            excelData.setEtc(currentRow.getCell(12).getStringCellValue());
            excelData.setIsVideo(currentRow.getCell(13).getStringCellValue());

            //excelDataRepository.save(excelData);
            lectureRepository.save(excelData);
        }

        workbook.close();
    }
}