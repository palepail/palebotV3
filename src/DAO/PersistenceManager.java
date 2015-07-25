package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManager {
    private static PersistenceManager INSTANCE;
    private EntityManagerFactory emFactory;

    public static PersistenceManager getInstance(){
        if(PersistenceManager.INSTANCE == null){
            PersistenceManager.INSTANCE = new PersistenceManager();
        }
        return PersistenceManager.INSTANCE;
    }

    private PersistenceManager() {
        emFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
    }

    public EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }


    public void close() {
        emFactory.close();
    }
}