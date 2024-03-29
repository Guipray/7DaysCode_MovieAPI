package br.com.alura.dayscode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.alura")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
