package com.webapp.market.json;

import java.util.List;

public class OrderBook {
    private static double bestAsk;
    private static double bestBid;
    private static double nearestAsk;
    private static double nearestBid;
    private static double bigAsk;
    private static double bigBid;
    private static double spyAsk;
    private static double spyBid;
    private static long time;

    private static List<List<Double>> ask;
    private static List<List<Double>> bid;

    public static double getBestAsk() {
        return ask.get(0).get(0);
    }

    public static double getBestBid() {
        return bid.get(0).get(0);
    }

    public static double getSpyAsk(double volumeOfAsk) {
        for (int i = 0; i < ask.size(); i++) {
            if (ask.get(i).get(1) == volumeOfAsk) {
                spyAsk = ask.get(i).get(0);
                return spyAsk;
            }
        }
        //spyAsk = -1;
        return spyAsk + 100;
    }

    public static double getSpyBid(double volumeOfAsk) {
        for (int i = 0; i < bid.size(); i++) {
            if (bid.get(i).get(1) == volumeOfAsk) {
                spyBid = bid.get(i).get(0);
                return spyBid;
            }
        }
        //spyBid = -1;
        return spyBid - 100;
    }

    public static double getBigAsk(double limit) {
        double result = ask.get(0).get(0);
        double max = ask.get(0).get(1);
        for (int i = 0; i < ask.size(); i++) {
            if (ask.get(i).get(1) > max) {
                result = ask.get(i).get(0);
                max = ask.get(i).get(1);
                if (max >= limit) {
                    return result;
                }
            }
        }
        return -1;
    }

    public static double getBigBid(double limit) {
        double result = bid.get(0).get(0);
        double max = bid.get(0).get(1);
        for (int i = 0; i < bid.size(); i++) {
            if (bid.get(i).get(1) > max) {
                result = bid.get(i).get(0);
                max = bid.get(i).get(1);
                if (max >= limit) {
                    return result;
                }
            }
        }
        return -1;
    }

    public static double getNearestAsk(double price) {
        double result = price;
        double curentAsk;
        for (int i = ask.size() - 1; i >= 0 ; i--) {
            curentAsk = ask.get(i).get(0);
            if (curentAsk >= price) {
                result = curentAsk - 0.01;
            } else {
                return result;
            }
        }
        if (result > getBestBid()) {
            return result;
        } else {
            return getBestBid() + 0.01;
        }
    }

    public static double getNearestBid(double price) {
        double result = price;
        double curentBid;
        for (int i = bid.size() - 1; i >= 0 ; i--) {
            curentBid = bid.get(i).get(0);
            if (curentBid <= price) {
                result = curentBid + 0.01;
            } else {
                return result;
            }
        }
        if (result < getBestAsk()) {
            return result;
        } else {
            return getBestAsk() - 0.01;
        }
    }

    public static List<List<Double>> getAsk() {
        return ask;
    }

    public void setAsk(List<List<Double>> ask) {
        this.ask = ask;
    }

    public static List<List<Double>> getBid() {
        return bid;
    }

    public void setBid(List<List<Double>> bid) {
        this.bid = bid;
    }

    public static long getTime() {
        return time;
    }

    public static void setTime(long time) {
        OrderBook.time = time;
    }
}
