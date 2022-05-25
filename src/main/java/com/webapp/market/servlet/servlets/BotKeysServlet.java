package com.webapp.market.servlet.servlets;

import com.webapp.market.exceptions.ConnectionPoolException;
import com.webapp.market.exceptions.ExmoException;
import com.webapp.market.markets.ConnectionPool;
import com.webapp.market.markets.Market;
import com.webapp.market.markets.exmo.*;
import com.webapp.market.strategies.SimpleStrategyBuy;
import com.webapp.market.strategies.SimpleStrategySell;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "botKeysServlet", value = "/bot_keys")
public class BotKeysServlet extends HttpServlet {

    public void init() {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        req.setAttribute("error", session.getAttribute("error"));
        session.removeAttribute("error");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/bot_keys.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String public_key = req.getParameter("public_key");//= Keys.PUBLIC_KEY.getKey(); //
        String secret_key = req.getParameter("secret_key");//= Keys.SECRET_KEY.getKey(); //
        Security.setPublicKey(public_key);
        Security.setSecretKey(secret_key);

        HttpSession session = req.getSession();

        if (req.getParameter("action").equals("start")) {
            try {
                ExmoMargin.getInstance().userInfo();
                startTrading((String) session.getAttribute("login"));
                resp.sendRedirect(req.getContextPath() + "/bot");
            } catch (ConnectionPoolException e) {
                session.setAttribute("error", e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/bot_keys");
            } catch (ExmoException e) {
                session.setAttribute("error", "Can't start trading. Enter correct keys. " + e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/bot_keys");
            }

        }
    }

    private void startTrading(String login) throws ConnectionPoolException {
        Market market = new Market(login, ExmoMargin.getInstance());
        ConnectionPool.addMarket(login, market);
        market.addStrategy(new SimpleStrategySell());
        market.addStrategy(new SimpleStrategyBuy());
        market.start();
    }
}
