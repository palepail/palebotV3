package dao;

import models.Channel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by palepail on 7/25/2015.
 */
public class ChannelDAO {

    EntityManager em = PersistenceManager.getInstance().getEntityManager();

    public List<Channel> getAll(){

        Query query = em.createQuery("SELECT e FROM models.Channel e");
        return query.getResultList();
    }

    public Channel getChannelById(int id){
        return em.find(Channel.class, id);
    }

    public void updateChannel(String channelName){
        em.merge(channelName);
    }
    public void deleteChannel(int id){
        em.remove(getChannelById(id));
    }
}
