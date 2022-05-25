package com.webapp.market.markets;

import com.webapp.market.dao.DAOFactory;
import com.webapp.market.exceptions.ExmoException;
import com.webapp.market.utils.Log;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@ClientEndpoint()
public abstract class AbstractWebsocket {

    private final Log log = new Log(AbstractWebsocket.class.getName());

    @SuppressWarnings("unchecked")
    private AtomicReference<DAOFactory> dao = new AtomicReference<>(DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL));
    private String login = "System";

    private final WebSocketContainer container;
    private Session userSession = null;
    private final String serverName;
    private final List<String> messages = new ArrayList<>();
    private boolean alive;

    public boolean isAlive() {
        return alive;
    }

    public AbstractWebsocket(String serverName) {
        this.container = ContainerProvider.getWebSocketContainer();
        this.serverName = serverName;
        initMessages();
    }

    public abstract void initMessages();

    public void connect(String endpoint) throws ExmoException {
        try {
            container.connectToServer(this, new URI(endpoint));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            log.logSEVERE(dao, login, String.format("Can't connect to server \"%s\". %s", serverName, e.getMessage()));
            throw new ExmoException(String.format("Can't connect to server \"%s\". %s", serverName, e.getMessage()));
        }
    }

    public void disconnect() {
        if (userSession != null) {
            try {
                userSession.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public boolean addMessage(String message) {
        return messages.add(message);
    }

    @OnOpen
    public void onOpen(Session userSession) {
        log.logINFO(dao, login, String.format("opening websocket \"%s\"", serverName));
        this.userSession = userSession;
        for (String message : messages)
            sendMessage(message);
        alive = true;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        log.logINFO(dao, login, String.format("closing websocket \"%s\". Reason %s", serverName, reason.getReasonPhrase()));
        this.userSession = null;
        alive = false;
    }

    @OnMessage
    public void onMessage(String message) throws ExmoException {
        if (message.contains("error")) {
            log.logSEVERE(String.format("Error communicating with server \"%s\". %s", serverName, message));
        }
        parseMessage(message);
    }

    public abstract void parseMessage(String message) throws ExmoException;

    @OnError
    public void onError(Session userSession, Throwable error){
        log.logSEVERE(dao, login, String.format("Error communicating with server \"%s\". %s", serverName, error.getMessage()));
        alive = false;
    }
}
