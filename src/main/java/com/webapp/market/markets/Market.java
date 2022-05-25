package com.webapp.market.markets;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.exceptions.ExmoException;
import com.webapp.market.markets.exmo.WsExmoMarginAuthenticated;
import com.webapp.market.strategies.Strategy;
import com.webapp.market.utils.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Market extends Thread {

    private static final Log log = new Log(Market.class.getName());

    private AtomicReference<DAOFactory> dao = new AtomicReference<>(DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL));

    private List<Strategy> strategies = new ArrayList<>();

    private Map<String, Object> walletUSDT;
    private Map<String, Object> position;
    private Map<String, Object> limitOrderSell;
    private Map<String, Object> limitOrderBuy;


    WsExmoMarginAuthenticated clientWsExmoMA;
    private Exchange exchange;

    private String login;

    private static int leverage = 2;

    private boolean isActive;

    public void disable(){
        isActive=false;
    }

    public Market(String login, Exchange exchange) {
        this.login = login;
        this.exchange = exchange;
        clientWsExmoMA = new WsExmoMarginAuthenticated();

        walletUSDT = clientWsExmoMA.getWalletUSDT();
        position = clientWsExmoMA.getPosition();
        limitOrderSell = clientWsExmoMA.getLimitOrderSell();
        limitOrderBuy = clientWsExmoMA.getLimitOrderBuy();
    }

    public void addStrategy(Strategy strategy) {
        strategy.setMarket(this);
        strategy.setLogin(login);
        strategies.add(strategy);
    }

    public double getQuantityShortPosition() {
        if (position.containsKey("closed")
                && !((Boolean) position.get("closed"))
                && position.containsKey("type")
                && position.get("type").equals("short")) {
            return Double.parseDouble(position.containsKey("quantity") ? (String) position.get("quantity") : "0.0");
        }
        return 0.0;
    }

    public double getQuantityLongPosition() {
        if (position.containsKey("closed")
                && !((Boolean) position.get("closed"))
                && position.containsKey("type")
                && position.get("type").equals("long")) {
            return Double.parseDouble(position.containsKey("quantity") ? (String) position.get("quantity") : "0.0");
        }
        return 0.0;
    }

    public double getQuantityBalance(double price) {
        double balance = Double.parseDouble(walletUSDT.containsKey("balance") ? (String) walletUSDT.get("balance") : "0.0");
        return balance * leverage / price;
    }

    public String createOrderSell(double price, double quantity) throws ExmoException {
        String status = limitOrderSell.containsKey("status") ? (String) limitOrderSell.get("status") : "";
        String order_id = limitOrderSell.containsKey("order_id") ? (String) limitOrderSell.get("order_id") : "";
        if (quantity >= 0.00002) {
            //System.out.println("status: " + status);
            if (status != null && status.equals("active")) {
                return exchange.orderUpdate(order_id, quantity, price);
            } else {
                return exchange.orderSellCreate(leverage, quantity, price);
            }
        } else {
            if (status != null && status.equals("active")) {
                return exchange.orderCancel(order_id);
            }
        }
        return "Order hasn't created.";
    }

    public String createOrderBuy(double price, double quantity) throws ExmoException {
        String status = limitOrderBuy.containsKey("status") ? (String) limitOrderBuy.get("status") : "";
        String order_id = limitOrderBuy.containsKey("order_id") ? (String) limitOrderBuy.get("order_id") : "";
        if (quantity >= 0.00002) {
            //System.out.println("status: " + status);
            if (status != null && status.equals("active")) {
                return exchange.orderUpdate(order_id, quantity, price);
            } else {
                return exchange.orderBuyCreate(leverage, quantity, price);
            }
        } else {
            if (status != null && status.equals("active")) {
                return exchange.orderCancel(order_id);
            }
        }
        return "Order hasn't created.";
    }

    public String orderSellCancel() throws ExmoException {
        if (limitOrderSell.containsKey("order_id")) {
            String order_id = (String) limitOrderSell.get("order_id");
            if (order_id != null && !order_id.equals(""))
                return exchange.orderCancel(order_id);
            else
                return "limitOrderSell hasn't been found";
        }
        return "limitOrderSell doesn't contain \"order_id\"";
    }

    public String orderBuyCancel() throws ExmoException {
        if (limitOrderBuy.containsKey("order_id")) {
            String order_id = (String) limitOrderBuy.get("order_id");
            if (order_id != null && !order_id.equals(""))
                return exchange.orderCancel(order_id);
            else
                return "limitOrderBuy hasn't been found";
        }
        return "limitOrderBuy doesn't contain \"order_id\"";
    }

    @Override
    public void run() {
        isActive = true;
        try {
            clientWsExmoMA.connect("wss://ws-api.exmo.com:443/v1/margin/private");
            executeStrategies();
            interruptStrategies();
            clientWsExmoMA.disconnect();
        } catch (ExmoException e) {
            e.printStackTrace();
        }
    }

    public void executeStrategies() {
        log.logINFO(dao, login, "The strategies are started");
        try {
            long lastTime = new Date().getTime();
            while (isActive) {
                for (Strategy strategy : strategies) {
                    long nowTime = new Date().getTime();
                    while (nowTime - lastTime < 1000) {
                        nowTime = new Date().getTime();
                    }
                    lastTime = nowTime;
                    strategy.execute();
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException ignore) {}
    }

    public void interruptStrategies() {
        for (int i = 0; i < strategies.size(); i++) {
            strategies.get(i).interrupt();
        }
    }

    public Map<String, Object> getWalletUSDT() {
        return walletUSDT;
    }

    public Map<String, Object> getPosition() {
        return position;
    }

    public Map<String, Object> getLimitOrderSell() {
        return limitOrderSell;
    }

    public Map<String, Object> getLimitOrderBuy() {
        return limitOrderBuy;
    }
}
