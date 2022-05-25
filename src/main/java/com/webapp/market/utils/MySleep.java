package com.webapp.market.utils;

public class MySleep {
    public static void sleep(long msec) {
        try {
            Thread.sleep(msec);
        } catch (Exception ignore) {}
    }
}
