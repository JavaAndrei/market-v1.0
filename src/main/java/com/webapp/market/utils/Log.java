package com.webapp.market.utils;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.dao.entities.RegisterAction;
import com.webapp.market.dao.entities.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Log {

    private Logger LOGGER;

    static {
        try (FileInputStream ins = new FileInputStream("C:\\Users\\Andrey\\IdeaProjects\\market\\log.config")) { //полный путь до файла с конфигами
            LogManager.getLogManager().readConfiguration(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Log(String name) {
        this.LOGGER = Logger.getLogger(name);
    }

    //SEVERE (highest value)
    //WARNING
    //INFO
    //CONFIG
    //FINE
    //FINER
    //FINEST (lowest value)

    public void logSEVERE(String message) {
        LOGGER.log(Level.SEVERE, message);
    }

    public void logSEVERE(AtomicReference<DAOFactory> dao, String login, String message) {
        LOGGER.log(Level.SEVERE, message);
        dao.get().getRegisterActionDAO().insertRegisterAction(new RegisterAction(new User(login), new Date(), "SEVERE:" + message));
    }

    public void logWARNING(String message) {
        LOGGER.log(Level.WARNING, message);
    }

    public void logWARNING(AtomicReference<DAOFactory> dao, String login, String message) {
        LOGGER.log(Level.WARNING, message);
        dao.get().getRegisterActionDAO().insertRegisterAction(new RegisterAction(new User(login), new Date(), "WARNING:" + message));
    }

    public void logINFO(String message) {
        LOGGER.log(Level.INFO, message);
    }

    public void logINFO(AtomicReference<DAOFactory> dao, String login, String message) {
        LOGGER.log(Level.INFO, message);
        dao.get().getRegisterActionDAO().insertRegisterAction(new RegisterAction(new User(login), new Date(), "INFO:" + message));
    }
}
