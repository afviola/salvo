package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_player")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_game")
    private Game game;

    private Date finishDate;
    private float score;

    public Score() {

    }

    public Score(Player player, Game game, float score) {
        this.player = player;
        this.game = game;
        this.score = score;
        this.finishDate = new Date();
    }

    public long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public float getScore() {
        return score;
    }
}
