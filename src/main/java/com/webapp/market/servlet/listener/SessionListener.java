package com.webapp.market.servlet.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;

@WebListener
public class SessionListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent se) {
        //se.getSession().setMaxInactiveInterval(300);
        //System.out.println("Need to load data in new session!!!");
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        //System.out.println("Need to save data!!! Destroyed:Id=" + se.getSession().getId() + " " + new Date());
    }
}
