
package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
	    SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository) {
		return (args) -> {
			playerRepository.save(new Player("player1@ex.com"));
			playerRepository.save(new Player("player2@ex.com"));
			playerRepository.save(new Player("player3@ex.com"));

			Instant currentDate = Instant.now();

			gameRepository.save(new Game(Date.from(currentDate)));
			gameRepository.save(new Game(Date.from(currentDate.plusSeconds(3600))));
			gameRepository.save(new Game(Date.from(currentDate.plusSeconds(3600 * 2))));
			gameRepository.save(new Game(Date.from(currentDate.plusSeconds(3600 * 3))));
			gameRepository.save(new Game(Date.from(currentDate.plusSeconds(3600 * 4))));
		};
	}
}














