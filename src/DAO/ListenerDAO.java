package dao;

import models.Listener;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by palepail on 7/31/2015.
 */
public class ListenerDAO {
    EntityManager em = PersistenceManager.getInstance().getEntityManager();

    public List<Listener> getAllByChannel(int channelId){

        Query query = em.createQuery("SELECT e FROM models.Listener e Where e.channelId = :channelId");

        return  query.setParameter("channelId", channelId).getResultList();
    }

    public List<Listener> getAll(){

        Query query = em.createQuery(" FROM models.Listener Group By (name)");
        return  query.getResultList();
    }


}
