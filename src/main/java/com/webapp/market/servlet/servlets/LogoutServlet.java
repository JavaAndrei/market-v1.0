package com.webapp.market.servlet.servlets;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.utils.Log;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@WebServlet(name = "logoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {

    private static final Log log = new Log(LogoutServlet.class.getName());

    public void init() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String login = (String) req.getSession().getAttribute("login");
        @SuppressWarnings("unchecked")
        AtomicReference<DAOFactory> dao = (AtomicReference<DAOFactory>) req.getServletContext().getAttribute("dao");
        if (login != null)
            log.logINFO(dao, login, String.format("The user \"%s\" has logged out", login));
        session.invalidate();
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }

    @Override
    public void destroy() {
    }
}
