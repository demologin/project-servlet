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
        // Получаем текущую сессию
        HttpSession currentSession = req.getSession();
        // Получаем объект игрового поля из сессии
        Field field = extractedField(currentSession);
        // получаем индекс ячейки, по которой произошел клик
        int index = getSelectedIndex(req);
        // получаем символ, находящийся в ячейке, по которой произошел клик
        Sign currentSign = field.getField().get(index);
        // Проверяем, что ячейка, по которой был клик пустая.
        // Иначе ничего не делаем и отправляем пользователя на ту же страницу без изменений параметров в сессии
        if (Sign.EMPTY != currentSign) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(req, resp);
            return;
        }
        // ставим крестик в ячейке, по которой кликнул пользователь
        field.getField().put(index, Sign.CROSS);
        // Проверяем, не победил ли крестик после добавления последнего клика пользователя
        if (checkWin(resp, currentSession, field)) {
            return;
        }
        // Получаем пустую ячейку поля
        int emptyFieldIndex = field.getEmptyFieldIndex();
        //автоматически устанавливаем нолик по индексу пустой ячейки поля
        if (emptyFieldIndex >= 0) {
            field.getField().put(emptyFieldIndex, Sign.NOUGHT);
            // Проверяем, не победил ли нолик после добавление последнего нолика
            if (checkWin(resp, currentSession, field)) {
                return;
            }
        }
        // Если пустой ячейки нет и никто не победил - значит это ничья
        else {
            // Добавляем в сессию флаг, который сигнализирует что произошла ничья
            currentSession.setAttribute("draw", true);
            // Считаем список значков
            List<Sign> data = field.getFieldData();
            // Обновляем этот список в сессии
            currentSession.setAttribute("data", data);
            // Шлем редирект
            resp.sendRedirect("/index.jsp");
            return;
        }
        // Считаем список значков
        List<Sign> data = field.getFieldData();
        // Обновляем объект поля и список значков в сессии
        currentSession.setAttribute("data", data);
        currentSession.setAttribute("field", field);
        //Перенаправляет пользователя на страницу /index.jsp.
        resp.sendRedirect("/index.jsp");
    }

    // метод, который извлекает объект игрового поля из сессии.
    private Field extractedField(HttpSession currentSession) {
        Object fieldAttribute = currentSession.getAttribute("field");
        //Проверяет, что атрибут "field" в текущей сессии является объектом класса Field.
        if (Field.class != fieldAttribute.getClass()) {
            //Если это не так, сессия инвалидируется, и выбрасывается исключение.
            currentSession.invalidate();
            throw new RuntimeException("Session is broken, try one more time");
        }
        return (Field) fieldAttribute;
    }

    private int getSelectedIndex(HttpServletRequest request) {
        String click = request.getParameter("click");
        boolean isNumeric = click.chars().allMatch(Character::isDigit);
        return isNumeric ? Integer.parseInt(click) : 0;
    }

    /**
     * Метод проверяет, нет ли трех крестиков/ноликов в ряд.
     * Возвращает true/false
     */
    private boolean checkWin(HttpServletResponse response, HttpSession currentSession, Field field) throws IOException {
        Sign winner = field.checkWin();
        if (Sign.CROSS == winner || Sign.NOUGHT == winner) {
            // Добавляем флаг, который показывает что кто-то победил
            currentSession.setAttribute("winner", winner);
            // Считаем список значков
            List<Sign> data = field.getFieldData();
            // Обновляем этот список в сессии
            currentSession.setAttribute("data", data);
            // Шлем редирект
            response.sendRedirect("/index.jsp");
            return true;
        }
        return false;
    }
}
