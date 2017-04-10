package dao;

import models.Spam;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by palepail on 9/29/2015.
 */
public class SpamDAO {


    public List<Spam> getAll() {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.Spam e");
        List<Spam> list = query.getResultList();
        em.close();
        return list;
    }

    public boolean addSpam(Spam spam){
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        em.merge(spam);
        em.getTransaction().commit();
        em.close();
        return true;
    }

}
