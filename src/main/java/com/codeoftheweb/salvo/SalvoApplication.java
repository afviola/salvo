package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository) {
		return (args) -> {
			playerRepository.save(new Player("player1@ex.com"));
			playerRepository.save(new Player("player2@ex.com"));
			playerRepository.save(new Player("player3@ex.com"));
		};
	}
}














