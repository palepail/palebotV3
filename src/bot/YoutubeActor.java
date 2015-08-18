package bot;

/**
 * Created by palepail on 8/16/2015.
 */

import endpoints.PalebotWebSocket;
import managers.ChannelManager;
import managers.CustomMessageManager;
import models.Channel;
import models.CustomMessage;
import org.pircbotx.hooks.events.MessageEvent;

public class YoutubeActor {

    MessageManager messageManager;
    PalebotWebSocket webSocket = new PalebotWebSocket();

    YoutubeManager youtubeManager = YoutubeManager.getInstance();
    private String YOUTUBE_REQUEST_ID;

    ChannelManager channelManager = new ChannelManager();
    Channel channelEntity;
    String userName;
    String channelName;
    String message;

    public void setValues(MessageEvent event) {
        channelName = event.getChannel().getName();
        userName = event.getUser().getNick();
        messageManager = MessageManager.getInstance(channelName);
        channelEntity = channelManager.getChannelByName(channelName.substring(1));
        message = event.getMessage();
        YOUTUBE_REQUEST_ID = "YOUTUBE_REQUEST_" + channelEntity.getId();
    }


    public void sendYoutubeRequest(MessageEvent event) {


        String request = message.replace("!request", "").trim();

        if (request.contains("?v=")) {
           request = request.substring(request.indexOf('=')+1, request.length());
        }

        YoutubeVideo video = youtubeManager.getVideoDetails(request);
        video.setUploader(userName);

      //  String json = "{ \"id\":\"" + request.trim() + "\", \"uploader\": \"" + userName.trim() + "\" }";
        webSocket.sendYoutubeRequest(channelEntity.getId(), video);


    }

    public boolean tooManyRequests(MessageEvent event) {

        if (messageManager.overLimit() || !messageManager.lock(YOUTUBE_REQUEST_ID, 10000)) {
            messageManager.sendMessage(event, userName + ", please wait to request a song.");
            return true;
        } else {
            return false;
        }
    }
}



