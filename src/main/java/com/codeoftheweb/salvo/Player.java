package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores;

    private String userName;

    public Player() {

    }

    public Player(String userName) {
        this.userName = userName;
        this.gamePlayers = new HashSet<>();
        this.scores = new HashSet<>();
    }

    public float getWins() {
        return scores.stream().filter(score -> score.getScore() == 1).count();
    }

    public float getLoses() {
        return scores.stream().filter(score -> score.getScore() == 0.5).count();
    }

    public float getDraws() {
        return scores.stream().filter(score -> score.getScore() == 0).count();
    }

    public float getTotalScore() {
        return 1f * getWins() + 0.5f * getDraws() + 0 * getLoses();
    }

    public Map<String, Object> toGameHistory() {
        Map<String, Object> history = new LinkedHashMap<>();
        history.put("name", userName);
        history.put("total", getTotalScore());
        history.put("won", getWins());
        history.put("lost", getLoses());
        history.put("tied", getDraws());

        return history;
    }

    public Map<String, Object> toDto() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("email", userName);

        return dto;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }
}





























