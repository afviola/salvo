package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class GamePlayer {
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

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvoes;

    private Date creationDate;

    public GamePlayer() {

    }

    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.creationDate = new Date();
    }

    public Map<String, Object> toDto() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gpid", id);
        dto.put("player", player.toDto());

        return dto;
    }

    public Stream<Map<String, Object>> toDtoShipStream() {
        return ships.stream().map(ship -> ship.toDto());
    }

    public Stream<Map<String, Object>> toDtoSalvoStream() {
        return salvoes.stream().map(salvo -> salvo.toDto());
    }

    public long getId() {
        return id;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }
}
