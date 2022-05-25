package com.webapp.market.servlet.servlets;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.dao.entities.Role;
import com.webapp.market.dao.entities.User;
import com.webapp.market.exceptions.UserDataFormatException;
import com.webapp.market.utils.Log;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@WebServlet(name = "registrationServlet", value = "/registration")
public class RegistrationServlet extends HttpServlet {

    private static final Log log = new Log(RegistrationServlet.class.getName());

    public void init() {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        req.setAttribute("error", session.getAttribute("error"));
        session.removeAttribute("error");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/registration.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("newLogin");
        String password = req.getParameter("newPassword");

        HttpSession session = req.getSession();
        @SuppressWarnings("unchecked")
        AtomicReference<DAOFactory> dao = (AtomicReference<DAOFactory>) req.getServletContext().getAttribute("dao");

        try {
            dao.get().getUserDAO().insertUser(new User(login, password, new Role("USER")));
            session.setAttribute("login", login);
            session.setAttribute("password", password);
            session.setAttribute("role", "USER");
            log.logINFO(dao, login, String.format("The user \"%s\" has created", login));
            resp.sendRedirect(req.getContextPath() + "/bot_keys");
        } catch (UserDataFormatException e) {
            session.setAttribute("error", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/registration");
        }
    }
}
