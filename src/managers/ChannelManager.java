package managers;

import dao.ChannelDAO;
import dto.ChannelDTO;
import models.Channel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by palepail on 7/29/2015.
 */
public class ChannelManager {

    static ChannelDAO channelDAO = new ChannelDAO();
    PalebotManager palebot = PalebotManager.getInstance();
    ListenerManager listenerManager = ListenerManager.getInstance();


    public List<ChannelDTO> getAllDTO() {

       return createChannelDTOs(channelDAO.getAll());
    }
    public List<Channel> getAll() {
        return channelDAO.getAll();
    }

    public ChannelDTO getChannelDTOById(int channelId){
        Channel channel = getChannelById(channelId);
        ChannelDTO channelDTO = createChannelDTO(channel);
        return channelDTO;
    }

    private List<ChannelDTO> createChannelDTOs(List<Channel> channels) {

        List<ChannelDTO> dtos = new ArrayList<>();
        for(Channel channel : channels)
        {
            dtos.add(createChannelDTO(channel));
        }
        return dtos;
    }

    private ChannelDTO createChannelDTO(Channel channel){
        ChannelDTO dto= new ChannelDTO();
        dto.setName(channel.getName());
        dto.setId(channel.getId());
        dto.setStatus(palebot.getStatus(channel.getName()));
        dto.setListeners(listenerManager.getAllSimpleByChannelID(channel.getId()));
      return dto;
    }

    public Channel getChannelById(int id) {
        return channelDAO.getChannelById(id);
    }

    public Channel getChannelByName(String name) {
        return channelDAO.getChannelByName(name);
    }

    public ChannelDTO getChannelDTOByName(String name) {
        return createChannelDTO(channelDAO.getChannelByName(name));
    }

    public void deleteChannel(int id) {

        Channel channelToRemove = channelDAO.getChannelById(id);
        channelDAO.deleteChannel(channelToRemove.getId());

    }

    public Channel addChannel(Channel channel) {
        return channelDAO.addChannel(channel);
    }
}
