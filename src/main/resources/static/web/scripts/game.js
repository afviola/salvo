Vue.component('board', {
    props: ['rows', 'cols', 'type'],
    data: function() {
        return {
            intToChar: {
                1: 'A',
                2: 'B',
                3: 'C',
                4: 'D',
                5: 'E',
                6: 'F',
                7: 'G',
                8: 'H',
                9: 'I',
                10: 'J'
            },

            charToInt: {
                A: 1,
                B: 2,
                C: 3,
                D: 4,
                E: 5,
                F: 6,
                G: 7,
                H: 8,
                I: 9,
                J: 10
            }
        }
    },

    template: `
        <table class="table table-bordered">
            <thead>
                <tr>
                  <th scope="col"></th>
                  <th v-for="col in cols">{{ col }}</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="row in rows">
                  <th scope="row">{{ intToChar[row] }}</th>
                  <td v-for="col in cols" :id="type + intToChar[row] + col"></td>
                </tr>
            </tbody>
        </table>
    `
});

new Vue({
    el: '#app',
    data: {
        gpid: null,
        me: null,
        opponent: null,
        title: 'Waiting opponent',
        salvoes: null,
        ships: null,
        boardDataReady: false,
        toLaunch: []
    },

    mounted() {
        this.gpid = this.getParameterByName('gp');
        fetch('/api/game_view/' + this.gpid)
            .then(response => response.json())
            .then(data => {
                console.log(data);
                data.gamePlayers.forEach(gamePlayer => {
                    if(gamePlayer.gpid == this.gpid)
                        this.me = gamePlayer;
                    else
                        this.opponent = gamePlayer;
                    });
                this.setTitle();
                this.ships = data.ships;
                this.salvoes = data.salvoes;

                if(this.ships.length == 0)
                    location.assign(`input.html?gp=${this.gpid}`);

                this.boardDataReady = true;

                this.printBoats();
                this.printOpponentHits();
                this.printLaunchedSalvoes();
            })
            .catch(error => console.log(error));
        },

    methods: {
        getParameterByName(name) {
            const match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
            return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
        },

        setTitle() {
            this.title = this.opponent == null ? 'Waiting Opponent' : this.me.player.email + ' VS ' + this.opponent.player.email;
        },

        printBoats() {
            this.ships.forEach(ship => {
                ship.locations.forEach(location => {
                    document.getElementById(`b${location}`).classList.add('ship-piece');
                });
            });
        },

        printOpponentHits() {
            const myLocations = this.ships.flatMap(ship => ship.locations);

            this.salvoes
                .filter(salvo => salvo.gpid != this.gpid) // los salvos enemigos
                .forEach(salvo => {
                    salvo.locations.forEach(location => {
                        if (myLocations.includes(location)) {
                            const boatCell = document.getElementById(`b${location}`);
                            boatCell.classList.add('ship-piece-hited');
                            boatCell.innerHTML = salvo.turn;
                        }
                    });
                });
        },

        printLaunchedSalvoes() {
            this.salvoes
                .filter(salvo => salvo.gpid == this.gpid) // mis salvos
                .forEach(salvo => {
                    salvo.locations.forEach(location => {
                        const salvoCell = document.getElementById(`s${location}`);
                        salvoCell.classList.add('salvo');
                        salvoCell.innerHTML = salvo.turn;
                    });
                });
        },



        printSalvoToLaunch() {

        }
    },
});