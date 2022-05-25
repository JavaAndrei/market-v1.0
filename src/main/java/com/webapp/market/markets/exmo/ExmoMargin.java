package com.webapp.market.markets.exmo;

import com.webapp.market.exceptions.ExmoException;
import com.webapp.market.markets.Exchange;

import java.util.HashMap;

public class ExmoMargin extends Exmo implements Exchange {

    private static ExmoMargin instance;

    private ExmoMargin() {
        super();
    }

    public static ExmoMargin getInstance() {
        if (instance == null) {
            instance = new ExmoMargin();
        }
        return instance;
    }

    private static String pair = "BTC_USDT";

    public String orderBuyCreate(int leverage, double quantity, double price) throws ExmoException {
        //System.out.printf("b    q = %f    p = %f\n", quantity, price);
        String result = request("margin/user/order/create", new HashMap<String, String>() {{
            put("pair", pair);
            put("leverage", String.valueOf(leverage));
            put("type", "limit_buy");
            put("quantity", String.format("%.6f", Math.floor(quantity * 1000000) / 1000000));
            put("price", String.format("%.2f", price));
        }});
        return result;
    }

    public String orderSellCreate(int leverage, double quantity, double price) throws ExmoException {
        //System.out.printf("s    q = %f    p = %f\n", quantity, price);

        String result = request("margin/user/order/create", new HashMap<String, String>() {{
            put("pair", pair);
            put("leverage", String.valueOf(leverage));
            put("type", "limit_sell");
            put("quantity", String.format("%.8f", Math.floor(quantity * 100000000) / 100000000));
            put("price", String.format("%.2f", price));
        }});
        return result;
    }

    public String orderUpdate(String order_id, double quantity, double price) throws ExmoException {
        String result = request("margin/user/order/update", new HashMap<String, String>() {{
            put("order_id", order_id);
            put("quantity", String.format("%.8f", Math.floor(quantity * 100000000) / 100000000));
            put("price", String.format("%.2f", price));
        }});
        return result;
    }

    public String orderCancel(String order_id) throws ExmoException {
        String result = request("margin/user/order/cancel", new HashMap<String, String>() {{
            put("order_id", order_id);
        }});
        return result;
    }

    public final String userInfo() throws ExmoException {
        String result = request("margin/user/info", null);
        return result;
    }

    public static String walletList() throws ExmoException {
        String result = request("margin/user/wallet/list", null);
        return result;
    }

    public final String positionList() throws ExmoException {
        String result = request("margin/user/position/list", null);
        return result;
    }
}