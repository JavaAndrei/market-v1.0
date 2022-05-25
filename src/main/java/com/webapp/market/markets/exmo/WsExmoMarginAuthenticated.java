package com.webapp.market.markets.exmo;

import com.webapp.market.exceptions.ExmoException;
import com.webapp.market.markets.AbstractWebsocket;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.websocket.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ClientEndpoint()
public class WsExmoMarginAuthenticated extends AbstractWebsocket {

    private final Map<String, Object> walletUSDT;
    private final Map<String, Object> position;
    private final Map<String, Object> limitOrderSell;
    private final Map<String, Object> limitOrderBuy;

    public WsExmoMarginAuthenticated() {
        super("Exmo margin authenticated");
        walletUSDT = new ConcurrentHashMap<>();
        position = new ConcurrentHashMap<>();
        limitOrderSell = new ConcurrentHashMap<>();
        limitOrderBuy = new ConcurrentHashMap<>();
    }

    @Override
    public void initMessages() {

        long nonce = System.currentTimeMillis() + 1119621336617600L;
        String sign = Security.signWS(Security.getPublicKey() + nonce); //String HMAC_SHA512 = "HmacSHA512";
        String key = Security.getPublicKey();

        addMessage(String.format("{\"id\":1,\"method\":\"login\",\"api_key\":\"%s\",\"sign\":\"%s\",\"nonce\":%d}", key, sign, nonce));
        addMessage("{\"id\":1,\"method\":\"subscribe\",\"topics\":[\"margin/wallets\",\"margin/positions\",\"margin/orders\"]}");
    }

    @Override
    public void parseMessage(String message) throws ExmoException {
        //System.out.println("parseMessage" + message);
        try {
            String json = message.toString();
            if (json.contains("margin/wallets")) {
                if (json.contains("snapshot") | json.contains("\"event\":\"update\"")) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(json).get("data").get("USDT");
                    walletUSDT.putAll(mapper.readValue(node.toString(), new TypeReference<Map<String,Object>>(){}));
                }
            } else if (json.contains("margin/positions")) {
                if (json.contains("snapshot")) {
                    ObjectMapper mapper = new ObjectMapper();
                    Iterator<JsonNode> node = mapper.readTree(json).get("data").getElements();
                    while (node.hasNext()) {
                        position.putAll(mapper.readValue(node.toString(), new TypeReference<Map<String,Object>>(){}));
                        //System.out.println("position: " + node);
                    }
                }
                if (json.contains("\"event\":\"update\"")) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(json).get("data");
                    position.putAll(mapper.readValue(node.toString(), new TypeReference<Map<String,Object>>(){}));
                    //System.out.println("position: " + node);
                }
            }  else if (json.contains("margin/orders")) {
                if (json.contains("snapshot")) {
                    ObjectMapper mapper = new ObjectMapper();
                    Iterator<JsonNode> node = mapper.readTree(json).get("data").getElements();
                    while (node.hasNext()) {
                        String nodeTmp = node.next().toString();
                        if (nodeTmp.contains("\"type\":\"limit_buy\"")) {
                            limitOrderBuy.putAll(mapper.readValue(nodeTmp, new TypeReference<Map<String,Object>>(){}));
                        }
                        if (nodeTmp.contains("\"type\":\"limit_sell\"")) {
                            limitOrderSell.putAll(mapper.readValue(nodeTmp, new TypeReference<Map<String,Object>>(){}));
                        }
                    }
                }
                if (json.contains("\"event\":\"update\"")) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(json).get("data");
                    String nodeTmp = node.toString();
                    if (nodeTmp.contains("\"type\":\"limit_buy\"")) {
                        limitOrderBuy.putAll(mapper.readValue(nodeTmp, new TypeReference<Map<String,Object>>(){}));
                    }
                    if (nodeTmp.contains("\"type\":\"limit_sell\"")) {
                        limitOrderSell.putAll(mapper.readValue(nodeTmp, new TypeReference<Map<String,Object>>(){}));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
