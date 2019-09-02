package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    private String userName;

    public Player() {

    }

    public Player(String userName) {
        this.userName = userName;
        this.gamePlayers = new HashSet<>();
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
}





























