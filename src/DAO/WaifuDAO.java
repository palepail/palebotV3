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

    public List<Waifu> getWaifuByName(String name){
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE UPPER(e.name) like UPPER(:name)");
        return query.setParameter("name", "%"+name.trim()+"%").getResultList();
    }


    public List<Waifu> getWaifuByNameFromChannel(String name, int channelId){
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE UPPER(e.name) like UPPER(:name) AND e.channelId = :channelId ");
        return query.setParameter("channelId", channelId).setParameter("name", "%" + name.trim() + "%").getResultList();
    }

    public List<Waifu> getWaifuByLink(String link)
    {
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE e.link = :link");
        return query.setParameter("link", link.trim()).getResultList();
    }

    public List<Waifu> getWaifuByLinkFromChannel(String link, int channelId)
    {
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE e.link = :link AND e.channelId = :channelId");
        return query.setParameter("link", link.trim()).setParameter("channelId", channelId).getResultList();
    }

    public List<Waifu> getBest(int channelId){

        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE  e.channelId = :channelId and e.points =(SELECT max(e.points) FROM e WHERE  e.channelId = :channelId)");
        List<Waifu> waifu = query.setParameter("channelId", channelId).getResultList();
        return waifu;
    }

    public List<Waifu> getWorst(int channelId){

        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE  e.channelId = :channelId and e.points = (SELECT min(e.points) FROM e WHERE  e.channelId = :channelId)");
        List<Waifu> waifu = query.setParameter("channelId", channelId).getResultList();
        return waifu;
    }

    public void resetFight(int channelId){

        String queryString = "Update palebot.Waifu Set palebot.waifu.POINTS = 0 WHERE CHANNEL_ID = :channelId";

        Query query = em.createNativeQuery(queryString, Waifu.class);
    }

    public Waifu getRandom(){
        //native query requires db names
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

    public Waifu getRandomFromChannel(int channelId){
        //native query requires db names
        String queryString = "SELECT * FROM palebot.Waifu WHERE CHANNEL_ID = :channelId ORDER BY RAND() LIMIT 1";

        Query query = em.createNativeQuery(queryString, Waifu.class);
        List results = query.setParameter("channelId", channelId).getResultList();
        if(results.size()==0)
        {
            return null;
        }else{
            return (Waifu) results.get(0);
        }
    }

    public Waifu addWaifu(Waifu waifu){

            Waifu newWaifu = new Waifu();
            newWaifu.setName(waifu.getName().trim());
            newWaifu.setLink(waifu.getLink().trim());
            newWaifu.setChannelId(waifu.getChannelId());
            newWaifu.setUploader(waifu.getUploader());
            newWaifu.setPoints(0);
            em.getTransaction().begin();
            newWaifu = em.merge(newWaifu);
            em.getTransaction().commit();
            return newWaifu;

    }

    public Waifu updateWaifu(Waifu waifu){
        Waifu updatedWaifu = getWaifuById(waifu.getId());
        if(updatedWaifu!=null)
        {
            em.getTransaction().begin();
            updatedWaifu.setName(waifu.getName());
            updatedWaifu.setChannelId(waifu.getChannelId());
            updatedWaifu.setLink(waifu.getLink());
            updatedWaifu.setPoints(waifu.getPoints());
            em.getTransaction().commit();
            return updatedWaifu;
        }
        return null;

    }
    public void deleteWaifu(int id){
        em.getTransaction().begin();
        em.remove(getWaifuById(id));
        em.getTransaction().commit();

    }
    public boolean deleteWaifuByLink(String link){

        List<Waifu> waifu = getWaifuByLink(link);
        if(waifu.size()>0)
        {
            em.getTransaction().begin();
            for(Waifu currentWaifu : waifu) {
                em.remove(currentWaifu);
            }
            em.getTransaction().commit();

            return true;
        }
        return false;
    }

    public boolean deleteWaifuByLinkFromChannel(String link, int channelId){

        List<Waifu> waifu = getWaifuByLinkFromChannel(link, channelId);
        if(waifu.size()>0)
        {
            em.getTransaction().begin();
            for(Waifu currentWaifu : waifu) {
                em.remove(currentWaifu);
            }
            em.getTransaction().commit();

            return true;
        }
        return false;
    }

}
