var refreshRate = 300; //milliseconds

diceValue = 0;
tryNumber = 1;
selectedWord = "";
showWord = false;

$(document).ready(function () {
    $.ajaxSetup({cache: false});

    $('#GameAction').hide();
    $('#Replay').hide();
    $('#visitorsTable').hide();

    $('#buttonQuit').on("click", ajaxQuitGame);
    $('#buttonDice').on("click", ajaxThrowDice);
    $('#buttonShowTiles').on("click", ajaxShowTiles);
    $('#buttonMakeMove').on("click", ajaxCheckWord);

    ajaxGamesDetailsAndPlayers();
    GamesDetailsAndPlayers = setInterval(ajaxGamesDetailsAndPlayers, refreshRate);

    getBoard($('#board'));

    realPlayer();
});

function ajaxGameDone() {

    var actionType = "isGameDone";
    $.ajax({
        url: "gamingRoom",
        data: {
            "ActionType": actionType
        },
        success:function (response){
            if(response["isEnded"] === true) {
                announceWinner(response["data"]);
            }
        }
    });
}

function announceWinner (gameDetails) {

   if  (gameDetails.m_FinishAllRound === true){
       clearInterval(gameDone);

       if (gameDetails.m_TechnicalVictory === true) {
           openPopup(gameDetails.m_ErrorMsg);
       } else {
           if(gameDetails.m_WinnerName === undefined) {

               var declareTie= "It's a tie. The players points are:";
               var playersList = $('<div><p>' + declareTie + '</p><br></div>');

               gameDetails.m_Players.forEach(function (player) {
                   $('<p></p><br>').text(player.m_Name + '   ' + player.m_Score).appendTo(playersList);
               });

               openPopup(playersList);
           }
           else
               openPopup(gameDetails.m_WinnerName + " is the winner!!!");
       }
       $('#GameAction').hide();
       $('#GameInfo').hide();
       $('#buttonQuit').val("Back To Lobby");
       clearInterval(GamesDetailsAndPlayers);
   }
}

function realPlayer() {
    $("#radioMark").prop("checked", true);

    startGame = setInterval(ajaxIsGameStarted, refreshRate);

    $('#GameInfo').hide();

    $('.option').on("click", actionSelected);
}

function startIfFirstPlayerComputer() {

    var actionType = "firstPlyComputer";

    $.ajax({
        url: "gamingRoom",
        data: {
            "ActionType": actionType
        },
        success:function (isComp){
            if(isComp === "true"){
                setInterval(ajaxBoard,refreshRate); //only if is computer it will pull every min
                $('#GameAction').hide();
            }
            else
            {
                $('#GameAction').fadeIn(200);
            }
        }
    });
}

function ajaxBoard() {

    var actionType = "pullBoard";

    $.ajax({
        url: "gamingRoom",
        data: {
            "ActionType": actionType
        },
        success: function (boardInfo) {
            updateAllBoard(boardInfo);
        }
    });
}

function ajaxGamesDetailsAndPlayers() {
    var actionType = "GameStatus";

    $.ajax({
        url: "gamingRoom",
        data: {
            "ActionType": actionType
        },
        success: function (data) {
            var players = data[0];
            var gameDetails = data[1];
            var PlayerFromSession = data[2];
            var board = data[3];

            refreshGameDetails(gameDetails,PlayerFromSession );
            refreshPlayerList(players, PlayerFromSession);
            //$('#board').empty();
            if ($('#board').childElementCount === 0) {
                createBoard(board, $('#board'), actionType);
            }
            else {
                updateBoard_(board);
            }
        }
    });
}

function refreshPlayerList(players, PlayerFromSession) {
    $("#playingUsersTable").empty();
    $.each(players || [], function (index, user) {
        var icon;
        if (user.m_PlayerType === 'Human') {
            icon = "<span class='HumanIcon'></span>"
        }
        else {
            icon = "<span class='MachineIcon'></span>"
        }

        var userList = $('<tr></tr>');
        $('<th>' + user['name'] + '</th>').appendTo(userList);
        $('<th>' + icon + '</th>').appendTo(userList);
        $('<th>' + user.m_Color + '</th>').appendTo(userList);
        $('<th>' + user['score'] + '</th>').appendTo(userList);
        userList.appendTo($("#playingUsersTable"));

        if (PlayerFromSession === user['name']) {
            userList.addClass('success');
        }
    });
}

