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
}
