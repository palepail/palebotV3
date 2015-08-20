package bot;

/**
 * Created by palepail on 8/16/2015.
 */

import endpoints.PalebotWebSocket;
import managers.ChannelManager;
import managers.YoutubeManager;
import models.Channel;
import org.pircbotx.hooks.events.MessageEvent;

import java.sql.Date;
import java.time.Duration;
import java.time.ZonedDateTime;

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

    public void sendCurrentSongQuery(MessageEvent event){
        webSocket.sendCurrentSongQuery(channelEntity.getId());
    }


    public void sendYoutubeRequest(MessageEvent event) {


        String request = message.replace("!request", "").trim();

        if (request.contains("?v=")) {
           request = request.substring(request.indexOf('=')+1, request.length());
        }

        YoutubeVideo video = youtubeManager.getVideoDetails(request);

        if(video!=null) {
            video.setUploader(userName);
            video.setChannelId(channelEntity.getId());
            webSocket.sendYoutubeRequest(channelEntity.getId(), video);



            Duration duration = Duration.parse(video.items.get(0).contentDetails.duration);
            String durationString = String.format("%d:%02d:%02d", duration.getSeconds() / 3600, (duration.getSeconds() % 3600) / 60, (duration.getSeconds() % 60));
            messageManager.sendMessage(event, video.items.get(0).snippet.title + " " + durationString + " requested by " + userName);


        }else{
            messageManager.sendMessage(event, "Video Not Found");
        }

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



