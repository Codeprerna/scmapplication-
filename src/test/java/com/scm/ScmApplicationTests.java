package com.scm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.main.web-application-type=none",
    "spring.datasource.url=jdbc:h2:mem:testdb"
})
class ScmApplicationTests {

	@Test
	void contextLoads() {
	}

}
