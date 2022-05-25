package com.webapp.market.json;

public class TargetPrice {

    private static double value;
    private static long time;

    public static double getValue() {
        return value;
    }

    public static void setValue(double value) {
        TargetPrice.value = value;
    }

    public static long getTime() {
        return time;
    }

    public static void setTime(long time) {
        TargetPrice.time = time;
    }
}
