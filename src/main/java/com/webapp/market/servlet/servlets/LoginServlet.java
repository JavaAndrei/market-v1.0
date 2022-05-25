package com.webapp.market.servlet.servlets;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.dao.entities.Role;
import com.webapp.market.dao.entities.User;
import com.webapp.market.exceptions.UserDataFormatException;
import com.webapp.market.markets.ConnectionPool;
import com.webapp.market.utils.Log;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private static final Log log = new Log(LoginServlet.class.getName());

    public void init() {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        req.setAttribute("error", session.getAttribute("error"));
        session.removeAttribute("error");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/login.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        HttpSession session = req.getSession();

        @SuppressWarnings("unchecked")
        AtomicReference<DAOFactory> dao = (AtomicReference<DAOFactory>) req.getServletContext().getAttribute("dao");
        try {
            if (dao.get().getUserDAO().isExist(login, password)) {
                session.setAttribute("login", login);
                session.setAttribute("password", password);
                Role role = dao.get().getUserDAO().findUser(new User(login, password)).getRole();
                req.getSession().setAttribute("role", role);
                log.logINFO(dao, login, String.format("The user \"%s\" has been authenticated from %s:%s", login, req.getRemoteAddr(), req.getRemotePort()));
                if (role.getName().equals("ADMIN")) {
                    resp.sendRedirect(req.getContextPath() + "/admin");
                } else {
                    if (ConnectionPool.isMarketRunning(login))
                        resp.sendRedirect(req.getContextPath() + "/bot");
                    else
                        resp.sendRedirect(req.getContextPath() + "/bot_keys");
                }
            } else {
                throw new UserDataFormatException("Error: Incorrect Login or Password. Type the correct Login and Password, and try again.");
            }
        } catch (UserDataFormatException e) {
            session.setAttribute("error", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/login");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