function refreshGameDetails(gameDetails, PlayerFromSession) {

    $('#loggedInUser').text("Welcome " + PlayerFromSession);
    $('#labelGameTitle').text(gameDetails.gameTitle);
    $('#labelCurrentPlayer').text(gameDetails.currentPlayer.player['name']);
    $('#labelCurrentMove').text(gameDetails.numOfTurns);
    $("#labelCurrentTry").text(tryNumber);
}

function ajaxQuitGame() {
    var actionType = "ExitGame";

    $.ajax({
        url: "gamingRoom",
        data: {
            "ActionType": actionType
        },
        success: function (data) {
            window.location.replace("lobby.html");
        },
        error: function (data) {
            console.log(data);
        }
    });
}

function ajaxThrowDice() {
    var actionType = "ThrowDice";

    $.ajax({
        url: "gamingRoom",
        data: {
            "ActionType": actionType
        },
        success: function (response) {
            if (response.canPlay) {
                diceValue = response.data;
                openPopup("Dice value is: " + diceValue);
                selectedTilesList = [];
            }
            else {
                openPopup(response.message);
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
}

function ajaxShowTiles() {
    var actionType = "UpdateBoard";

    $.ajax({
        url: "gamingRoom",
        data: {
            "ActionType": actionType,
            "TilesList": JSON.stringify(selectedTilesList)
        },
        success: function (response) {
            if (response.canPlay) {
                var data = response.data;
                if (data[0] === true) {
                    selectedTilesList = [];
                    showWord = true;
                    tryNumber = 1;
                }
                else if (data[1] === false) {
                    openPopup("You chose too many tiles! you need to choose only " + diceValue + " tiles \n\nTry again.");
                }
                else {
                    openPopup("You have selected at least one tile out of bounds!");
                }
            }
            else {
                openPopup(response.message);
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
}

function ajaxCheckWord() {
    var actionType = "CheckWord";

    if (selectedTilesList.length < 2) {
        openPopup("You need to select at least 2 tiles");
        return;
    }

    $.ajax({
        url: "gamingRoom",
        data: {
            "ActionType": actionType,
            "Word": selectedWord,
            "TryNumber": tryNumber,
            "TilesList": JSON.stringify(selectedTilesList)
        },
        success: function (response) {
            if (response.canPlay) {
                var data = response.data;
                var message;
                switch (data) {
                    case "CORRECT":
                        message = "Well done! your word \"" + selectedWord + "\" is correct!!!";
                        selectedTilesList = [];
                        showWord = false;
                        break;
                    case "WRONG":
                        message = "Uhh.. the word \"" + selectedWord + "\" is wrong!!!\nTry again.";
                        tryNumber++;
                        break;
                    case "WRONG_CANT_RETRY":
                        message = "Uhh.. The word \"" + selectedWord + "\" is Wrong.\nThat was your last try..";
                        selectedTilesList = [];
                        showWord = false;
                        break;
                    case "CHARS_NOT_PRESENT":
                        message = "Oops.. you enter INVALID chars!";
                        tryNumber++;
                        break;
                    case "TRIES_DEPLETED":
                        message = "Oops.. you have got No more tries!!";
                        selectedTilesList = [];
                        showWord = false;
                        break;
                }
                openPopup(message);
            }
            else {
                openPopup(response.message);
            }
        },
        error: function (data) {
            console.log(data);
        }
    });
}

function ajaxIsGameStarted() {
    var actionType = "isGameStarted";

    $.ajax({
        url: "gamingRoom",
        data: {
            "ActionType": actionType
        },
        success: function (isGameStarted) {
            if (isGameStarted === true) {
                openPopup("Game Started!");
                clearInterval(startGame);
                $('#GameInfo').fadeIn(200);
                gameDone = setInterval(ajaxGameDone, refreshRate);
                startIfFirstPlayerComputer();
            }
        }
    });
}

function actionSelected() {
    $('.option').removeClass('optionSelected');
    $(this).addClass('optionSelected');
}

function openPopup(msg) {
    $("#message").html(msg);
    $("#popup").show();
}

function closePopup() {
    $("#message").html("");
    $("#popup").hide();
}
