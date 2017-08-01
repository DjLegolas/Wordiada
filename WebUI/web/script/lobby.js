var refreshRate = 2000; //miliseconds

$(document).ready(function () {
    ajaxIsAlreadyPlaying();

    $.ajaxSetup({cache: false});
    $('#joinGameButton').hide().on("click", joinGame);
    $('#showBoard').hide().on("click",openPopupWithBoard);


    ajaxUsersAndGameList();
    setInterval(ajaxUsersAndGameList, refreshRate);

    //$('#joinGameButton').on("click", joinGame);
    //$('#showBoard').on("click",openPopupWithBoard);
    $('#buttonLogOut').on("click", logOut);

    $('#xmlSelectionButton').on("click", function (event) {
        event.preventDefault();
        $('#xmlFile').trigger('click');
    });

    $('#dictSelectionButton').on("click", function (event) {
        event.preventDefault();
        $('#dictFile').trigger('click');
    });

    $('#uploadButton').on("click", function (event) {
        event.preventDefault();
        if(validFileExtension([$("#xmlFile").val(), $('#dictFile').val()])){
            uploadFile();
            $('#xmlFile').val("");
            $('#dictFile').val("");
        }
    });
});

$(document).on("click", "#gameTable tr", function(e){
    $(this).addClass('success').siblings().removeClass('success');
    $('#joinGameButton').fadeIn(200);

    $('#showBoard').fadeIn(200);
});


function validFileExtension(filesArray) {
    var arrayExtensions = ["xml", "txt"];
    for (var i = 0; i < filesArray.length; i++) {
        var file = filesArray[i];
        var ext = file.split(".");
        ext = ext[ext.length - 1].toLocaleLowerCase();

        if (i === 0 && arrayExtensions.lastIndexOf(ext) !== 0) {
            $('#xmlFile').val("");
            openPopup("not xml file");
            return false;
        }
        else if (i === 1 && arrayExtensions.lastIndexOf(ext) !== 1) {
            $('#dictFile').val("");
            openPopup("not dictionary file");
            return false;
        }
    }
    return true;
}


function joinGame() {

    $('#joinGameButton').hide();

    var gamettl = $("#gameTable").find("tr.success").attr('value');

    var actionType = "JoinGame";

    $.ajax({
        url: "lobby",
        type: 'GET',
        data: {
            "gameTitle": gamettl,
            "ActionType": actionType
        },
        success: function(success) {
            if (success === true){
                window.location.replace("GameRoom.html");
            } else {
                openPopup("Can not get into this Game.");
            }
        }
    });
}

function logOut() {
    var actionType = "Logout";

    $.ajax({
        url: "lobby",
        type: 'GET',
        data:{
            "ActionType": actionType
        },
        success: function(data) {
            window.location.replace("index.html");
        },
        error: function (data) {
            openPopup("There was an error. Please try again.")
        }
    });
}

function ajaxUsersAndGameList() {

    var actionType = "GameAndUserList";

    $.ajax({
        url: "lobby",
        type: 'GET',
        data:{
            "ActionType": actionType
        },
        success: function(data) {
            refreshUsersAndGameList(data);
        },
        error: function (data) {
            console.log(data);
        }
    });
}

function refreshUsersAndGameList(data) {
    var usersList = data[0];
    var gamesList = data[1];

    refreshUsersList(usersList);
    refreshGameList(gamesList);
}

function refreshGameList(games) {

    var selected = $("#gameTable").find("tr.success").attr('value');

    $("#gameTable").empty();
    $.each(games || [], function (gameKey, gameValue)
    {
        var gameList = $('<tr value="'+gameKey+'"> </tr>');
        var bordSize = gameValue.boardSize;
        var players = gameValue.totalPlayers;
        var signedUpPlayers = gameValue.players.length ;
        var gameStatus;
        if(!gameValue.isGameActive && gameValue.players.length < gameValue.totalPlayers){
            gameStatus = "Available"
        }else {
            gameStatus = "Not Available"
        }
        var dictName = gameValue.dictName;
        var lettersAmount = gameValue.lettersAmount;


        $('<th>' + gameKey + '</th>').appendTo(gameList);
        $('<th>' + gameValue.organizer + '</th>').appendTo(gameList);
        $('<th>' + players + '</th>').appendTo(gameList);
        $('<th>' + signedUpPlayers + '</th>').appendTo(gameList);
        $('<th>' + bordSize + '</th>').appendTo(gameList);
        $('<th>' + gameStatus + '</th>').appendTo(gameList);
        $('<th>' + dictName + '</th>').appendTo(gameList);
        $('<th>' + lettersAmount + '</th>').appendTo(gameList);

        gameList.appendTo($("#gameTable"));

        if(gameKey == selected) {
            gameList.addClass('success');
        }

    })
}

function refreshUsersList(users){

    //clear all current users
    $("#usersTable").empty();
    $.each(users || [], function(index, user) {
        // console.log("Adding user #" + index + ": " + name);
        var icon;
        if(user.m_PlayerType === 'Human') {
            icon = "<span class='HumanIcon'></span>"
        }
        else {
            icon = "<span class='MachineIcon'></span>"
        }

        var userList = $('<tr class="active"> </tr>');
        $('<th>' + user.m_Name + '</th>').appendTo(userList);
        $('<th>' + icon + '</th>').appendTo(userList);
        userList.appendTo($("#usersTable"));
    });
}

function ajaxIsAlreadyPlaying() {

    var actionType = "CheckUserPlaying";

    $.ajax({
        url: "lobby",
        type: 'GET',
        data:{
            "ActionType": actionType
        },
        success: function(isAlreadyPlaying) {
            if (isAlreadyPlaying === true){
                window.location.replace("GameRoom.html");
            }
        }
    });
}

function uploadFile() {

    var formData = new FormData();
    formData.append('xmlFileName', $('#xmlFile')[0].files[0]);
    formData.append('dictFileName', $('#dictFile')[0].files[0]);

    $.ajax({
        url: "lobby",
        data: formData,
        type: 'POST',
        processData: false,
        contentType: false,
        enctype: "multipart/form-data",
        dataType: 'json',
        success: function (message) {
            if(message != "success") {
                openPopup(message);
            }
        }
    })
}

function openPopup(msg) {
    $("#message").html(msg);
    $("#popup").show();
}

function closePopup() {
    $("#popup").hide();
    $("#message").removeClass('boardMessage');
}

function openPopupWithBoard(){
    $("#message").addClass('boardMessage');
    $("#message").empty();
    var table = $("<table></table>");
    $("#message").append(table);
    getShowBoard(table);

    $("#popup").show();
}