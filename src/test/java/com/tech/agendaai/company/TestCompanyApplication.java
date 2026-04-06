package com.tech.agendaai.company;

import org.springframework.boot.SpringApplication;

public class TestCompanyApplication {

	public static void main(String[] args) {
		SpringApplication.from(CompanyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
