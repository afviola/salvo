
package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
	    SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			PlayerRepository playerRepository,
			GameRepository gameRepository,
			GamePlayerRepository gamePlayerRepository,
            ShipRepository shipRepository,
			SalvoRepository salvoRepository,
			ScoreRepository scoreRepository) {

		return (args) -> {
			Player player1 = new Player("j.bauer@ctu.gov");
			Player player2 = new Player("c.obrian@ctu.gov");

			playerRepository.save(player1);
			playerRepository.save(player2);

			Game game1 = new Game();

			gameRepository.save(game1);

			GamePlayer gp1 = new GamePlayer(player1, game1);
			GamePlayer gp2 = new GamePlayer(player2, game1);

			gamePlayerRepository.save(gp1);
			gamePlayerRepository.save(gp2);

			shipRepository.save(new Ship("Submarine", gp1, Arrays.asList("E1", "F1", "G1")));
			shipRepository.save(new Ship("Patrol Boat", gp1, Arrays.asList("B4", "B5")));
			shipRepository.save(new Ship("Destroyer", gp2, Arrays.asList("B5", "C5", "D5")));
			shipRepository.save(new Ship("Patrol Boat", gp2, Arrays.asList("C6", "C7")));

			salvoRepository.save(new Salvo(gp1, 1, Arrays.asList("A1", "H8", "G5")));
			salvoRepository.save(new Salvo(gp2, 2, Arrays.asList("B2", "F1", "D5")));

			scoreRepository.save(new Score(player1, game1, 1));
			scoreRepository.save(new Score(player2, game1, 0));
		};
	}
}














