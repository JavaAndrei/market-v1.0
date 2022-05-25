package com.webapp.market.markets.binance;

import com.webapp.market.json.TargetPrice;
import com.webapp.market.markets.AbstractWebsocket;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;


import javax.websocket.*;
import java.io.IOException;

@ClientEndpoint()
public class WsBinance extends AbstractWebsocket {

    private static WsBinance instance;

    private WsBinance() {
        super("Binance");
    }

    public static WsBinance getInstance() {
        if (instance == null) {
            instance = new WsBinance();
        }
        return instance;
    }

    @Override
    public void initMessages() {

    }

    @Override
    public void parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(message.toString());
            TargetPrice.setValue(Double.parseDouble(node.get("p").getTextValue()));
            TargetPrice.setTime(Long.parseLong(node.get("E").toString()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
