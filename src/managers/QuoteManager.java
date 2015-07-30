package managers;

import dao.QuoteDAO;
import models.Quote;

import java.util.List;

/**
 * Created by palepail on 7/29/2015.
 */
public class QuoteManager {
    QuoteDAO quoteDao = new QuoteDAO();

    public List<Quote> getAll() {
        return quoteDao.getAll();
    }

    public Quote getQuoteById(int id) {
        return quoteDao.getQuoteById(id);
    }

    public void deleteQuote(int id) {
        quoteDao.deleteQuote(id);
    }

    public void updateQuote(Quote quote) {
        quoteDao.updateQuote(quote);
    }
}
