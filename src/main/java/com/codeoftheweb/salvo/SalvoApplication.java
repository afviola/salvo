
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
            ShipRepository shipRepository) {
		return (args) -> {
			Player player1 = new Player("player1@ex.com");
			Player player2 = new Player("player2@ex.com");
			Player player3 = new Player("player3@ex.com");

			Game game1 = new Game();
			Game game2 = new Game();

			GamePlayer gp1 = new GamePlayer(player1, game1);
			GamePlayer gp2 = new GamePlayer(player2, game1);
			GamePlayer gp3 = new GamePlayer(player2, game2);
			GamePlayer gp4 = new GamePlayer(player3, game2);

            Ship destroyer1 = new Ship(gp1, "Destroyer", Arrays.asList("A1","B2","C3"));
            Ship patrolBoat = new Ship(gp1, "Patrol Boat", Arrays.asList("H1","H2"));

            gp1.addShip(destroyer1);
            gp1.addShip(patrolBoat);

            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);

            gameRepository.save(game1);
            gameRepository.save(game2);

            shipRepository.save(destroyer1);
            shipRepository.save(patrolBoat);

            gamePlayerRepository.save(gp1);
            gamePlayerRepository.save(gp2);
            gamePlayerRepository.save(gp3);
            gamePlayerRepository.save(gp4);
		};
	}
}














