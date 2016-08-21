package managers;

import dao.QuoteDAO;
import models.Quote;

import java.util.List;

/**
 * Created by palepail on 7/29/2015.
 */
public class QuoteManager {
    static QuoteDAO quoteDao = new QuoteDAO();
    private static QuoteManager quoteManager = new QuoteManager();

    public static QuoteManager getInstance(){
        return quoteManager;
    }
    public List<Quote> getAll() {
        return quoteDao.getAll();
    }

    public List<Quote> getQuotesFromChannel(int channelId) {

        return quoteDao.getQuotesFromChannel(channelId);
    }

    public Quote getQuoteByIdFromChannel(String quoteId, int channelId) {
        Integer quoteIdInt = convertToInt(quoteId);
        if(quoteIdInt==null)
        {
            return null;
        }

        return quoteDao.getQuoteByIdFromChannel(quoteIdInt, channelId);
    }

    public boolean deleteQuoteByIdFromChannel(String quoteId, int channelId) {

        Integer quoteIdInt = convertToInt(quoteId);
        if(quoteIdInt==null)
        {
            return false;
        }
        return quoteDao.deleteQuoteByIdFromChannel(quoteIdInt, channelId);
    }

    public Quote getRandomFromChannel(int channelId){
        return quoteDao.getRandomFromChannel(channelId);
    }

    public List<Quote> getQuoteResultsFromChannel(String searchCriteria, int channelId){
        return quoteDao.getQuoteResultsFromChannel(searchCriteria, channelId);
    }
    public Quote getExactQuoteFromChannel(String searchCriteria, int channelId){
        return quoteDao.getExactQuoteFromChannel(searchCriteria, channelId);
    }

    public boolean addQuote(Quote quote)
    {
        return quoteDao.addQuote(quote);
    }

    public void deleteQuote(int id) {
        quoteDao.deleteQuote(id);
    }

    public void updateQuote(Quote quote) {
        quoteDao.updateQuote(quote);
    }

    private Integer convertToInt(String string)
    {
        Integer integer;
        try {
            integer = Integer.parseInt(string);
        } catch(NumberFormatException e) {
            return null;
        }
        return integer;
    }
}
