package dao;

import models.CustomMessage;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by palepail on 8/4/2015.
 */
public class CustomMessageDAO {


    public List<CustomMessage> getCustomMessagesByChannel(int channelId){
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
            Query query = em.createQuery("SELECT e FROM models.CustomMessage e Where e.channelId = :channelId");
        List<CustomMessage> list = query.setParameter("channelId", channelId).getResultList();
        em.close();
            return list;
    }
    public CustomMessage getCustomMessagesByTriggerFromChannel(int channelId, String trigger){
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.CustomMessage e Where e.channelId = :channelId and e.customTrigger = :trigger");
        List<CustomMessage> messages = query.setParameter("channelId", channelId).setParameter("trigger", trigger).getResultList();
        em.close();
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
            EntityManager em = PersistenceManager.getInstance().getEntityManager();
            em.getTransaction().begin();
            em.remove(customMessage);
            em.getTransaction().commit();
            em.close();
            return true;
        }
    }

    public boolean addCustomMessage(CustomMessage message)
    {

        CustomMessage customMessage = getCustomMessagesByTriggerFromChannel(message.getChannelId(), message.getCustomTrigger());
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
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
        em.close();
        return true;
    }
}
