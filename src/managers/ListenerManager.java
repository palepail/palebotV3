package managers;

import bot.AnimeListener;
import bot.DefaultListener;
import dao.ListenerDAO;
import models.Channel;
import models.Listener;
import org.pircbotx.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by palepail on 7/31/2015.
 */
public class ListenerManager {

    ListenerDAO listenerDAO = new ListenerDAO();
    ChannelManager channelManager = new ChannelManager();
    private static ListenerManager listenerManager= new ListenerManager();

    public static ListenerManager getInstance(){
        return listenerManager;
    }

    public List<Listener> getAllSimpleByChannelID(int channelId){
        return listenerDAO.getAllByChannel(channelId);
    }

    public List<ListenerAdapter> getAllByChannelID(int channelId){

        List<Listener> listenerEntities = listenerDAO.getAllByChannel(channelId);
        List<ListenerAdapter> listnerAdapters = new ArrayList<>();

        for(Listener listner : listenerEntities)
        {
            switch (listner.getName()){
                case DefaultListener.NAME:{
                    listnerAdapters.add(new DefaultListener());
                    break;
                }
                case AnimeListener.NAME:{
                    listnerAdapters.add(new AnimeListener());
                    break;
                }
                default:{
                    break;
                }
            }
        }
       return listnerAdapters;
    }

    public List<ListenerAdapter> getAllByChannelName(String name){
        Channel channel = channelManager.getChannelByName(name);
        return getAllByChannelID(channel.getId());

    }
}
