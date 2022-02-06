package com.github.aurinosalvador.anlixapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.github.aurinosalvador.anlixapi")
@EntityScan(basePackages = "com.github.aurinosalvador.anlixapi.entities")
public class AnlixApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnlixApiApplication.class, args);
	}

}
