package dao;

import dto.WaifuDTO;
import models.Waifu;
import models.WaifuRank;
import models.WaifuThirst;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.*;

/**
 * Created by palepail on 7/31/2015.
 */
public class WaifuDAO {

    Random random = new Random(System.currentTimeMillis());



    public void updateWaifuThirst(WaifuThirst thirst) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT e FROM models.WaifuThirst e WHERE  e.channelId = :channelId and e.user = :user");
        List<WaifuThirst> waifuThirst = query.setParameter("channelId", thirst.getChannelId()).setParameter("user", thirst.getUser()).getResultList();
        if (waifuThirst.size() == 0) {
            WaifuThirst newThirst = new WaifuThirst();
            newThirst.setChannelId(thirst.getChannelId());
            newThirst.setCount(1);
            newThirst.setUser(thirst.getUser());
            em.persist(newThirst);
        } else {
            WaifuThirst existingThirst = waifuThirst.get(0);
            existingThirst.setCount(existingThirst.getCount() + 1);
        }

        em.getTransaction().commit();
        em.close();
    }

    public WaifuThirst getThirst(String userName, int channelId)
    {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.WaifuThirst e WHERE  e.channelId = :channelId and e.user = :user");

        List<WaifuThirst> waifuThirst = query.setParameter("channelId", channelId).setParameter("user", userName).getResultList();
        em.close();
        if (waifuThirst.size() == 0) {
            return null;
        } else {
            return waifuThirst.get(0);
        }

    }

    public List<WaifuThirst> getThirstiest(int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.WaifuThirst e WHERE  e.channelId = :channelId and e.count = (SELECT max(e.count) FROM e WHERE  e.channelId = :channelId)");

        List<WaifuThirst> waifuThirst = query.setParameter("channelId", channelId).getResultList();
        em.close();
        if (waifuThirst.size() == 0) {
            return null;
        } else {
            return waifuThirst;
        }
    }

    public List<Waifu> getBestUploader(int channelId) {
        //native query requires db names
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        String queryString = "SELECT * FROM waifu where CHANNEL_ID = :channelId and uploader = (select uploader from (select uploader,count(uploader) as cnt from waifu where CHANNEL_ID = :channelId and uploader = 0) t order by cnt DESC)";
        em.close();
        Query query = em.createNativeQuery(queryString, Waifu.class);
        List results = query.setParameter("channelId", channelId).getResultList();
        return results;
    }

    public List<Waifu> getAll() {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.Waifu e");
        List<Waifu> list = query.getResultList();
        em.close();

        return list;
    }

    public Waifu getWaifuById(int id, EntityManager em) {

        Waifu waifu = em.find(Waifu.class, id);

        return waifu;
    }

    public Waifu getWaifuById(int id) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Waifu waifu = em.find(Waifu.class, id);

        em.close();
        return waifu;
    }


    public List<Waifu> getWaifuByChannel(int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE e.channelId = :channelId");
        List<Waifu> list = query.setParameter("channelId", channelId).getResultList();
        em.close();
        return list;
    }

    public List<Waifu> getWaifuByName(String name) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE UPPER(e.name) like UPPER(:name)");
        List<Waifu> list = query.setParameter("name", "%" + name.trim() + "%").getResultList();
        em.close();
        return list;
    }

    public List<Waifu> getWaifuByClaimed(int channelId, String claimed) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE UPPER(e.claimed) like UPPER(:claimed) AND e.channelId = :channelId");
        List<Waifu> list = query.setParameter("claimed", "%" + claimed.trim() + "%").setParameter("channelId", channelId).getResultList();
        em.close();
        return list;
    }


    public List<Waifu> getWaifuByNameFromChannel(String name, int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE UPPER(e.name) like UPPER(:name) AND e.channelId = :channelId ");
        List<Waifu> list = query.setParameter("channelId", channelId).setParameter("name", "%" + name.trim() + "%").getResultList();
        em.close();
        return list;
    }

    public List<Waifu> getWaifuByLink(String link, int id) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE e.link = :link AND e.channelId = :id");
        List<Waifu> list = query.setParameter("link", link.trim()).setParameter("id", id).getResultList();
        em.close();
        if (list.size() == 0) {
            return null;
        } else {
            return list;
        }

    }

    public List<Waifu> getWaifuByLinkFromChannel(String link, int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE e.link = :link AND e.channelId = :channelId");
        List<Waifu> list = query.setParameter("link", link.trim()).setParameter("channelId", channelId).getResultList();
        em.close();
        return list;
    }

    public List<Waifu> getBest(int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE  e.channelId = :channelId and e.points =(SELECT max(e.points) FROM e WHERE  e.channelId = :channelId)");

        List<Waifu> waifu = query.setParameter("channelId", channelId).getResultList();
        em.close();
        return waifu;
    }

    public List<Waifu> getWorst(int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT e FROM models.Waifu e WHERE  e.channelId = :channelId and e.points = (SELECT min(e.points) FROM e WHERE  e.channelId = :channelId)");

        List<Waifu> waifu = query.setParameter("channelId", channelId).getResultList();
        em.close();
        return waifu;
    }


    public void resetFight(int channelId) {

        String queryString = "Update palebot.Waifu Set palebot.waifu.POINTS = 0 WHERE CHANNEL_ID = :channelId";
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery(queryString, Waifu.class);
        em.close();
    }

    public Waifu getRandom() {
        //native query requires db names
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        String queryString = "SELECT * FROM palebot.Waifu ORDER BY RAND() LIMIT 1";
        em.getTransaction().begin();
        Query query = em.createNativeQuery(queryString, Waifu.class);
        List results = query.getResultList();
        em.close();
        if (results.size() == 0) {
            return null;
        } else {
            return (Waifu) results.get(0);
        }
    }


    public Waifu getRandomFromChannel(int channelId) {
        List<Waifu> list = getWaifuByChannel(channelId);

        Collections.sort(list, new Comparator<Waifu>() {
            public int compare(Waifu o1, Waifu o2) {
                if (o1.getPoints() == o2.getPoints())
                    return 0;
                return o1.getPoints() < o2.getPoints() ? -1 : 1;
            }
        });


        int pos = random.nextInt(list.size());

        if (pos > list.size() * .75) {
            pos = random.nextInt(list.size());
        }

        return list.get(pos);

    }


    public WaifuRank addRank(WaifuRank rank) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
       em.getTransaction().begin();
        WaifuRank newRank = em.merge(rank);
        em.getTransaction().commit();
        em.close();
        return newRank;
    }

    public WaifuRank getRandomRankFromChannel(int channelId, int tier) {
        String queryString = "SELECT * FROM palebot.waifurank WHERE CHANNEL_ID = :channelId AND TIER = :tier ORDER BY RAND() LIMIT 1";
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        Query query = em.createNativeQuery(queryString, WaifuRank.class);
        List results = query.setParameter("channelId", channelId).setParameter("tier", tier).getResultList();
        em.close();
        if (results.size() == 0) {
            return null;
        } else {
            return (WaifuRank) results.get(0);
        }
    }

    public Waifu addWaifu(Waifu waifu) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Waifu newWaifu = new Waifu();
        newWaifu.setName(waifu.getName().trim());
        newWaifu.setLink(waifu.getLink().trim());
        newWaifu.setChannelId(waifu.getChannelId());
        newWaifu.setUploader(waifu.getUploader());
        newWaifu.setPoints(0);
        em.getTransaction().begin();
        newWaifu = em.merge(newWaifu);
        em.getTransaction().commit();
        em.close();
        return newWaifu;

    }

    public Waifu updateWaifu(Waifu waifu) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Waifu updatedWaifu = getWaifuById(waifu.getId(), em);
        em.getTransaction().begin();
        if (updatedWaifu != null) {


            updatedWaifu.setName(waifu.getName());
            updatedWaifu.setChannelId(waifu.getChannelId());
            updatedWaifu.setLink(waifu.getLink());
            updatedWaifu.setPoints(waifu.getPoints());
            updatedWaifu.setClaimed(waifu.getClaimed());


        }
        em.getTransaction().commit();
        em.close();
        return updatedWaifu;

    }

    public boolean deleteWaifu(int id) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Waifu waifu = getWaifuById(id, em);
        if (waifu != null) {
            em.getTransaction().begin();
            em.remove(waifu);
            em.getTransaction().commit();
            em.close();
            return true;
        }

        em.close();
        return false;


    }

    public boolean deleteWaifuByLink(String link, int id) {

        List<Waifu> foundWaifu = getWaifuByLink(link, id);
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        for (Waifu waifu : foundWaifu) {

            em.remove(waifu);
            em.getTransaction().commit();
            em.close();
            return true;
        }
        em.close();
        return false;
    }

    public boolean deleteWaifuByLinkFromChannel(String link, int channelId) {

        List<Waifu> waifu = getWaifuByLinkFromChannel(link, channelId);
        if (waifu.size() > 0) {
            EntityManager em = PersistenceManager.getInstance().getEntityManager();
            em.getTransaction().begin();
            for (Waifu currentWaifu : waifu) {
                em.remove(currentWaifu);
            }
            em.getTransaction().commit();
            em.close();
            return true;
        }
        return false;
    }

}
