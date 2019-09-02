package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/games")
    public List<Object> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeGameDto(game))
                .collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> getGamePlayerViewById(@PathVariable long gamePlayerId) {
        Map<String, Object> gameView = new LinkedHashMap<>();

        GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerId);

        /*
        gameView.put("id", gamePlayer.getGame().getId());
        gameView.put("creationDate", gamePlayer.getGame().getCreationDate());
        */

        return gameView;
    }

    private Map<String, Object> makeGameDto(Game game) {
        Map<String, Object> gameDto = new LinkedHashMap<>();
        gameDto.put("id", game.getId());
        gameDto.put("created", game.getCreationDate().toLocaleString());

        List<Map<String, Object>> gamePlayerDtos = game
                .getGamePlayers()
                .stream()
                .map(gamePlayer -> makeGamePlayerDto(gamePlayer))
                .collect(Collectors.toList());

        gameDto.put("gamePlayers", gamePlayerDtos);
        return gameDto;
    }

    private Map<String, Object> makeGamePlayerDto(GamePlayer gamePlayer) {
        Map<String, Object> gamePlayerDto = new LinkedHashMap<>();
        gamePlayerDto.put("id", gamePlayer.getId());
        gamePlayerDto.put("player", makePlayerDto(gamePlayer.getPlayer()));
        return gamePlayerDto;
    }

    private Map<String, Object> makePlayerDto(Player player) {
        Map<String, Object> playerDto = new LinkedHashMap<>();
        playerDto.put("id", player.getId());
        playerDto.put("email", player.getUserName());
        return playerDto;
    }
}








