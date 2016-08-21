package dao;

import models.Quote;
import models.Waifu;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by palepail on 7/24/2015.
 */
public class QuoteDAO {


    Random random = new Random(System.currentTimeMillis());

    public List<Quote> getAll() {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.Quote e");
        em.close();
        return query.getResultList();
    }

    public Quote getQuoteById(int id) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Quote quote = em.find(Quote.class, id);
        em.close();
        return quote;
    }

    public void updateQuote(Quote quote) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.merge(quote);
        em.close();
    }

    public void deleteQuote(int id) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.remove(getQuoteById(id));
        em.close();
    }

    public Quote getQuoteByIdFromChannel(int quoteId, int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();

        Query query = em.createQuery("SELECT e FROM models.Quote e WHERE e.id = :quoteId AND e.channelId = :channelId");
        List<Quote> list = query.setParameter("quoteId", quoteId).setParameter("channelId", channelId).getResultList();
        em.close();
        return list.get(0);
    }

    public List<Quote> getQuotesFromChannel(int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.Quote e WHERE e.channelId = :channelId");
        List<Quote> list = query.setParameter("channelId", channelId).getResultList();
        em.close();
        return list;
    }

    public boolean deleteQuoteByIdFromChannel(int quoteId, int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Quote quoteToDelete = getQuoteByIdFromChannel(quoteId, channelId);
        Boolean flag = false;
        if (quoteToDelete != null) {
            em.getTransaction().begin();
            em.remove(quoteToDelete);
            em.getTransaction().commit();
            flag = true;
        }
        em.close();
        return flag;
    }

    public Quote getRandomFromChannel(int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        List<Quote> list = getQuotesFromChannel(channelId);
        em.close();
        return list.get(random.nextInt(list.size()));

    }

    public List<Quote> getQuoteResultsFromChannel(String searchCriteria, int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.Quote e WHERE UPPER(e.quote) like UPPER(:searchCriteria) OR  UPPER(e.quotee) like UPPER(:searchCriteria)  AND e.channelId = :channelId ");
        List<Quote> list =  query.setParameter("channelId", channelId).setParameter("searchCriteria", "%" + searchCriteria.trim() + "%").getResultList();
        em.close();
        return list;
    }

    public Quote getExactQuoteFromChannel(String searchCriteria, int channelId) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT e FROM models.Quote e WHERE UPPER(e.quote) = UPPER(:searchCriteria)  AND e.channelId = :channelId ");
        List<Quote> quotes = query.setParameter("channelId", channelId).setParameter("searchCriteria", searchCriteria.trim()).getResultList();
        em.close();
        if (quotes.size() > 0) {
            return quotes.get(0);
        }
        return null;
    }

    public boolean addQuote(Quote quoteToAdd) {
        EntityManager em = PersistenceManager.getInstance().getEntityManager();
        em.getTransaction().begin();
        em.persist(quoteToAdd);
        em.getTransaction().commit();
        em.close();
        return true;
    }

}
