package servelet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="GameServlet", urlPatterns = {"/utils-room"})
public class GameServlet extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String action = request.getParameter(Constants.ACTION_TYPE);
        switch (action) {
            case Constants.DO_MOVE:
                handleDoMove(request,response);
                break;
            case Constants.GAME_STATUS:
                gameStatus(request, response);
                break;
            case Constants.IS_GAME_STARTED:
                isGameStarted(request, response);
                break;
            case Constants.EXIT_GAME:
                exitGame(request, response);
                break;
            case Constants.GET_BOARD:
                getBoard(request, response);
                break;
            case Constants.GET_SHOW_BOARD:
                getShowBoard(request, response);
                break;
            case Constants.IS_GAME_DONE:
                isGameDone(request,response);
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
