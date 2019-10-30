Vue.component('leaderboard', {
    data: function() {
        return {
            players: []
        }
    },

    created() {
        $.get('/api/leaderboard').done(players => { this.players = players });
    },

    template: `
        <div>
            <h3>Leaderboard</h3>
            <table>
                <thead>
                    <th>Name</th>
                    <th>Total</th>
                    <th>Won</th>
                    <th>Tied</th>
                    <th>Lost</th>
                </thead>
                <tr v-for="player in players">
                    <td>{{ player.name }}</td>
                    <td>{{ player.total }}</td>
                    <td>{{ player.won }}</td>
                    <td>{{ player.tied }}</td>
                    <td>{{ player.lost }}</td>
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
            user: null,
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
            $.get('/api/games').done(response => {
                this.games = response.games;
                this.user = response.player;
            });
        },

        login() {
            $.post('/api/login', { username: this.username, password: this.password })
                .done(() => {
                    this.fetchData();
                    this.clearInputs();
                })
                .fail(() => console.log('ups! login failed'));
        },

        logout() {
            $.post('/api/logout')
                .done(() => { this.user = null });
        },

        signIn() {
            $.post('/api/players', { username: this.username, password: this.password })
                .done(() => {
                    this.login()
                })
                .fail(response => { console.log(response) });
        },

        createGame() {
            $.post('/api/games')
                .done(response => {
                    console.log(response);
                    this.redirectBrowserToGame(response.gpid);
                });
        },

        isUserPlayingThisGame(game) {
            return game.gamePlayers.find(gp => gp.player.email == this.user.email) != null;
        },

        isGameFull(game) {
            return game.gamePlayers.length == 2;
         },

        getOwnerGamePlayerId(game) {
            return game.gamePlayers[0].gpid;
        },

        joinGame(event) {
            $.post(`api/game/${event.target.attributes.gameid.value}/players`)
                .done(response => { console.log(response) })
                .fail(response => { console.log(response) });
        },

        returnToGame(event) {
            this.redirectBrowserToGame(event.target.id);
        },

        redirectBrowserToGame(gpid) {
            location.assign(`game.html?gp=${gpid}`);
        },
    },

    template: `
        <div>
            <span v-if="!user">
                <input type="text" placeholder="username" v-model="username">
                <input type="password" placeholder="password" v-model="password">
                <button @click="login">login</button>
                <button @click="signIn">sign in</button>
            </span>
            <span v-else>
                <span>User: {{ user.email }}</span>
                <button @click="logout">logout</button>
            </span>

            <h3>Game List</h3>
            <button v-show="user" @click="createGame">New Game</button>
            <table>
                <thead>
                    <th>Created</th>
                    <th>Player1</th>
                    <th>Player2</th>
                </thead>
                <tr v-for="game in games">
                    <td>{{ game.created }}</td>
                    <td>{{ game.gamePlayers[0].player.email }}</td>
                    <td v-if="isGameFull(game)">{{ game.gamePlayers[1].player.email }}</td>
                    <td v-else>
                        <button :id="getOwnerGamePlayerId(game)"
                                :gameid="game.id"
                                @click="joinGame">
                                Join Game
                        </button>
                    </td>
                    <td v-if="user && isUserPlayingThisGame(game)">
                        <button :id="getOwnerGamePlayerId(game)" @click="returnToGame">Enter</button>
                    </td>
                </tr>
            </table>
        </div>
    `
});

var app = new Vue({
    el: '#app'
});