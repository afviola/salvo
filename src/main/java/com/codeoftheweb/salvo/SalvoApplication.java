
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
			SalvoRepository salvoRepository) {

		return (args) -> {
			Player player1 = new Player("j.bauer@ctu.gov");
			Player player2 = new Player("c.obrian@ctu.gov");
			Player player3 = new Player("kim_bauer@gmail.com");
			Player player4 = new Player("t.almeida@ctu.gov");

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);

			Game game1 = new Game();
			Game game2 = new Game();

			gameRepository.save(game1);
			gameRepository.save(game2);

			GamePlayer gp1 = new GamePlayer(player1, game1);
			GamePlayer gp2 = new GamePlayer(player2, game1);
			GamePlayer gp3 = new GamePlayer(player3, game2);
			GamePlayer gp4 = new GamePlayer(player4, game2);

			gamePlayerRepository.save(gp1);
			gamePlayerRepository.save(gp2);
			gamePlayerRepository.save(gp3);
			gamePlayerRepository.save(gp4);

			//game 1
			shipRepository.save(new Ship("Submarine", gp1, Arrays.asList("E1", "F1", "G1")));
			shipRepository.save(new Ship("Patrol Boat", gp1, Arrays.asList("B4", "B5")));
			shipRepository.save(new Ship("Destroyer", gp2, Arrays.asList("B5", "C5", "D5")));
			shipRepository.save(new Ship("Patrol Boat", gp2, Arrays.asList("C6", "C7")));

			//game2
			shipRepository.save(new Ship("Destroyer", gp3, Arrays.asList("A1", "A2", "A3")));
			shipRepository.save(new Ship("Submarine", gp3, Arrays.asList("B1", "C1", "D1")));
			shipRepository.save(new Ship("Patrol Boat", gp4, Arrays.asList("H7", "H8")));
			shipRepository.save(new Ship("Patrol Boat", gp4, Arrays.asList("A4", "A5")));

			Salvo salvo1 = new Salvo(gp1, 1, Arrays.asList("A1", "H8", "G5"));
			Salvo salvo2 = new Salvo(gp2, 2, Arrays.asList("B2", "F1", "D5"));

			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);
		};
	}
}














