package com.webapp.market.servlet.filters;

import com.webapp.market.markets.ConnectionPool;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(urlPatterns = { "/bot", "/bot_keys" })
public class BotFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession();
        String servletPath = req.getServletPath();

        String login = (String) session.getAttribute("login");
        if (ConnectionPool.isMarketRunning(login)) {
            if (servletPath.equals("/bot"))
                filterChain.doFilter(req, resp);
            else
                resp.sendRedirect(req.getContextPath() + "/bot");
        } else {
            if (servletPath.equals("/bot_keys"))
                filterChain.doFilter(req, resp);
            else
                resp.sendRedirect(req.getContextPath() + "/bot_keys");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
