import codecs
import glob
import os
import sys
import time
import warnings
from datetime import datetime

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select

# 기본 설정 (포탈 ID, PW 입력 필수, 파일 다운로드 경로는 절대경로로 입력해야 함)
userID = ""
userPW = ""
downloadPath = r""
originalFilepath = os.path.join(downloadPath, "noMsg.xlsx")
# ===============================================================

sys.stdout = codecs.getwriter("utf-8")(sys.stdout.detach())
print("Content-type: text/html;charset=utf-8\r\n")

warnings.filterwarnings("ignore")
now = datetime.now()
url = "https://mportal.cau.ac.kr/std/usk/sUskSif001/index.do?type=1"

options = webdriver.ChromeOptions()
options.add_experimental_option("prefs", {
    "download.default_directory": downloadPath,
    "download.prompt_for_download": False
})
driver = webdriver.Chrome(options=options)

driver.get(url)
time.sleep(0.5)
driver.find_element(By.CSS_SELECTOR, "#txtUserID").send_keys(userID)
driver.find_element(By.CSS_SELECTOR, "#txtPwd").send_keys(userPW)
driver.find_element(By.CSS_SELECTOR,
                    "#form1 > div > div > div.login-wrap > a").click()
time.sleep(1)
Select(driver.find_element(By.CSS_SELECTOR, "#sel_year")).select_by_value("2024")
Select(driver.find_element(By.CSS_SELECTOR, "#sel_shtm")).select_by_value("2")
Select(driver.find_element(By.CSS_SELECTOR, "#sel_camp")).select_by_value("1")
time.sleep(0.3)
colleges = [option for option in Select(driver.find_element(By.CSS_SELECTOR, "#sel_colg")).options
            if option.text != "" and option.get_attribute("value") != ""][1:]
for college in colleges:
    Select(driver.find_element(By.CSS_SELECTOR, "#sel_colg")) \
        .select_by_value(college.get_attribute("value"))
    time.sleep(0.3)
    depts = [option for option in Select(driver.find_element(By.CSS_SELECTOR, "#sel_sust")).options
             ][1:]
    for dept in depts:
        Select(driver.find_element(By.CSS_SELECTOR, "#sel_sust")) \
            .select_by_value(dept.get_attribute("value"))
        driver.find_element(By.CSS_SELECTOR,
                            "#nbContext > div.nb-contents > div > div.BO_system.nb-q.ng-scope > section.nb-search > "
                            "div.nb-search-submit > div > div > button"
                            ).click()
        time.sleep(2)

        # 없어진 학부를 지우기 위해 교양, 교직만 있거나 코드셰어 과목만 있다면 교양 tab이 아닌 이상 지움
        lectures = driver.find_elements(By.CSS_SELECTOR,
                                        "#nbContext > div.nb-contents > div > div.BO_system.nb-q.ng-scope > "
                                        "section.section-gap > div > div > div > div.sp-grid-body > div > div")
        toSave = False
        for lecture in lectures:
            classification = lecture.find_element(By.CSS_SELECTOR,
                                                  "div:nth-child(5) > div.sp-grid-data.text-center > span > span").text
            remark = lecture.find_element(By.CSS_SELECTOR,
                                          "div:nth-child(13) > div.sp-grid-data.text-center > span > span").text
            if (classification != "교양" and classification != "교직" and
                remark != "경영경제대학 재학생 및 다전공생외 수강 불가" and remark != "공대 코드쉐어 과목"
                    # 코드셰어 과목은 임시조치로 때움, 비고란으로 잡아내기 때문에 비고 내용 변경되면 추적이 안됨
            ) or college == colleges[0]:
                toSave = True
                break

        if toSave:
            print(f"{college.text} {dept.text}: {len(lectures)}")
            driver.find_element(By.CSS_SELECTOR, "#nbContext > div.nb-contents > div > div.BO_system.nb-q.ng-scope > "
                                                 "section.nb-search > div.nb-btn-default.nb-theme > div > div > "
                                                 "button").click()
            while not glob.glob(originalFilepath):
                nothing = "to do"  # 파일 다운로드 대기
            os.rename(originalFilepath, os.path.join(downloadPath, f"{college.text}.{dept.text}.xlsx"))
