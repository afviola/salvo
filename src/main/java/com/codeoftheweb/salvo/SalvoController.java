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
import java.util.Set;
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
        Map dto = makeMap("player", isGuest(authentication) ? null : getCurrentUser(authentication).toDto());

        dto.put("games", gameRepository
                .findAll()
                .stream()
                .map(game -> game.toDto())
                .collect(Collectors.toList()));

        return dto;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createNewGame(Authentication authentication) {

        if( isGuest(authentication) )
            return new ResponseEntity<>(makeMap("error", "users must be logged in to create games"), HttpStatus.UNAUTHORIZED);

        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(getCurrentUser(authentication), gameRepository.save(new Game())));

        return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long gameId, Authentication authentication) {

        if( isGuest(authentication) )
            return new ResponseEntity<>(makeMap("error", "users must be logged in to join games"), HttpStatus.UNAUTHORIZED);

        Game game = gameRepository.getOne(gameId);
        if( game == null )
            return new ResponseEntity<>(makeMap("error", "no such game"), HttpStatus.FORBIDDEN);

        Set<GamePlayer> gamePlayers = game.getGamePlayers();
        if( gamePlayers.size() == 2 )
            return new ResponseEntity<>(makeMap("error", "game is full"), HttpStatus.FORBIDDEN);

        if( gamePlayers.stream().anyMatch(gp -> gp.getPlayer().getId() == getCurrentUser(authentication).getId()) )
            return new ResponseEntity<>(makeMap("error", "you can't join to your own game"), HttpStatus.FORBIDDEN);

        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(getCurrentUser(authentication), game));

        return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> getGamePlayerViewById(@PathVariable long gamePlayerId, Authentication authenticacion) {

        if( isGuest(authenticacion) )
            return new ResponseEntity<>(makeMap("error", "user is a guest"), HttpStatus.UNAUTHORIZED);

        if( getCurrentUser(authenticacion)
                .getGamePlayers()
                .stream()
                .noneMatch(gamePlayer -> gamePlayer.getId() == gamePlayerId) ) {

            return new ResponseEntity<>(makeMap("error", "what a shame bro"), HttpStatus.FORBIDDEN);
        }

        GamePlayer currentGamePlayer = gamePlayerRepository.getOne(gamePlayerId);
        Map gameView = currentGamePlayer.getGame().toDto();

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

        return new ResponseEntity<>(gameView, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createNewPlayer(@RequestParam String username, @RequestParam String password) {

        if( username.isEmpty() || password.isEmpty() )
            return new ResponseEntity<>(makeMap("error", "invalid login data"), HttpStatus.FORBIDDEN);

        if( playerRepository.findByUserName(username) != null )
            return new ResponseEntity<>(makeMap("error", "username already exists"), HttpStatus.CONFLICT);

        Player newPlayer = playerRepository.save(new Player(username, passwordEncoder.encode(password)));
        Map map = makeMap("id", newPlayer.getId());
        map.put("username", newPlayer.getUserName());

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    private Player getCurrentUser(Authentication authentication) {
        return playerRepository.findByUserName(authentication.getName());
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }
}









