package com.webapp.market.servlet.servlets;

import com.webapp.market.markets.ConnectionPool;
import com.webapp.market.markets.Market;
import com.webapp.market.utils.MySleep;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "botServlet", value = "/bot")
public class BotServlet extends HttpServlet {

    HttpSession session;

    public void init() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/bot.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        session = req.getSession();
        if (req.getParameter("action").equals("finish")) {
            stopTrading();
            resp.sendRedirect(req.getContextPath() + "/bot_keys");
        }
    }

    private void stopTrading() {
        System.out.println("stopTrading");

        ///Market market = (Market) session.getAttribute("market");
        //Thread marketThread = (Thread) session.getAttribute("market_thread");
        String login = (String) session.getAttribute("login");
        if (ConnectionPool.isMarketRunning(login)) {
            Market market = ConnectionPool.getMarket(login);
            market.disable();

            while (market.isAlive()) {
                MySleep.sleep(200L);
            }

            ConnectionPool.removeMarket(login);
        }
    }
}
