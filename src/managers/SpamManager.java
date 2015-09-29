package managers;

import dao.SpamDAO;
import models.Spam;

import java.util.List;

/**
 * Created by palepail on 9/29/2015.
 */
public class SpamManager {
    static SpamDAO spamDAO = new SpamDAO();
    private static SpamManager spamManager = new SpamManager();

    public static SpamManager getInstance(){
        return spamManager;
    }

    public List<Spam> getAll() {
        return spamDAO.getAll();
    }

    public boolean addSpam(Spam spam)
    {
        return spamDAO.addSpam(spam);
    }
}
