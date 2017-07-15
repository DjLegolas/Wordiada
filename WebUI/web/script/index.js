$(document).ready(function (){

    $('#buttonLogin').on("click", checkUserNameAndType);
    $("#radioHuman").prop("checked", true);

    //ajaxIsAlreadyLogIn();
});

function checkUserNameAndType(){

    var inputFromUser = $('#inputFromUser').val();

    if(inputFromUser == ""){
        openPopup("Please fill User Name.");
    }
    else {
        var UserType = $('input[name=UserType]:checked').val();
        var actionType = "PerformLogIn";

        $.ajax({
            url: "login",
            data:{
                "username": inputFromUser,
                "usertype": UserType,
                "ActionType": actionType
            },
            success: function(isNameExist) {
                if (isNameExist === true){
                    openPopup("The user was already registered.");
                } else {
                    window.location.replace("lobby.html");
                }
            }
        });
    }
}

function ajaxIsAlreadyLogIn() {

    var actionType = "CheckUserLogIn";

    $.ajax({
        url: "login",
        data:{
            "ActionType": actionType
        },
        success: function(isAlreadyLogIn) {
            if (isAlreadyLogIn === true){
                window.location.replace("lobby.html");
            }
        }
    });
}

function openPopup(msg) {
    $("#message").html(msg);
    $("#popup").show();
}

function closePopup() {
    $("#popup").hide();
}