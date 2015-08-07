package dao;


import models.PalebotAdmin;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by palepail on 8/6/2015.
 */
public class PalebotDAO {

    EntityManager em = PersistenceManager.getInstance().getEntityManager();

    public boolean isPalebotAdmin(String userName){
        Query query = em.createQuery("SELECT e FROM models.PalebotAdmin e Where e.name = :userName");
      List<PalebotAdmin> admins = query.setParameter("userName", userName).getResultList();
        if (admins.size()==0)
        {
            return false;
        }
        return true;
    }


}
