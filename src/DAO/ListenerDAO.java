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

    public Listener getByNameFromChannel(int channelId, String listenerName) {

        Query query = em.createQuery("SELECT e FROM models.Listener e Where e.channelId = :channelId and e.name = :listenerName");
        List<Listener> listeners = query.setParameter("channelId", channelId).setParameter("listenerName", listenerName).getResultList();
        if (listeners.size() == 0) {
            return null;
        } else {
            return listeners.get(0);
        }
    }

    public List<Listener> getAllByChannel(int channelId) {

        Query query = em.createQuery("SELECT e FROM models.Listener e Where e.channelId = :channelId");

        return query.setParameter("channelId", channelId).getResultList();
    }

    public List<Listener> getAll() {

        Query query = em.createQuery(" FROM models.Listener Group By (name)");
        return query.getResultList();
    }

    public boolean addListenerToChannel(int channelId, String listenerName) {
        Listener listener = new Listener();
        listener.setChannelId(channelId);
        listener.setName(listenerName);
        em.getTransaction().begin();
        em.merge(listener);
        em.getTransaction().commit();
        return true;
    }

    public boolean removeFromChannel(int channelId, String listenerName) {
        em.getTransaction().begin();
        em.remove(getByNameFromChannel(channelId, listenerName));
        em.getTransaction().commit();
        return true;
    }


}
