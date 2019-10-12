package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public Map<String, Object> getAllGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();

        if(isGuest(authentication))
            dto.put("player", null);
        else
            dto.put("player", getCurrentUser(authentication).toDto());

        dto.put("games", gameRepository
                .findAll()
                .stream()
                .map(game -> game.toDto())
                .collect(Collectors.toList()));

        return dto;
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

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createNewPlayer(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> response = new LinkedHashMap<>();

        if(username.isEmpty()) {
            response.put("error", "no username");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        if(password.isEmpty()) {
            response.put("error", "no password");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        if(playerRepository.findByUserName(username) != null) {
            response.put("error", "username already exists");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        // irian unas cuantas validaciones mas, si las pasa todas creo el user

        Player newPlayer = playerRepository.save(new Player(username, passwordEncoder.encode(password)));
        response.put("id", newPlayer.getId());
        response.put("username", newPlayer.getUserName());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private Player getCurrentUser(Authentication authentication) {
        return playerRepository.findByUserName(authentication.getName());
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
}









