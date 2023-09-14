package com.ekart;

import com.ekart.controller.AuthController;
import com.ekart.dto.request.RegisterRequest;
import com.ekart.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static com.ekart.model.RoleType.ADMIN;

@SpringBootApplication

@ComponentScan(basePackages = {"com.ekart"})
public class EkartApplication {

	public static void main(String[] args) {
		SpringApplication.run(EkartApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthController authController
//	) {
//		return args -> {
//			var admin = RegisterRequest.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("admin@gmail.com")
//					.password("password")
//					.role(ADMIN)
//					.build();
//			System.out.println("Admin token: " + authController.adminRegister(admin).getAccessToken());
//
////			var manager = RegisterRequest.builder()
////					.firstname("Admin")
////					.lastname("Admin")
////					.email("manager@mail.com")
////					.password("password")
////					.role(MANAGER)
////					.build();
////			System.out.println("Manager token: " + service.register(manager).getAccessToken());
//
//		};
//	}
}
