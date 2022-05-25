package com.webapp.market.markets;

import com.webapp.market.exceptions.ExmoException;

import java.util.HashMap;

public interface Exchange {

    String orderBuyCreate(int leverage, double quantity, double price) throws ExmoException;

    String orderSellCreate(int leverage, double quantity, double price) throws ExmoException;

    String orderUpdate(String order_id, double quantity, double price) throws ExmoException;

    String orderCancel(String order_id) throws ExmoException;
}
