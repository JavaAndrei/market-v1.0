package com.webapp.market.strategies;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.exceptions.ExmoException;
import com.webapp.market.json.*;
import com.webapp.market.markets.Market;
import com.webapp.market.utils.Log;

import java.util.concurrent.atomic.AtomicReference;

public class SimpleStrategyBuy implements Strategy{

    private static final Log log = new Log(SimpleStrategySell.class.getName());

    private AtomicReference<DAOFactory> dao = new AtomicReference<>(DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL));
    private String login;

    private Market market;

    private double targetPrice;
    private double delta;
    private double price;

    public SimpleStrategyBuy() {
    }

    private double getTargetPrice() {
        return TargetPrice.getValue();
    }

    private double getPrice(double price, double delta) {
        return price - delta;
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
        //log.logINFO("The simple strategy buy is started");
        prepareData();
        double quantityBalance = market.getQuantityBalance(price);
        double quantityLong = market.getQuantityLongPosition();
        //check quantity if enough
        try {
            String orderId = market.createOrderBuy(price, quantityBalance - quantityLong);
        } catch (ExmoException e) {
            e.printStackTrace();
        }
        //else
        //?????????
        //System.out.println(new Date().getTime() + " orderBuyId: " + orderId);
    }

    @Override
    public void interrupt() {
        log.logINFO(dao, login, "The simple strategy buy is stopped");
        try {
            System.out.println(market.orderBuyCancel());
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
