package com.commit.viewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ViewerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViewerApplication.class, args);
		System.out.println("AYAKTAYIIIIIIIIMMMMMM");
	}

}
