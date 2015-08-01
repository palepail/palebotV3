package dao;

import models.Waifu;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

/**
 * Created by palepail on 7/31/2015.
 */
public class WaifuDAO {
    EntityManager em = PersistenceManager.getInstance().getEntityManager();

    public List<Waifu> getAll(){

        Query query = em.createQuery("SELECT e FROM models.Waifu e");
        return query.getResultList();
    }

    public Waifu getWaifuById(int id){
        return em.find(Waifu.class, id);
    }

    public Waifu getWaifuByName(String name){
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE e.name = :name");
        if(query.getResultList().size()>0) {
            return (Waifu) query.getResultList().get(0);
        }else{
            return null;
        }
    }

    public Waifu getRandom(){
        String queryString = "SELECT * FROM palebot.Waifu ORDER BY RAND() LIMIT 1";

        Query query = em.createNativeQuery(queryString, Waifu.class);
        List results = query.getResultList();
        if(results.size()==0)
        {
            return null;
        }else{
           return (Waifu) results.get(0);
        }
    }

    public Waifu addWaifu(Waifu waifu){

            Waifu newWaifu = new Waifu();
            newWaifu.setName(waifu.getName());
            newWaifu.setLink(waifu.getLink());
            em.getTransaction().begin();
            newWaifu = em.merge(newWaifu);
            em.getTransaction().commit();
            return newWaifu;

    }
    public void deleteWaifu(int id){
        em.getTransaction().begin();
        em.remove(getWaifuById(id));
        em.getTransaction().commit();

    }
}
