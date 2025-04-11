package com._dengz.mungcourse;

import com._dengz.mungcourse.properties.GoogleOAuth2Properties;
import com._dengz.mungcourse.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, GoogleOAuth2Properties.class})
public class MungcourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(MungcourseApplication.class, args);
	}

}
