package com.webapp.market.exceptions;

import com.webapp.market.markets.AbstractWebsocket;
import com.webapp.market.utils.Log;

public class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Log log = new Log(DefaultUncaughtExceptionHandler.class.getName());

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.logSEVERE("DefaultUncaughtException: " + t.getName() + ", " + e.getMessage());
    }
}
