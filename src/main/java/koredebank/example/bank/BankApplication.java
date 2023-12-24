package koredebank.example.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class BankApplication {

	public static void main(String[] args) {

		SpringApplication.run(BankApplication.class, args);
		System.out.println("Finished running People's Bank App");
	}

}
