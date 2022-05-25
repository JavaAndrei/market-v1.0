package com.webapp.market.markets;

import com.webapp.market.exceptions.ExmoException;
import com.webapp.market.markets.binance.WsBinance;
import com.webapp.market.markets.exmo.WsExmo;

public class WsStarter {
    private static final WsBinance wsBinance = WsBinance.getInstance();
    private static WsExmo wsExmo = WsExmo.getInstance();

    public WsStarter() {
    }

    public static void start() throws ExmoException {
        try {
            // open websocket

            wsBinance.connect("wss://stream.binance.com:9443/ws/btcusdt@trade");

            wsExmo.connect("wss://ws-api.exmo.com:443/v1/public");

            Thread.sleep(1000);
            //clientWsBinance.sendMessage("Stop");

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (RuntimeException ex) {
            System.err.println("RuntimeException exception: " + ex.getMessage());
        }
    }
}
