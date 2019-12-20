const eventBus = new Vue();

Vue.component('topbar', {
    data: function() {
        return {
            username: '',
            password: '',
            logged: false
        }
    },

    template: `
        <nav class="navbar navbar-light bg-light">
          <a class="navbar-brand">Salvo!</a>
          <form class="form-inline">
            <input class="form-control mr-sm-2" type="text" placeholder="Username" aria-label="Username" v-model="username" v-show="!logged">
            <input class="form-control mr-sm-2" type="password" placeholder="Password" aria-label="Password" v-model="password" v-show="!logged">
            <button @click="login" class="btn btn-outline-success my-2 my-sm-0" type="button" v-show="!logged">Log in</button>
            <h5 v-if="logged" class="mr-2">{{ username }}</h5>
            <button @click="logout" class="btn btn-outline-danger my-2 my-sm-0" type="button" v-show="logged">Log out</button>
            <button @click="signIn" class="btn btn-outline-primary my-2 ml-2 my-sm-0" type="button" v-show="!logged">Sign in</button>
          </form>
        </nav>
    `,

    created() {
        eventBus.$on('games-user-logged', user => {
            this.username = user.email;
            this.logged = true;
        });

        eventBus.$on('games-user-logged-out', () => {
            this.logged = false;
            this.username = '';
        });
    },

    methods: {
        login() {
            $.post('/api/login', {username: this.username, password: this.password})
                .done(() => {
                    eventBus.$emit('bar-user-logged', this.username);
                    this.password = '';
                    this.logged = true;
                })
                .fail(error => {
                    alert('Sorry, try again');
                    console.log(error);
                });
        },

        logout() {
            $.post('/api/logout')
                .then(() => {
                    alert('See you soon');
                    eventBus.$emit('bar-user-logged-out');
                    this.username = '';
                    this.logged = false;
                });
        },

        signIn() {
            $.post('/api/players', { username: this.username, password: this.password })
                .done(() => {
                    alert('Welcome ' + this.username);
                    this.login()
                })
                .fail(response => {
                    console.log(response);
                    alert('sign in failed');
                });
        }
    }
});

Vue.component('game-list', {
    data: function() {
        return {
            ready: true,
            player: null,
            games: null
        }
    },

    template: `
        <div v-show="ready">
            <div class="row">
                <div class="col">
                    <button v-if="player" class="btn btn-outline-success" type="button" @click="createGame">New Game</button>
                </div>
                <h3 class="d-flex justify-content-center col">Game List</h3>
                <div class="col"></div>
            </div>

            <table class="table">
                <thead>
                    <tr>
                        <th scope="col">Created</th>
                        <th scope="col">Player1</th>
                        <th scope="col">Player2</th>
                        <th scope="col" v-show="player"></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="game in games">
                        <td> {{ game.created }} </td>
                        <td> {{ game.gamePlayers[0].player.email }} </td>
                        <td>
                            <span v-if="isGameFull(game)">{{ game.gamePlayers[1].player.email }}</span>
                            <button v-else-if="!isMyGame(game) && player"
                                    class="btn btn-outline-primary"
                                    type="button"
                                    @click="joinGame(game)">Join Game</button>
                        </td>
                        <td v-if="player">
                            <button v-if="isMyGame(game)"
                                    class="btn btn-outline-primary"
                                    type="button"
                                    @click="returnToGame(game)">Return</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    `,

    created() {
        eventBus.$on('bar-user-logged-out', () => { this.fetchGames() });
        eventBus.$on('bar-user-logged', user => { this.fetchGames() });

        this.fetchGames(); // fetch inicial
    },

    methods: {
        fetchGames() {
            $.get('/api/games')
                .done(json => {
                    console.log(json);
                    this.player = json.player;
                    this.games = json.games;
                    if(this.player)
                        eventBus.$emit('games-user-logged', this.player);
                    this.ready = true;
                })
                .fail(error => console.log(error));
        },

        isGameFull(game) {
            return game.gamePlayers.length == 2;
        },

        isMyGame(game) {
           return this.player && game.gamePlayers.find(gp => gp.player.email == this.player.email) != null
        },

        returnToGame(game) {
            location.assign(`game.html?gp=${this.getMyGPIDfromGame(game)}`);
        },

        createGame() {
            $.post('/api/games')
                .done(response => {
                    location.assign(`game.html?gp=${response.gpid}`);
                });
        },

        joinGame(game) {
            $.post(`/api/game/${game.id}/players`)
                .done(res => { location.assign(`game.html?gp=${res.gpid}`) });
        },

        getMyGPIDfromGame(game) {
            let myIndex = this.player.email == game.gamePlayers[0].player.email ? 0 : 1;
            return game.gamePlayers[myIndex].gpid;
        }
    }
});

Vue.component('leaderboard', {
    data: function() {
        return {
            ready: false,
            leaderboard: null
        }
    },

    template: `
        <div v-show="ready">
            <h3 class="d-flex justify-content-center">Leaderboard</h3>
            <table class="table">
                <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Name</th>
                        <th scope="col">Total</th>
                        <th scope="col">Won</th>
                        <th scope="col">Tied</th>
                        <th scope="col">Lost</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(player, index) in leaderboard">
                        <th scope="row">{{ index + 1}}</th>
                        <td>{{ player.name }}</td>
                        <td>{{ player.total }}</td>
                        <td>{{ player.won }}</td>
                        <td>{{ player.tied }}</td>
                        <td>{{ player.lost }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    `,

    created() {
        $.get('/api/leaderboard')
            .done(json => {
                this.leaderboard = json.sort((p1, p2) => p2.total - p1.total);
                this.ready = true;
            })
            .fail(error => console.log(error));
    }
});

const app = new Vue({
    el: '#app'
});