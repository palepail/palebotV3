package dao;

import models.Spam;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by palepail on 9/29/2015.
 */
public class SpamDAO {
    EntityManager em = PersistenceManager.getInstance().getEntityManager();

    public List<Spam> getAll() {
        Query query = em.createQuery("SELECT e FROM models.Spam e");
        return query.getResultList();
    }

    public boolean addSpam(Spam spam){
        em.getTransaction().begin();
        em.merge(spam);
        em.getTransaction().commit();
        return true;
    }

}
