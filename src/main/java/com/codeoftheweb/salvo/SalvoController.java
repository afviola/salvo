package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/leaderboard")
    public List<Object> getLeaderboard() {
        return playerRepository
                .findAll()
                .stream()
                .map(player -> player.toGameHistory())
                .collect(Collectors.toList());
    }

    @RequestMapping("/games")
    public List<Object> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> game.toDto())
                .collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> getGamePlayerViewById(@PathVariable long gamePlayerId) {
        Map<String, Object> gameView = new LinkedHashMap<>();
        GamePlayer currentGamePlayer = gamePlayerRepository.getOne(gamePlayerId);

        gameView.put("id", currentGamePlayer.getGame().getId());
        gameView.put("creationDate", currentGamePlayer.getCreationDate());
        gameView.put("gamePlayers", currentGamePlayer
                .getGame()
                .getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayer.toDto())
                .collect(Collectors.toList()));

        gameView.put("ships", currentGamePlayer
                .getShips()
                .stream()
                .map(ship -> ship.toDto())
                .collect(Collectors.toList()));

        gameView.put("salvoes", currentGamePlayer
                .getGame()
                .getGamePlayers()
                .stream()
                .flatMap(gp -> gp.getSalvoes().stream().map(salvo -> salvo.toDto())));

        return gameView;
    }
}









