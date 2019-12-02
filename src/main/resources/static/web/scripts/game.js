Vue.component('board', {
    props: ['filas', 'columnas', 'tablero'],
    template: `
        <div>
            <table border="1">
                <thead>
                    <th></th>
                    <th v-for="columna in columnas">{{ columna }}</th>
                </thead>
                <tr v-for="fila in filas">
                    <td>{{ fila }}</td>
                    <td v-for="columna in Number(columnas)" :id="tablero + '_' + fila + columna"></td>
                </tr>
            </table>
        </div>
    `,
});

var app = new Vue({
    el: "#app",
    data: {
        filas: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'],
        columnas: 10,
        players: [],
        boardDataReady: false,
    },

    mounted() {
         $.get('/api/game_view/' + this.getParameterByName('gp'))
            .done(data => {
                console.log(data);
                this.setPlayersInfo(data);
                this.printBoats(data.ships);
                this.printLaunchedSalvoes(data.salvoes);
                this.printOpponentHits(data.ships, data.salvoes);
                this.boardDataReady = true;
            })
            .fail(response => { console.log(response) });
    },

    methods: {
        getParameterByName(name) {
          const match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
          return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
        },

        printBoats(ships) {
            ships.forEach(ship => {
                ship.locations.forEach(location => {
                    document.getElementById(`B_${location}`).classList.add('ship-piece');
                });
            });
        },

        printLaunchedSalvoes(salvoes) {
            console.log(salvoes);
            salvoes
                .filter(salvo => salvo.player == this.players[0].gpid) // mis salvos
                .forEach(salvo => {
                    salvo.locations.forEach(location => {
                        const salvoCell = document.getElementById(`S_${location}`);
                        salvoCell.classList.add('salvo');
                        salvoCell.innerHTML = salvo.turn;
                    });
                });
        },

        printOpponentHits(ships, salvoes) {
            const myLocations = ships.flatMap(ship => ship.locations);

            salvoes
                .filter(salvo => salvo.player != this.players[0].id) // los salvos enemigos
                .forEach(salvo => {
                    salvo.locations.forEach(location => {
                        if (myLocations.includes(location)) {
                            const boatCell = document.getElementById(`B_${location}`);
                            boatCell.classList.add('ship-piece-hited');
                            boatCell.innerHTML = salvo.turn;
                        }
                    });
                });
        },

        setPlayersInfo(data) {
            const swapArray = ([a,b]) => b ? [b,a] : [a];
            this.players = data.gamePlayers[0].id == this.getParameterByName('gp') ? data.gamePlayers : swapArray(data.gamePlayers);
        },

        redirectToGames() {
            location.assign('games.html');
        },

        isGameFull() {
            return this.players.length == 2;
        }
    },

    computed: {
        gameTitle() {
            return this.isGameFull() ? this.players[0].player.email + ' VS ' + this.players[1].player.email : 'Waiting Opponent';
        }
    }
});

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