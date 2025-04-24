package vn.dkk.medicineshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
//include >< exclude
@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
public class MedicineshopApplication {

	public static void main(String[] args) {

		SpringApplication.run(MedicineshopApplication.class, args);

	}

}
