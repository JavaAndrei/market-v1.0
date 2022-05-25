package com.webapp.market.servlet.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(urlPatterns = { "/*" })
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession();
        String servletPath = req.getServletPath();

        if (session != null && session.getAttribute("login") != null && session.getAttribute("password") != null) {

            if (servletPath.equals("/registration") || servletPath.equals("/login")) {
                PrintWriter printWriter = resp.getWriter();
                printWriter.println("You have already registered and logged in");
            } else {
                filterChain.doFilter(req, resp);
            }
        } else {
            if (servletPath.equals("/index.jsp")
                    || servletPath.equals("/registration")
                    || servletPath.equals("/login")) {
                filterChain.doFilter(req, resp);
                return;
            } else if (servletPath.equals("/service")) {
                PrintWriter printWriter = resp.getWriter();
                printWriter.println("You should Login or Register");
            } else {
                resp.sendRedirect(req.getContextPath() + "/login");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
