package com.tictactoe;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/logic")
public class LogicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession currentSession = req.getSession();
        Field field = extractField(currentSession);

        int index = getSelectIndex(req);
        Sign currentSign = field.getField().get(index);

        if (currentSign != Sign.EMPTY) {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/index.jsp");
            requestDispatcher.forward(req, resp);
            return;
        }

        field.getField().put(index, Sign.CROSS);
        if (checkWin(currentSession, field, resp)) {
            return;
        }

        int emptyFieldIndex = field.getEmptyFieldIndex();
        if (emptyFieldIndex >= 0) {
            field.getField().put(emptyFieldIndex, Sign.NOUGHT);
            if (checkWin(currentSession, field, resp)) {
                return;
            }
        } else {
            currentSession.setAttribute("draw", true);
        }

        List<Sign> data = field.getFieldData();

        currentSession.setAttribute("data", data);
        currentSession.setAttribute("field", field);

        resp.sendRedirect("/index.jsp");
    }

    private boolean checkWin(HttpSession session, Field field, HttpServletResponse resp) throws IOException {
        Sign winner = field.checkWin();

        if (Sign.CROSS == winner || Sign.NOUGHT == winner) {
            session.setAttribute("winner", winner);
            List<Sign> data = field.getFieldData();
            session.setAttribute("data", data);
            resp.sendRedirect("/index.jsp");
            return true;
        }

        return false;
    }

    private Field extractField(HttpSession currentSession) {
        Object fieldAttribute = currentSession.getAttribute("field");

        if (Field.class != fieldAttribute.getClass()) {
            currentSession.invalidate();
            throw new RuntimeException("Session is broken, please try again.");
        }
        return (Field) fieldAttribute;
    }

    private int getSelectIndex(HttpServletRequest req) {
        String click = req.getParameter("click");
        boolean isDigit = click.chars().allMatch(Character::isDigit);
        return isDigit ? Integer.parseInt(click) : 0;
    }
}
