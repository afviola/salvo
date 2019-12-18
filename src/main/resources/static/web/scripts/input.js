const players = {
    me: null,
    opponent: null
};

function getParameterByName(name) {
    const match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

function setPlayersInfo(data) {
    data.gamePlayers.forEach(gamePlayer => {
        if(gamePlayer.gpid == getParameterByName('gp'))
            players.me = gamePlayer;
        else
            players.opponent = gamePlayer;
    });
}

function postShips() {
    let url = `/api/games/players/${getParameterByName('gp')}/ships`;
    let data = ['destroyer', 'carrier', 'patrol_boat', 'submarine', 'battleship'].map(type => getLocations(type));

    $.post({
        url: url,
        dataType: "text",
        contentType: "application/json",
        data: JSON.stringify(data)
    })
    .done(respuesta => {
        console.log(respuesta);
        location.assign(`game.html?gp=${getParameterByName('gp')}`);
    })
    .fail(respuesta => console.log(respuesta));
}

function getLocations(shipType) {
    let domShip = $('#' + shipType);
    let ship = {
        name: domShip.attr('id'),
        x: domShip.attr('data-gs-x'),
        y: domShip.attr('data-gs-y'),
        width: domShip.attr('data-gs-width'),
        height: domShip.attr('data-gs-height'),
        positions: []
    }

    if (ship.height == 1) {
        for (let i = 1; i <= ship.width; i++) {
            ship.positions.push(String.fromCharCode(parseInt(ship.y) + 65) + (parseInt(ship.x) + i))
        }
    } else {
        for (let i = 0; i < ship.height; i++) {
            ship.positions.push(String.fromCharCode(parseInt(ship.y) + 65 + i) + (parseInt(ship.x) + 1))
        }
    }

    return {
        type: ship.name,
        locations: ship.positions
    }
}

$(document).ready(function() {
    $.get('/api/game_view/' + getParameterByName('gp'))
        .done(data => {
            console.log(data);
            setPlayersInfo(data);

            $('#btn-back-to-games').click(function() {
                location.assign('games.html');
            });

            $('#btn-save-ships').click(function() {
                postShips();
            });

            loadGrid();
        })
        .fail(response => { console.log(response) });
});


