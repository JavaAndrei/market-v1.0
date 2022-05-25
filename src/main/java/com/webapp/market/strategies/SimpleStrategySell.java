package com.webapp.market.strategies;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.exceptions.ExmoException;
import com.webapp.market.json.*;
import com.webapp.market.markets.Market;
import com.webapp.market.utils.Log;

import java.util.concurrent.atomic.AtomicReference;

public class SimpleStrategySell implements Strategy{

    private static final Log log = new Log(SimpleStrategySell.class.getName());

    private AtomicReference<DAOFactory> dao = new AtomicReference<>(DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL));
    private String login;

    private Market market;

    private double targetPrice;
    private double delta;
    private double price;

    public SimpleStrategySell() {
    }

    private double getTargetPrice() {
        return TargetPrice.getValue();
    }

    private double getPrice(double price, double delta) {
        return price + delta;
    }

    private double getDelta() {
        return 200.0;
    }

    private void prepareData() {
        delta = getDelta();
        targetPrice = getTargetPrice();
        price = getPrice(targetPrice, delta);
    }

    @Override
    public void execute() {
        //log.logINFO("The simple strategy sell is started");
        prepareData();
        double quantityBalance = market.getQuantityBalance(price);
        double quantityShort = market.getQuantityShortPosition();
        double quantityLong = market.getQuantityLongPosition();
        //check quantity if enough
        try {
            String orderId = market.createOrderSell(price, quantityLong);
        } catch (ExmoException e) {
            e.printStackTrace();
        }
        //else
        //?????????
        //System.out.println(new Date().getTime() + " orderSellId: " + orderId);
    }

    @Override
    public void interrupt() {
        log.logINFO(dao, login, "The simple strategy sell is stopped");
        try {
            System.out.println(market.orderSellCancel());
        } catch (ExmoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMarket(Market market) {
        this.market = market;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
