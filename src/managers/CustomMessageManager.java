package managers;

import dao.CustomMessageDAO;
import models.CustomMessage;

import java.util.List;

/**
 * Created by palepail on 8/3/2015.
 */
public class CustomMessageManager {
CustomMessageDAO customMessageDAO = new CustomMessageDAO();

    public List<CustomMessage> getCustomMessagesByChannel(int channelId){
        return customMessageDAO.getCustomMessagesByChannel(channelId);
    }
    public boolean deleteTriggerFromChannel(int channelId, String trigger)
    {
        return customMessageDAO.deleteTriggerFromChannel(channelId, trigger);
    }

    public boolean addCustomMessage(CustomMessage message)
    {
        return customMessageDAO.addCustomMessage(message);
    }


}
