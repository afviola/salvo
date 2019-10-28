Vue.component('leaderboard', {
    data: function() {
        return {
            players: []
        }
    },

    created() {
        $.get('/api/leaderboard').done(players => players.forEach(player => this.players.push(player)));
    },

    template: `
        <div>
            <h3>Leaderboard</h3>
            <table>
                <thead>
                    <th>Name</th>
                    <th>Total</th>
                    <th>Won</th>
                    <th>Lost</th>
                    <th>Tied</th>
                </thead>
                <tr v-for="player in players">
                    <td>{{ player.name }}</td>
                    <td>{{ player.total }}</td>
                    <td>{{ player.lost }}</td>
                    <td>{{ player.tied }}</td>
                </tr>
            </table>
        </div>
    `
});

Vue.component('games', {
    data: function() {
        return {
            username: '',
            password: '',
            player: null,
            games: []
        }
    },

    created() {
       this.fetchData();
    },

    methods: {
        clearInputs() {
            this.username = '';
            this.password = '';
        },

        fetchData() {
            $.get("/api/games").done(response => {
                response.games.forEach(game => this.games.push(game));
                this.player = response.player;
            });
        },

        login() {
            $.post("/api/login", { username: this.username, password: this.password })
                .done(() => {
                    this.fetchData();
                    this.clearInputs();
                })
                .fail(() => console.log('ups! login failed'));
        },

        logout() {
            $.post("/api/logout")
                .done(() => {
                    this.player = null
                });
        },

        signIn() {
            $.post("/api/players", { username: this.username, password: this.password })
                .done(response => {
                    this.clearInputs();
                    alert(`new user created: ${response.username}`);
            });
        },

        createGame() {
            $.post("/api/games")
                .done(response => {
                    console.log(response);
                    location.assign(`game.html?gp=${response.gpid}`);
                });
        },

        joinGame() {
            console.log('joinGame clicked');
        }
    },

    template: `
        <div>
            <span v-if="!player">
                <input type="text" placeholder="username" v-model="username">
                <input type="password" placeholder="password" v-model="password">
                <button @click="login">login</button>
                <button @click="signIn">sign in</button>
            </span>
            <span v-else>
                <span>User: {{ player.email }}</span>
                <button @click="logout">logout</button>
            </span>

            <h3>Game List</h3>
            <button v-show="player" @click="createGame">New Game</button>
            <table>
                <thead>
                    <th>Created</th>
                    <th>Player1</th>
                    <th>Player2</th>
                </thead>
                <tr v-for="game in games">
                    <td>{{ game.created }}</td>
                    <td>{{ game.gamePlayers[0].player.email }}</td>
                    <td v-show="game.gamePlayers[1]">{{ game.gamePlayers[1].player.email }}</td>
                    <td v-show="!game.gamePlayers[1]">
                        <button :id="game.gamePlayers[0].gpid"
                                @click="joinGame">
                                Join Game
                        </button>
                    </td>
                </tr>
            </table>
        </div>
    `
});

var app = new Vue({
    el: '#app'
});