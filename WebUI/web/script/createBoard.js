selectedTilesList = [];
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

function createBoard(theBoard, table, actionType) {
    $('#board').empty();
    console.log("board create");

    table.addClass("myTable");
    createBoardButtons(theBoard, table, actionType);
}


function createBoardButtons(board, table, action) {
    var columns = board.length;
    var rows = board.length;

    for (var row = 1; row <= rows; row++) {

        //A <tr> element contains one or more <th> or <td> elements.
        var tr = document.createElement('tr');
        for (var column = 1; column <= columns; column++) {

            //Standard cells - contains data (created with the <td> element)
            var td = document.createElement('td');
            setRowCol(td, row, column);

            var btn = document.createElement("button");
            setRowCol(btn, row, column);
            $(btn).addClass("boardBtn");
            $(btn).attr("id", "buttonBoard" + row + "_" + column);
            if(action !== "getShowBoard") {
                setButtonOnClickFunc(btn, row, column);
                var boardBtn = board[row - 1][column - 1];

                setButtonSymbol(boardBtn , btn);
            }


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
    var row = btn.getAttribute("row");
    var col = btn.getAttribute("column");
    selectedTilesList.push([btn.getAttribute("row"), btn.getAttribute("column")]);
    setButtonOnClickFunc(btn, row, col);
    if (showWord) {
        buildWord();
    }
}

function removeFromList(btnClicked) {
    var btn = btnClicked.currentTarget;
    var row = btn.getAttribute("row");
    var col = btn.getAttribute("column");
    var index = indexOf(selectedTilesList, row, col);
    selectedTilesList.splice(index, 1);
    setButtonOnClickFunc(btn, row, col);
    if (showWord) {
        buildWord();
    }
}

function updateBoard_(board) {
    var columns = board.length;
    var rows = board.length;

    for (var row = 1; row <= rows; row++) {
        for (var column = 1; column <= columns; column++) {
            var btn = document.getElementById("buttonBoard" + row + "_" + column);
            var boardBtn = board[row - 1][column - 1];
            setButtonSymbol(boardBtn, btn);
            setButtonOnClickFunc(btn, row, column);
        }
    }
}

function indexOf(list, row, col) {
    row = row.toString();
    col = col.toString();
    for (var i = 0; i < selectedTilesList.length; i++) {
        if (list[i][0] === row && list[i][1] === col) {
            return i;
        }
    }
    return -1;
}

function setButtonOnClickFunc(btn, row, column) {
    if (indexOf(selectedTilesList, row, column) >= 0) {
        btn.classList.remove("greyBtn");
        btn.classList.add("blueBtn");
        $(btn).off("click").on("click", removeFromList);
    }
    else {
        btn.classList.remove("blueBtn");
        btn.classList.add("greyBtn");
        $(btn).off("click").on("click", addToList);
    }
}

function buildWord() {
    selectedWord = "";
    var toRemove = [];
    selectedTilesList.forEach(function (val, index) {
        var char = $("#buttonBoard" + val[0] + "_" + val[1]).text()[0];
        if (char === undefined || char === '') {
            toRemove.push(index);
        }
        else {
            selectedWord += char;
        }
    });
    $("#labelSelectedWord").text(selectedWord);
    toRemove.forEach(function (index) {
        selectedTilesList.splice(index, 1);
    });
}