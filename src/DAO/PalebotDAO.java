package dao;


import models.PalebotAdmin;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by palepail on 8/6/2015.
 */
public class PalebotDAO {



    public boolean isPalebotAdmin(String userName){
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.PalebotAdmin e Where e.name = :userName");
          List<PalebotAdmin> admins = query.setParameter("userName", userName).getResultList();
        em.close();
        if (admins.size()==0)
        {
            return false;
        }
        return true;
    }


}
