package com.example.demo.controller;

import com.example.demo.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@RestController

@RequestMapping("/api/db")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile multipartFile,
                                  @RequestParam("college") String college,
                                  @RequestParam("dept") String dept) {
        try {
            File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            multipartFile.transferTo(file);
            excelService.saveExcelData(file, college, dept);
            return "File uploaded and data saved successfully!";
        } catch (IOException | NullPointerException e) {
            return "An error occurred: " + e.getMessage();
        }
    }
}
