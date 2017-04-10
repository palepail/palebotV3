package dao;

import models.Channel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by palepail on 7/25/2015.
 */
public class ChannelDAO {



    public List<Channel> getAll(){
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.Channel e");
        List<Channel> list =  query.getResultList();
        em.close();
        return list;
    }

    public Channel getChannelById(int id){
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Channel channel = em.find(Channel.class, id);
        em.close();
        return channel;
    }
    public Channel getChannelByName(String name){
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.Channel e WHERE e.name = :name");
        List<Channel> list = query.setParameter("name", name ).getResultList();
        if(list.size()==0)
        {
            em.close();
            return null;
        }
        else{
            Channel channel = (Channel) query.setParameter("name", name ).getSingleResult();
            em.close();
            return channel;
        }


    }

    public Channel addChannel(Channel channel){


        if( getChannelByName(channel.getName())==null) {
            EntityManager em = PersistenceManager.getInstance().getEntityManager();
            Channel newChannel = new Channel();
            newChannel.setName(channel.getName().trim());
            em.getTransaction().begin();
            newChannel = em.merge(newChannel);
            em.getTransaction().commit();
            return newChannel;
        }
        return null;

    }
    public void deleteChannel(int id){
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        em.remove(getChannelById(id));
        em.getTransaction().commit();
        em.close();
    }
}
