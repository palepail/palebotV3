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

    public Channel insertChannel(Channel channel){

        Query query = em.createQuery("SELECT e FROM models.Channel e WHERE e.name = :name");

        if(query.setParameter("name", channel.getName() ).getResultList().size()==0) {

            Channel newChannel = new Channel();
            newChannel.setName(channel.getName());
            em.getTransaction().begin();
            newChannel = em.merge(newChannel);
            em.getTransaction().commit();
            return newChannel;
        }
        return null;

    }
    public void deleteChannel(int id){
        em.remove(getChannelById(id));
    }
}
