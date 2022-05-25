package com.webapp.market.markets.exmo;

import com.webapp.market.json.OrderBook;
import com.webapp.market.markets.AbstractWebsocket;
import com.webapp.market.utils.Log;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.websocket.*;
import java.io.IOException;

@ClientEndpoint()
public class WsExmo extends AbstractWebsocket {

    private static final Log log = new Log(WsExmo.class.getName());

    private static WsExmo instance;

    private WsExmo() {
        super("Exmo");
    }

    public static WsExmo getInstance() {
        if (instance == null) {
            instance = new WsExmo();
        }
        return instance;
    }

    @Override
    public void initMessages() {
        addMessage("{\"id\":1,\"method\":\"subscribe\",\"topics\":[\"spot/order_book_snapshots:BTC_USDT\"]}");
    }

    @Override
    public void parseMessage(String message) {
        try {
            if (message.contains("update") && message.contains("spot/order_book_snapshots:BTC_USDT")) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(message);
                mapper.readValue(node.get("data").toString(), OrderBook.class);
                OrderBook.setTime(Long.parseLong(node.get("ts").toString()));
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
