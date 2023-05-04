package com.aseemahuja.chatforusms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableCaching
@SpringBootApplication
public class ChatforusApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatforusApplication.class, args);
	}
	

}
