package com.blb;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.blb.mapper")
public class ApplicationHome {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationHome.class, args);
	}

}
