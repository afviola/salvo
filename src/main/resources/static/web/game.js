Vue.component('board', {
    props: ['filas', 'columnas', 'tablero'],
    template: `
        <div>
            <table border="1">
                <thead>
                    <th></th>
                    <th v-for="columna in columnas">
                      {{columna}}
                    </th>
                </thead>
                <tr v-for="fila in filas">
                    <td> {{fila}} </td>
                    <td v-for="columna in Number(columnas)"
                        :id="tablero + '_' + fila + columna">
                    </td>
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
        playerInfo: [],
        gameTitle: '',
        boardDataReady: false,
    },

    mounted() {
         $.get('/api/game_view/' + this.getParameterByName('gp'))
            .done(data => {
                console.log(data);
                this.setPlayerInfo(data);
                this.setGameTitle();

                this.printBoats(data.ships);
                this.printLaunchedSalvoes(data.salvoes);
                this.printOpponentHits(data.ships, data.salvoes);

                this.boardDataReady = true;
            })
            .fail((jqXHR, textStatus) => {
                alert('Failed: ' + textStatus);
            });
    },

    methods: {
        getParameterByName(name) {
          let match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
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
            salvoes
                .filter(salvo => salvo.player == this.playerInfo[0].id) // mis salvos
                .forEach(salvo => {
                    salvo.locations.forEach(location => {
                        let salvoCell = document.getElementById(`S_${location}`);
                        salvoCell.classList.add('salvo');
                        salvoCell.innerHTML = salvo.turn;
                    });
                });
        },

        printOpponentHits(ships, salvoes) {
            let myLocations = ships.flatMap(ship => ship.locations);

            salvoes
                .filter(salvo => salvo.player != this.playerInfo[0].id) // los salvos enemigos
                .forEach(salvo => {
                    salvo.locations.forEach(location => {
                        if (myLocations.includes(location)) {
                            let boatCell = document.getElementById(`B_${location}`);
                            boatCell.classList.add('ship-piece-hited');
                            boatCell.innerHTML = salvo.turn;
                        }
                    });
                });
        },

        setPlayerInfo(data) {
            if (data.gamePlayers[0].id == this.getParameterByName('gp'))
                this.playerInfo = [data.gamePlayers[0].player, data.gamePlayers[1].player];
            else
                this.playerInfo = [data.gamePlayers[1].player, data.gamePlayers[0].player];
        },

        setGameTitle() {
            this.gameTitle = this.playerInfo[0].email + '(you) vs ' + this.playerInfo[1].email;
        },

        getGameTitle() {
            return this.playerInfo[0].email + '(you) vs ' + this.playerInfo[1].email;
        }
    }
});