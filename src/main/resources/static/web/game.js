Vue.component('casilla', {
    props: [],
    template: `
        <div>

        </div>
    `
});

Vue.component('board', {
    template: `
        <div>
            <table>
                <tr v-for="fila in filas">
                    <td v-for="columna in columnas">
                        <casilla></casilla>
                    </td>
                </tr>
            </table>
        </div>
    `
});

var app = new Vue({
    el: "#app",
    data: {
        gameView: null
    },
    created() {
         $.get('/api/game_view/' + this.getParameterByName('gp'))
            .done(gameView => {

            })
    },
    methods: {
        getParameterByName(name) {
          var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
          return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
        }
    }
});