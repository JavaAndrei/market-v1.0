package com.webapp.market.strategies;

import com.webapp.market.markets.Market;

public interface Strategy {

    void setLogin(String login);

    void execute();

    void interrupt();

    void setMarket(Market market);

}
