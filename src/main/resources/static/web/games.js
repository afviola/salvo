
Vue.component('navbar', {
    data: function() {
        return {
            username: '',
            password: '',
            guest: true,
        }
    },

    methods: {
        login: function() {
            $.post("/api/login", { username: this.username, password: this.password })
                .done(() => {
                    this.cleanInputs();
                    this.guest = false;
                    console.log("logged in");
                })
                .fail(() => console.log('ups! login failed'));
        },

        logout: function() {
            $.post("/api/logout")
                .done(() => {
                    this.guest = true;
                    console.log("logged out");
                });
        },

        signIn: function() {
            $.post("/api/players", { username: this.username, password: this.password })
                .done(response => {
                    this.cleanInputs();
                    console.log(`new user created: ${response.username}`);
                });
        },

        cleanInputs: function() {
            this.username = '';
            this.password = '';
        }
    },

    template: `
        <div>
            <span v-if="guest">
                <input type="text" placeholder="username" v-model="username">
                <input type="password" placeholder="password" v-model="password">
                <button @click="login">login</button>
                <button @click="signIn">sign in</button>
            </span>
            <button v-else @click="logout">logout</button>
        </div>
    `
});

Vue.component('leaderboard', {
    data: function() {
        return {
            players: []
        }
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
    `,

    created() {
        $.get('/api/leaderboard')
            .done(players => players.forEach(player => this.players.push(player)));
    }
});

Vue.component('game-list', {
    data: function() {
        return {
            games: []
        }
    },

    template: `
        <div>
            <h3>Game List</h3>
            <table>
                <thead>
                    <th>Created</th>
                    <th>Player1</th>
                    <th>Player2</th>
                </thead>
                <tr v-for="game in games">
                    <td>{{ game.created }}</td>
                    <td>{{ game.gamePlayers[0].player.email }}</td>
                    <td>{{ game.gamePlayers[1].player.email }}</td>
                </tr>
            </table>
        </div>
    `,

    created() {
        $.get('/api/games')
            .done(response => { response.games.forEach(game => this.games.push(game)) });
    }
});

var app = new Vue({
    el: '#app'
});