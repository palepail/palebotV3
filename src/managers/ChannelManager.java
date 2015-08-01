package managers;

import com.sun.jersey.spi.inject.Inject;
import dao.ChannelDAO;
import dto.ChannelDTO;
import models.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by palepail on 7/29/2015.
 */
public class ChannelManager {

    ChannelDAO channelDAO = new ChannelDAO();
    PalebotManager palebot = PalebotManager.getInstance();
    ListenerManager listenerManager = ListenerManager.getInstance();


    public List<ChannelDTO> getAllDTO() {

       return createChannelDTOs(channelDAO.getAll());
    }
    public List<Channel> getAll() {
        return channelDAO.getAll();
    }

    private List<ChannelDTO> createChannelDTOs(List<Channel> channels) {

        List<ChannelDTO> dtos = new ArrayList<>();
        for(Channel channel : channels)
        {
            ChannelDTO dto= new ChannelDTO();
            dto.setName(channel.getName());
            dto.setId(channel.getId());
            dto.setStatus(palebot.getStatus(channel.getName()));
            dto.setListeners(listenerManager.getAllSimpleByChannelID(channel.getId()));
            dtos.add(dto);
        }
        return dtos;
    }

    public Channel getChannelById(int id) {
        return channelDAO.getChannelById(id);
    }

    public Channel getChannelByName(String name) {
        return channelDAO.getChannelByName(name);
    }

    public void deleteChannel(int id) {

        Channel channelToRemove = channelDAO.getChannelById(id);
        channelDAO.deleteChannel(channelToRemove.getId());

    }

    public Channel addChannel(Channel channel) {
        return channelDAO.addChannel(channel);
    }
}
