package dao;

import models.CustomMessage;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by palepail on 8/4/2015.
 */
public class CustomMessageDAO {
    EntityManager em = PersistenceManager.getInstance().getEntityManager();

    public List<CustomMessage> getCustomMessagesByChannel(int channelId){
            Query query = em.createQuery("SELECT e FROM models.CustomMessage e Where e.channelId = :channelId");
            return query.setParameter("channelId", channelId).getResultList();
    }
    public CustomMessage getCustomMessagesByTriggerFromChannel(int channelId, String trigger){
        Query query = em.createQuery("SELECT e FROM models.CustomMessage e Where e.channelId = :channelId and e.customTrigger = :trigger");
        List<CustomMessage> messages = query.setParameter("channelId", channelId).setParameter("trigger", trigger).getResultList();
        if(messages.size()==0)
        {
            return null;
        }
        return messages.get(0);
    }

    public boolean deleteTriggerFromChannel(int channelId, String trigger)
    {
        CustomMessage customMessage = getCustomMessagesByTriggerFromChannel(channelId, trigger);
        if(customMessage==null)
        {
            return false;
        }
        else{
            em.getTransaction().begin();
            em.remove(customMessage);
            em.getTransaction().commit();
            return true;
        }
    }

    public boolean addCustomMessage(CustomMessage message)
    {
        CustomMessage customMessage = getCustomMessagesByTriggerFromChannel(message.getChannelId(), message.getCustomTrigger());

        if(customMessage == null)
        {
            customMessage = new CustomMessage();
        }
        customMessage.setChannelId(message.getChannelId());
        customMessage.setCustomTrigger(message.getCustomTrigger());
        customMessage.setMessage(message.getMessage());
        customMessage.setRestriction(message.getRestriction());
        em.getTransaction().begin();
        em.merge(customMessage);
        em.getTransaction().commit();
        return true;
    }
}
