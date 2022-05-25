package com.webapp.market.servlet.listener;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.exceptions.ExmoException;
import com.webapp.market.exceptions.DefaultUncaughtExceptionHandler;
import com.webapp.market.markets.ConnectionPool;
import com.webapp.market.utils.Log;
import com.webapp.market.markets.WsStarter;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.atomic.AtomicReference;

@WebListener
public class ContextListener implements ServletContextListener {

    private static final Log log = new Log(ContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();
        Thread.setDefaultUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler());


        AtomicReference<DAOFactory> dao = new AtomicReference<>(DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL));
        servletContext.setAttribute("dao", dao);

        ConnectionPool ConnectionPool = null;
        AtomicReference<ConnectionPool> connPool = new AtomicReference<>(ConnectionPool);
        servletContext.setAttribute("connPool", connPool);

        log.logINFO(dao, "System", String.format("The program's started on %s", servletContext.getServerInfo()));

        log.logINFO(dao, "System", String.format("Database \"%s\" has connected", dao.get().getNameDB()));

        try {
            WsStarter.start();
        } catch (ExmoException e) {
            System.exit(3);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
