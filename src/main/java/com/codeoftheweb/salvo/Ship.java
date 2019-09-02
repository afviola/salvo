package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_gameplayer")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "location")
    private List<String> locations;

    private String type;

    public Ship() {

    }

    public Ship(String type ,GamePlayer gamePlayer, List<String> locations) {
        this.type = type;
        this.gamePlayer = gamePlayer;
        this.locations = locations;
    }

    public long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public List<String> getLocations() {
        return locations;
    }

    public String getType() {
        return type;
    }
}
