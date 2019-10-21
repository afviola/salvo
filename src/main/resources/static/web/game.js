
// tipos de casilla: agua, salvo, barco averiado, barco destruido
Vue.component('casilla', {
    props: ['tipo'],
    template: `
        <div>
            {{ tipo }}
        </div>
    `
});

Vue.component('board', {
    props: ['filas', 'columnas', 'tablero', 'titulo'],
    template: `
        <div>
            <h3>{{ titulo }}</h3>
            <table>
                <tr v-for="fila in filas">
                    <td v-for="columna in Number(columnas)">
                        <casilla
                            :id="tablero + '-' + fila + columna"
                            :tipo="'agua'">
                        </casilla>
                    </td>
                </tr>
            </table>
        </div>
    `
});

var app = new Vue({
    el: "#app",
    data: {
        filas: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'],
        columnas: 10
    },
    created() {
         $.get('/api/game_view/' + this.getParameterByName('gp'))
            .done(response => {
                console.log(response);
            })
            .fail(() => { 'ups' });
    },
    methods: {
        getParameterByName(name) {
          var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
          return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
        },
    }
});