package dao;

import models.Quote;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.spi.PersistenceProvider;
import java.util.HashMap;
import java.util.List;

/**
 * Created by palepail on 7/24/2015.
 */
public class QuoteDAO {
    PersistenceProvider provider = new HibernatePersistenceProvider();
    EntityManagerFactory factory = provider.createEntityManagerFactory("NewPersistenceUnit", new HashMap());
    EntityManager em = factory.createEntityManager();

    public  List<Quote> getAll(){
     Query query = em.createQuery("SELECT e FROM models.Quote e");
     return query.getResultList();
    }
}
