package managers;

import dao.ChannelDAO;
import models.Channel;

import java.util.List;

/**
 * Created by palepail on 7/29/2015.
 */
public class ChannelManager {
    ChannelDAO channelDAO = new ChannelDAO();

    public List<Channel> getAll() {
       return channelDAO.getAll();
    }

    public Channel getChannelById(int id) {
        return channelDAO.getChannelById(id);
    }

    public void deleteChannel(int id) {
        channelDAO.deleteChannel(id);
    }

    public Channel insertChannel(Channel channel) {
        return channelDAO.insertChannel(channel);
    }
}
