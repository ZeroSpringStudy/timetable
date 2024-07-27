package com.example.demo.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service // @Bean 등록은 예외 발생
public class GetLectureFile {
    private final ExcelService excelService;
    private final Logger log;

    public GetLectureFile(ExcelService excelService) {
        this.excelService = excelService;
        log = LoggerFactory.getLogger(getClass());
    }

    // 포탈 ID, PW 입력 필수, 파일 다운로드 경로는 절대경로로 입력해야 함
    public void get(String userId, String userPw, String downloadPath) {
        String originalFilepath = downloadPath + "/noMsg.xlsx";

        // ChromeDriver 경로 설정
        WebDriverManager.chromedriver().setup();

        // ChromeOptions 설정
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();

        prefs.put("download.default_directory", downloadPath);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", true);

        Map<String, Object> mimeTypes = new HashMap<>();
        mimeTypes.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "savefile");
        prefs.put("profile.default_content_settings.popups", 1);
        prefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
        prefs.put("download.extensions_to_open", mimeTypes);

        options.setExperimentalOption("prefs", prefs);


        // WebDriver 생성
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // URL 열기
        String url = "https://mportal.cau.ac.kr/std/usk/sUskSif001/index.do?type=1";
        driver.get(url);

        // 로그인
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#txtUserID")))
                .sendKeys(userId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#txtPwd")))
                .sendKeys(userPw);
        driver.findElement(By.cssSelector("#form1 > div > div > div.login-wrap > a")).click();

        // 옵션 선택
        new Select(driver.findElement(By.cssSelector("#sel_year"))).selectByValue("2024");
        new Select(driver.findElement(By.cssSelector("#sel_shtm"))).selectByValue("2");
        new Select(driver.findElement(By.cssSelector("#sel_camp"))).selectByValue("1");

        // 대학 선택
        // 로딩 화면이 보이지 않을 때까지 대기
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(
                "body > div.sp-loading-wrap.none.ng-isolate-scope")));
        WebElement collegeSelect = wait.until
                (ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#sel_colg")));
        List<WebElement> colleges = collegeSelect.findElements(By.tagName("option"));
        for (int i = 1; i < colleges.size(); i++) { // 첫 번째 요소는 비어 있으므로 제외
            WebElement college = colleges.get(i);
            new Select(collegeSelect).selectByValue(college.getAttribute("value"));

            // 로딩 화면이 보이지 않을 때까지 대기
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(
                    "body > div.sp-loading-wrap.none.ng-isolate-scope")));

            // 학과 선택

            WebElement deptSelect = driver.findElement(By.cssSelector("#sel_sust"));
            List<WebElement> depts = deptSelect.findElements(By.tagName("option"));
            for (int j = 1; j < depts.size(); j++) {
                WebElement dept = depts.get(j);
                new Select(deptSelect).selectByValue(dept.getAttribute("value"));
                driver.findElement(By.cssSelector(
                        "#nbContext > div.nb-contents > div > div.BO_system.nb-q.ng-scope >" +
                                " section.nb-search > div.nb-search-submit > div > div > button"
                )).click();

                // 강의 확인
                // 로딩 화면이 보이지 않을 때까지 대기
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(
                        "body > div.sp-loading-wrap.none.ng-isolate-scope")));
                List<WebElement> lectures = driver.findElements(By.cssSelector(
                        "#nbContext > div.nb-contents > div > div.BO_system.nb-q.ng-scope >" +
                                " section.section-gap > div > div > div > div.sp-grid-body >" +
                                " div > div"
                ));

                try {
                    Thread.sleep(25); // 각종 예외로 인한 조치
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                boolean toSave = false;
                if (college == colleges.get(1) && lectures.size() != 0) toSave = true;
                else {
                    for (WebElement lecture : lectures) {
                        String classification = lecture.findElement(By.cssSelector(
                                        "div:nth-child(5) > div.sp-grid-data.text-center > span > span"))
                                .getText();
                        String remark = lecture.findElement(By.cssSelector(
                                        "div:nth-child(13) > div.sp-grid-data.text-center > span > span"))
                                .getText();
                        if (!classification.equals("교양") && !classification.equals("교직") &&
                                !remark.equals("경영경제대학 재학생 및 다전공생외 수강 불가") &&
                                !remark.equals("공대 코드쉐어 과목")) {
                            toSave = true;
                            break;
                        }
                    }
                }

                if (toSave) {
                    log.info(college.getText() + " " + dept.getText() + "의 개설 강의 수: " +
                            lectures.size());
                    driver.findElement(By.cssSelector(
                            "#nbContext > div.nb-contents > div > div.BO_system.nb-q.ng-scope >" +
                                    " section.nb-search > div.nb-btn-default.nb-theme >" +
                                    " div > div > button"
                    )).click();

                    try {
                        // 파일 다운로드 대기
                        while (!Files.exists(Paths.get(originalFilepath))) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // 파일명 변경
                        File file = new File(originalFilepath);
                        File dest = new File(downloadPath + "/" +
                                college.getText() + "." + dept.getText() + ".xlsx");
                        file.renameTo(dest);
                        excelService.saveExcelData(dest, college.getText(), dept.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
        }

        // 브라우저 닫기
        driver.quit();
    }
}
