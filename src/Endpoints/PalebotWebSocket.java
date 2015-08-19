package endpoints;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


import bot.YoutubeVideo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dto.ChannelDTO;
import jdk.nashorn.internal.parser.JSONParser;
import models.Channel;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by palepail on 8/13/2015.
 */
@ServerEndpoint("/channels/{id}")
public class PalebotWebSocket {
    Logger log = Logger.getLogger(this.getClass());
    Gson gson = new Gson();

    private static HashMap<Session,Integer> sessionMap = new HashMap<>();

    @OnMessage
    public void receiveMessage(String message, Session session) {
        log.info("Received : "+ message + ", session:" + session.getId());
    }

    @OnOpen
    public void open(  @PathParam("id")int id, Session session) {
        sessionMap.put(session,id);
        log.info("Open session:" + session.getId());

    }

    public void sendChannel(ChannelDTO channel){
        List<Session> sessions = sessionMap.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), channel.getId()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        for (Session session : sessions){
            session.getAsyncRemote().sendText(gson.toJson(channel));
        }

    }

    public void sendYoutubeRequest(int channelId, YoutubeVideo video){
        List<Session> sessions = sessionMap.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), channelId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        for (Session session : sessions){
            session.getAsyncRemote().sendText(new Gson().toJson(video));
        }

    }

    @OnClose
    public void close(Session session, CloseReason c) {
        sessionMap.remove(session.getId());
        log.info("Closing:" + session.getId());
    }

}
