package endpoints;


import dao.QuoteDAO;
import models.Quote;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by palepail on 7/23/2015.
 */
@Path("/quotes")
public class Quotes {
    QuoteDAO dao = new QuoteDAO();

    @GET
    @Produces("application/json")
    public List<Quote> getAll() {
        return dao.getAll();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Quote getById(@PathParam("id") int id) {
        return dao.getQuoteById(id);
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public void deleteById(@PathParam("id") int id) {
         dao.deleteQuote(id);
    }

    @POST
    @Produces("application/json")
    public void  addQuote(Quote quote) {
        dao.updateQuote(quote);
    }
}
