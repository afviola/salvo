package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    private Date creationDate;

    public Game() {
        this.creationDate = new Date();
        this.gamePlayers = new HashSet<>();
        this.scores = new HashSet<>();
    }

    public Map<String, Object> toDto() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("created", creationDate.toLocaleString());
        dto.put("gamePlayers", gamePlayers
                .stream()
                .map(gamePlayer -> gamePlayer.toDto())
                .collect(Collectors.toList()));

        return dto;
    }

    public long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }
}
















