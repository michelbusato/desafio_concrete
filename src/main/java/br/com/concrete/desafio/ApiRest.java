package br.com.concrete.desafio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(value="br.com.concrete.desafio.security")
public class ApiRest {
	public static void main(String[] args) {
		System.out.println("Inciando aplica��o...");
		SpringApplication.run(ApiRest.class, args);
		System.out.println("Aplica��o inciada com sucesso!!!");
	}
}
