list = [];
function getShowBoard(table){

    var actionType = "getShowBoard";
    var gameTitle = $("#gameTable").find("tr.success").attr('value');

    $.ajax({
        url: "gamingRoom",
        data: {
            "gameTitle": gameTitle,
            "ActionType": actionType
        },
        success: function (board) {
            createBoard(board, table, actionType);
        },
        error: function (data) {
            console.log(data);
        }
    });
}

function getBoard(table) {
    var actionType = "getBoard";

    $.ajax({
        url: "gamingRoom",
        data: {
            "ActionType": actionType
        },
        success: function (board) {

            createBoard(board, table, actionType);

        },
        error: function (data) {
            console.log(data);
        }
    });
}

function createBoard(theBoard,table,actionType) {
    $('#board').empty();
    console.log("board create");

    table.addClass("myTable");
    createBoardButtons(theBoard, table, actionType);
}


function createBoardButtons(board, table, action) {
    var columns = board.length;
    var rows = board.length;

    for (var row = 0; row < rows; row++) {

        //A <tr> element contains one or more <th> or <td> elements.
        var tr = document.createElement('tr');
        for (var column = 0; column < columns; column++) {

            //Standard cells - contains data (created with the <td> element)
            var td = document.createElement('td');
            setRowCol(td, row, column);

            var btn = document.createElement("button");
            setRowCol(btn, row + 1, column + 1);
            $(btn).addClass("boardBtn");
            $(btn).attr("id", "buttonBoard" + row + "_" + column);
            if(action !== "getShowBoard") {
                setButtonOnClickFunc(btn, row, column);
            }

            var boardBtn = board[row][column];

            setButtonColor(boardBtn , btn);
            setButtonSymbol(boardBtn , btn);

            td.appendChild(btn);
            tr.appendChild(td);
        }

        table.append(tr);
    }
}

function setRowCol(elm, row, col){
    elm.setAttribute("column",col);
    elm.setAttribute("row",row);
}

function setButtonColor(buttonObject, btn){

    switch(buttonObject.m_Color) {
        case "BLACK":
            $(btn).addClass("blackBtn");
            break;
        case "BLUE":
            $(btn).addClass("blueBtn");
            break;
        case "RED":
            $(btn).addClass("redBtn");
            break;
        case "GREEN":
            $(btn).addClass("greenBtn");
            break;
        case "MAGENTA":
            $(btn).addClass("mangentaBtn");
            break;
        case "ORANGE":
            $(btn).addClass("orangeBtn");
            break;
        case "PURPLE":
            $(btn).addClass("purpleBtn");
            break;
        default:
            $(btn).addClass("greyBtn");
    }
}

function setButtonSymbol(buttonObject, btn){
    if (buttonObject.isShown === true) {
        btn.innerText = buttonObject.sign + "(" + buttonObject.score + ")";
    }
    else {
        btn.innerText = "";
    }
}

function addToList(btnClicked) {
    var btn = btnClicked.currentTarget;
    list.push([btn.getAttribute("row"), btn.getAttribute("column")]);
    btn.classList.add("blueColor");
    btn.on("click", removeFromList);
}

function removeFromList(btnClicked) {
    var btn = btnClicked.currentTarget;
    var loc = [btn.getAttribute("row"), btn.getAttribute("column")];
    var index = indexOf(list, btn.getAttribute("row"), btn.getAttribute("column"));
    if (index >= 0) {
        list.removeChild(index);
        btn.classList.add("blueColor");
    }
    btn.on("click", addToList);
}

function updateBoard_(board) {
    var columns = board.length;
    var rows = board.length;

    for (var row = 0; row < rows; row++) {
        for (var column = 0; column < columns; column++) {
            var btn = document.getElementById("buttonBoard" + row + "_" + column);
            var boardBtn = board[row][column];
            setButtonSymbol(boardBtn, btn);
            setButtonOnClickFunc(btn, row, column);
        }
    }
}

function indexOf(list, row, col) {
    for (var i = 0; i < list.length; i++) {
        if (list[i][0] === row + 1 && list[i][1] === col + 1) {
            return i;
        }
    }
    return -1;
}

function setButtonOnClickFunc(btn, row, column) {
    if (indexOf(list, row, column) >= 0) {
        $(btn).on("click", removeFromList);
    }
    else {
        $(btn).on("click", addToList);
    }
}