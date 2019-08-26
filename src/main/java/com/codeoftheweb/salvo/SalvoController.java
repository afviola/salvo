package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SalvoController {
    @Autowired
    private GameRepository gameRepository;

    @RequestMapping("/api/games")
    public List<Object> findAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeGameDto(game))
                .collect(Collectors.toList());
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








