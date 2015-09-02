package managers;

import bot.AnimeListener;
import bot.DefaultListener;
import bot.QuoteListener;
import bot.YoutubeListener;
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

    static ListenerDAO listenerDAO = new ListenerDAO();
    static ChannelManager channelManager = new ChannelManager();
    private static ListenerManager listenerManager = new ListenerManager();

    public static ListenerManager getInstance() {
        return listenerManager;
    }

    public List<Listener> getAllSimpleByChannelID(int channelId) {
        return listenerDAO.getAllByChannel(channelId);
    }

    public List<Listener> getAll() {
        return listenerDAO.getAll();
    }

    public List<ListenerAdapter> getAllByChannelID(int channelId) {

        List<Listener> listenerEntities = listenerDAO.getAllByChannel(channelId);
        List<ListenerAdapter> listenerAdapters = new ArrayList<>();

        for (Listener listener : listenerEntities) {
            switch (listener.getName()) {
                case DefaultListener.NAME: {
                    listenerAdapters.add(new DefaultListener());
                    break;
                }
                case AnimeListener.NAME: {
                    listenerAdapters.add(new AnimeListener());
                    break;
                }
                case YoutubeListener.NAME: {
                    listenerAdapters.add(new YoutubeListener());
                    break;
                }
                case QuoteListener.NAME: {
                    listenerAdapters.add(new QuoteListener());
                    break;
                }
                default: {
                    break;
                }
            }
        }
        return listenerAdapters;
    }

    public ListenerAdapter getByName(String listenerName) {

        ListenerAdapter listenerAdapter;
        switch (listenerName) {
            case DefaultListener.NAME: {
                listenerAdapter = new DefaultListener();
                break;
            }
            case AnimeListener.NAME: {
                listenerAdapter = new AnimeListener();
                break;
            }
            case YoutubeListener.NAME: {
                listenerAdapter = new YoutubeListener();
                break;
            }
            case QuoteListener.NAME: {
                listenerAdapter = new QuoteListener();
                break;
            }
            default: {
                listenerAdapter = null;
                break;
            }
        }
        return listenerAdapter;
    }

    public boolean addListenerToChannel(int channelId, String listenerName) {
        listenerDAO.addListenerToChannel(channelId, listenerName);
        return true;
    }

    public boolean removeListenerFromChannel(int channelId, String listenerName) {
        listenerDAO.removeFromChannel(channelId, listenerName);

        return true;
    }

    public List<ListenerAdapter> getAllByChannelName(String name) {
        Channel channel = channelManager.getChannelByName(name);
        return getAllByChannelID(channel.getId());

    }
}
