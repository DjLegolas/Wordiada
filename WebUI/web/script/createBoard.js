
function getShowBoard(table){

    var actionType = "getShowBoard";
    var gamettl = $("#gameTable").find("tr.success").attr('value');

    $.ajax({
        url: "gamingRoom",
        data: {
            "gameTitle": gamettl,
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
    var columns = board.m_BoardSize;
    var rows = board.m_BoardSize;

    for (var row = 0; row < rows; row++) {

        //A <tr> element contains one or more <th> or <td> elements.
        var tr = document.createElement('tr');
        for (var column = 0; column < columns; column++) {

            //Standard cells - contains data (created with the <td> element)
            var td = document.createElement('td');
            setRowCol(td, row, column);

            var btn = document.createElement("button");
            setRowCol(btn, row, column);
            $(btn).addClass( "boardBtn");
            if(action != "getShowBoard") {
                $(btn).on("click", ajaxBoardBtnClicked);
            }

            var boardBtn = board.m_Board[row][column];

            setButtonColor(boardBtn , btn);
            setButtonSymbol( boardBtn , btn );

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
            $(btn).addClass( "blackBtn");
            break;
        case "BLUE":
            $(btn).addClass( "blueBtn");
            break;
        case "RED":
            $(btn).addClass( "redBtn");
            break;
        case "GREEN":
            $(btn).addClass( "greenBtn");
            break;
        case "MAGENTA":
            $(btn).addClass( "mangentaBtn");
            break;
        case "ORANGE":
            $(btn).addClass( "orangeBtn");
            break;
        case "PURPLE":
            $(btn).addClass( "purpleBtn");
            break;
        default:
            $(btn).addClass( "greyBtn");
    }
}

function setButtonSymbol(buttonObject, btn){

    btn.innerText = buttonObject.m_SquareSymbol;

}
