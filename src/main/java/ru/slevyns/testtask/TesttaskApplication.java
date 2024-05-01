package ru.slevyns.testtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//todo remove exclude={DataSourceAutoConfiguration.class} on 2 task
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class TesttaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TesttaskApplication.class, args);
	}

}
