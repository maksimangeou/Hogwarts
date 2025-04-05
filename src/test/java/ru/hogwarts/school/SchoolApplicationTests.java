package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.hogwarts.school.service.InfoService;

@SpringBootTest
class SchoolApplicationTests {

	@MockBean
	private InfoService infoService;

	@Test
	void contextLoads() {
	}

}
