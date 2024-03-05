package com.tictactoe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "InitServlet", value = "/start")
public class InitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //create playing session
        HttpSession session = req.getSession(true);
        //create field game
        Field field = new Field();
        Map<Integer, Sign> fieldData = field.getField();
        //get field's values
        List<Sign> data = field.getFieldData();
        //add field's value in session
        session.setAttribute("field", field);
        //add field's value as sort in index
        session.setAttribute("data", data);

        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
