package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_gameplayer")
    private GamePlayer gamePlayer;

    private int turn;

    public Salvo(GamePlayer gamePlayer, int turn) {
        this.gamePlayer = gamePlayer;
        this.turn = turn;
    }

    public long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public int getTurn() {
        return turn;
    }
}
