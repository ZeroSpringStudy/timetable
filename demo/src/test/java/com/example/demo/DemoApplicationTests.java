package com.example.demo;

import com.example.demo.service.GetLectureFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	GetLectureFile getLectureFile;

	@Test
	void lectureGetTest() {
		getLectureFile.get("", "",
				"");
	}
}