package dao;

import models.Quote;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by palepail on 7/24/2015.
 */
public class QuoteDAO {
    EntityManager em = PersistenceManager.getInstance().getEntityManager();

    public  List<Quote> getAll(){
     Query query = em.createQuery("SELECT e FROM models.Quote e");
     return query.getResultList();
    }

    public Quote getQuoteById(int id){
        return em.find(Quote.class, id);
    }
    public void updateQuote(Quote quote){
         em.merge(quote);
    }
    public void deleteQuote(int id){
        em.remove(getQuoteById(id));
    }

    public Quote getQuoteByIdFromChannel(int quoteId, int channelId){

        Query query = em.createQuery("SELECT e FROM models.Quote e WHERE e.id = :quoteId AND e.channelId = :channelId");
        List<Quote> list = query.setParameter("quoteId", quoteId).setParameter("channelId", channelId).getResultList();
        return list.get(0);
    }
    public List<Quote> getQuotesFromChannel(int channelId){
        Query query = em.createQuery("SELECT e FROM models.Quote e WHERE e.channelId = :channelId");
        List<Quote> list = query.setParameter("channelId", channelId).getResultList();
        return list;
    }

    public boolean deleteQuoteByIdFromChannel(int quoteId, int channelId)
    {
        Quote quoteToDelete = getQuoteByIdFromChannel(quoteId, channelId);
        if(quoteToDelete!= null)
        {
            em.getTransaction().begin();
            em.remove(quoteToDelete);
            em.getTransaction().commit();
            return true;
        }
        return false;
    }

    public Quote getRandomFromChannel(int channelId){
        //native query requires db names
        String queryString = "SELECT * FROM palebot.quote WHERE CHANNELID = :channelId ORDER BY RAND() LIMIT 1";

        Query query = em.createNativeQuery(queryString, Quote.class);
        List results = query.setParameter("channelId", channelId).getResultList();
        if(results.size()==0)
        {
            return null;
        }else{
            return (Quote) results.get(0);
        }
    }

    public  List<Quote> getQuoteResultsFromChannel(String searchCriteria, int channelId){
        Query query = em.createQuery("SELECT e FROM models.Quote e WHERE UPPER(e.quote) like UPPER(:searchCriteria) OR  UPPER(e.quotee) like UPPER(:searchCriteria)  AND e.channelId = :channelId ");
        return query.setParameter("channelId", channelId).setParameter("searchCriteria", "%" + searchCriteria.trim() + "%").getResultList();
    }

    public  boolean addQuote(Quote quoteToAdd){
        em.getTransaction().begin();
        em.persist(quoteToAdd);
        em.getTransaction().commit();
        return true;
    }

}
