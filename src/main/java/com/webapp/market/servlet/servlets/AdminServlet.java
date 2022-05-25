package com.webapp.market.servlet.servlets;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.dao.entities.Role;
import com.webapp.market.dao.entities.User;
import com.webapp.market.exceptions.UserDataFormatException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@WebServlet(name = "adminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        @SuppressWarnings("unchecked")
        AtomicReference<DAOFactory> dao = (AtomicReference<DAOFactory>) req.getServletContext().getAttribute("dao");
        List<User> users = null;
        HttpSession session = req.getSession();
        try {
            users = dao.get().getUserDAO().getAllUsers();
        } catch (UserDataFormatException e) {
            session.setAttribute("error", e.getMessage());
        }
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getLogin().compareTo(o2.getLogin());
            }
        });
        User user = (User) session.getAttribute("user");
        session.removeAttribute("user");
        req.setAttribute("user", user);
        req.setAttribute("userNames", users);

        req.setAttribute("error", session.getAttribute("error"));
        session.removeAttribute("error");

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/admin.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*if (req.getParameter("ws").equals("binancestart")) {
            WsBinance.getInstance().connect("wss://stream.binance.com:9443/ws/btcusdt@trade");
            resp.sendRedirect(req.getContextPath() + "/admin");
        } else if (req.getParameter("ws").equals("binancestop")) {
            WsBinance.getInstance().disconnect();
            resp.sendRedirect(req.getContextPath() + "/admin");
        } else if (req.getParameter("ws").equals("exmostart")) {
            WsExmo.getInstance().connect("wss://ws-api.exmo.com:443/v1/public");
            resp.sendRedirect(req.getContextPath() + "/admin");
        } else if (req.getParameter("ws").equals("exmostop")) {
            WsExmo.getInstance().disconnect();
            resp.sendRedirect(req.getContextPath() + "/admin");
        }*/

        HttpSession session = req.getSession();

        String strId = req.getParameter("id");
//        id = strId.matches("\\d*") ? Integer.parseInt(strId) : 0;
//        String login = req.getParameter("newLogin");
//        String password = req.getParameter("newPassword");
//        String role = req.getParameter("newRole");

        User user = new User(
                strId.matches("\\d{1,}") ? Integer.parseInt(strId) : 0,
                req.getParameter("newLogin"),
                req.getParameter("newPassword"),
                new Role(req.getParameter("newRole"))
        );

        @SuppressWarnings("unchecked")
        AtomicReference<DAOFactory> dao = (AtomicReference<DAOFactory>) req.getServletContext().getAttribute("dao");

        try {
            if (req.getParameter("action").equals("create")) {
                dao.get().getUserDAO().insertUser(user);
            } else if (req.getParameter("action").equals("read")) {
                readUser(session, user, dao);
            } else if (req.getParameter("action").equals("update")) {
                dao.get().getUserDAO().updateUserById(user);
            } else if (req.getParameter("action").equals("delete")) {
                dao.get().getUserDAO().deleteUserById(user.getId());
            }
        } catch (UserDataFormatException e) {
            session.setAttribute("error", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin");
    }

    private void readUser(HttpSession session, User user, AtomicReference<DAOFactory> dao) throws UserDataFormatException {
        User result = dao.get().getUserDAO().readUserById(user.getId());
        if (result == null) {
            session.setAttribute("error", "Error: Can't read. That \"id\" doesn't exist.");
        } else {
            session.setAttribute("user", result);
        }
    }
}
