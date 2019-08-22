package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Date;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Player player;
    private Game game;
    private Date creationDate;

    public GamePlayer() {}

    public GamePlayer(Player player, Game game, Date creationDate) {
        this.player = player;
        this.game = game;
        this.creationDate = creationDate;
    }
}
