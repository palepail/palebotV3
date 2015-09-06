package endpoints;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


import bot.MessageManager;
import bot.YoutubeVideo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dto.ChannelDTO;
import jdk.nashorn.internal.parser.JSONParser;
import managers.ChannelManager;
import managers.YoutubeManager;
import models.Channel;
import models.WebsocketMessage;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by palepail on 8/13/2015.
 */
@ServerEndpoint("/websocket/id/{id}")
public class PalebotWebSocket {
    Logger log = Logger.getLogger(this.getClass());
    Gson gson = new Gson();
    ChannelManager channelManager = new ChannelManager();
    YoutubeManager youtubeManager = YoutubeManager.getInstance();

    private static HashMap<Session,Integer> sessionMap = new HashMap<>();

    @OnMessage
    public void receiveMessage(String message, Session session) {
        int channelId = sessionMap.get(session);

        WebsocketMessage websocketMessage =  gson.fromJson(message, WebsocketMessage.class);

        switch(websocketMessage.getMessageType()){
            case "CurrentSong":{
                YoutubeVideo video =  websocketMessage.getPlaylist().get(0);
                MessageManager messageManager = MessageManager.getInstance(channelId);
                messageManager.sendMessage(channelId, video.items.get(0).snippet.title);
                break;
            }
            case "PlaylistUpdate":{
                Channel channel = channelManager.getChannelById(channelId);
                youtubeManager.setPlayList(channel.getName(), websocketMessage.getPlaylist());
                break;
            }
        }
    }

    @OnOpen
    public void open(  @PathParam("id")int id, Session session) {
        sessionMap.put(session,id);
        log.info("Open session:" + session.getId());

    }
    public void sendCurrentSongQuery(int channelId)
    {
        List<Session> sessions = getSessionsById(channelId);

        WebsocketMessage message = new WebsocketMessage("CurrentSong","CurrentSong");
        for (Session session : sessions){
            session.getAsyncRemote().sendText(new Gson().toJson(message));
        }

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

       List<Session> sessions = getSessionsById(channelId);
        WebsocketMessage message = new WebsocketMessage("YoutubeRequest",video);
        for (Session session : sessions){
            session.getAsyncRemote().sendText(new Gson().toJson(message));
        }

    }

    private List<Session> getSessionsById(int channelId){
        List<Session> sessions = sessionMap.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), channelId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return sessions;
    }

    @OnClose
    public void close(Session session, CloseReason c) {
        sessionMap.remove(session.getId());
        log.info("Closing:" + session.getId());
    }

}
