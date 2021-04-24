package com.gosia.multiplication;

import com.gosia.multiplication.controller.LoginController;
import com.gosia.multiplication.controller.QuestionController;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class MultiplicationApplicationTests {

	@Autowired
	private LoginController loginController;

	@Autowired
	private QuestionController questionController;

	@Before
	public void init(){
		System.out.println("Start");
	}

	@Test
	void contextLoads() {
		Assertions.assertThat(loginController).isNotNull();
		Assertions.assertThat(questionController).isNotNull();
	}

	@After
	public void done(){
		System.out.println("Koniec");
	}
}
