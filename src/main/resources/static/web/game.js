
Vue.component('casilla', {
    props: ['tipo', 'posicion'],
    template: `
        <div :class='tipo'>1</div>
    `
});

Vue.component('board', {
    props: ['filas', 'columnas', 'tablero', 'titulo', 'data'],
    methods: {
        getCellClass(pos) {
            this.tablero == 1 ? this.boardType1Cell(pos) : this.boardType2Cell(pos);
        },
        createShips() {

        },
        createSalvoes() {

        },

        boardType1Cell(pos) {

            return 'barco-averiado'
        },
        boardType2Cell(pos) {
            return 'salvo'
        }
    },
    template: `
        <div>
            <h3>{{ titulo }}</h3>
            <table>
                <tr v-for="fila in filas">
                    <td v-for="columna in Number(columnas)">
                        <casilla
                            :id="tablero + '-' + fila + columna"
                            :tipo="getCellClass(fila + columna)"
                            :posicion="fila + columna">
                        </casilla>
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
        gameView: null
    },
    created() {
         $.get('/api/game_view/' + this.getParameterByName('gp'))
            .done(response => {
                console.log(response);
                this.gameView = response;
            });
    },
    methods: {
        getParameterByName(name) {
          var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
          return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
        },
    }
});