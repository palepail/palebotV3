package endpoints;


import dao.QuoteDAO;
import models.Quote;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
}
