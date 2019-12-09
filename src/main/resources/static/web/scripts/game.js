let gameView = null;

function getParameterByName(name) {
    const match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

$.get('/api/game_view/' + this.getParameterByName('gp'))
    .done(data => {
        gameView = data;
    })
    .fail(response => { console.log(response) });


// para testear
function hacerLlamada() {
    let url = "/api/games/players/5/ships";
    let data = [
                 { "type": "destroyer", "locations": ["A1", "B1", "C1"] },
                 { "type": "patrol boat", "locations": ["B2", "B3"] },
                 { "type": "patrol boat", "locations": ["H1", "H2"] },
                 { "type": "patrol boat", "locations": ["G5", "G6"] },
                 { "type": "patrol boat", "locations": ["C7", "C8"] },
               ];
    $.post({
        url: url,
        dataType: "text",
        contentType: "application/json",
        data: JSON.stringify(data)
    })
    .done(respuesta => console.log(respuesta))
    .fail(respuesta => console.log(respuesta));
}


