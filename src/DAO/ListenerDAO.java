package dao;

import models.Listener;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by palepail on 7/31/2015.
 */
public class ListenerDAO {


    public Listener getByNameFromChannel(int channelId, String listenerName) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.Listener e Where e.channelId = :channelId and e.name = :listenerName");
        List<Listener> listeners = query.setParameter("channelId", channelId).setParameter("listenerName", listenerName).getResultList();
        em.close();
        if (listeners.size() == 0) {
            return null;
        } else {
            return listeners.get(0);
        }

    }

    public List<Listener> getAllByChannel(int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.Listener e Where e.channelId = :channelId");

        List<Listener> list = query.setParameter("channelId", channelId).getResultList();
        em.close();
        return list;
    }

    public List<Listener> getAll() {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery(" FROM models.Listener Group By (name)");
        em.close();
        return query.getResultList();
    }

    public boolean addListenerToChannel(int channelId, String listenerName) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Listener listener = new Listener();
        listener.setChannelId(channelId);
        listener.setName(listenerName);

        em.getTransaction().begin();
        em.merge(listener);
        em.getTransaction().commit();
        em.close();
        return true;
    }

    public boolean removeFromChannel(int channelId, String listenerName) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();

        em.remove(getByNameFromChannel(channelId, listenerName));
        em.getTransaction().commit();
        em.close();
        return true;
    }


    public boolean getActive(int channelId, String name) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();

        Query query = em.createQuery("SELECT e FROM models.Listener e WHERE e.name = :name AND e.channelId = :id");
        List<Listener> list = query.setParameter("name", name.trim()).setParameter("id", channelId).getResultList();
        em.close();
        if (list.size() == 1) {
            return list.get(0).getActive();
        } else {
            return false;
        }
    }

    public void setActive(int channelId, String name, boolean active) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();

        Query query = em.createQuery("SELECT e FROM models.Listener e WHERE e.name = :name AND e.channelId = :id");
        List<Listener> list = query.setParameter("name", name.trim()).setParameter("id", channelId).getResultList();
        if (list.size() == 1) {
            list.get(0).setActive(active);
        }
        em.getTransaction().commit();
        em.close();
    }
}
