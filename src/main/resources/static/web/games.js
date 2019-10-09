
$('document').ready(function() {
    $.get('/api/games')
        .done((games) => { createGameList(games) })
        .fail((jqHRX, status) => { console.error(status) });

    $.get('/api/leaderboard')
        .done((players) => { createLeaderboard(players) })
        .fail((jqHRX, status) => { console.error(status) });
});

function createGameList(games) {
    let gameList = $('#game-list');

    games.forEach(game => {
        gameList.append(
            `<li>
                ${game.created} ;
                ${game.gamePlayers[0].player.email} ;
                ${game.gamePlayers[1].player.email}
            </li>`
        );
    });
}

function createLeaderboard(players) {
    let leaderboard = $('#leaderboard');

    players.forEach(player => {
        leaderboard.append(
            `<li>
                ${ player.name }
                ${ player.total }
                ${ player.won }
                ${ player.lost }
                ${ player.tied }
             </li>
            `
        );
    });
}