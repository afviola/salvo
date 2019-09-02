package com.codeoftheweb.salvo;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_player")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_game")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships;

    private Date creationDate;

    public GamePlayer() {

    }

    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.creationDate = new Date();
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

    public Date getCreationDate() {
        return creationDate;
    }

    public Set<Ship> getShips() {
        return ships;
    }
}
