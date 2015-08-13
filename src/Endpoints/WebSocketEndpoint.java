package endpoints;


import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import org.jboss.logging.Logger;

/**
 * Created by palepail on 8/13/2015.
 */
@ServerEndpoint("/websocket")
public class WebSocketEndpoint {
    Logger log = Logger.getLogger(this.getClass());
    @OnMessage
    public void receiveMessage(String message, Session session) {
        log.info("Received : "+ message + ", session:" + session.getId());
    }

    @OnOpen
    public void open(Session session) {
        log.info("Open session:" + session.getId());
    }

    @OnClose
    public void close(Session session, CloseReason c) {
        log.info("Closing:" + session.getId());
    }

}
