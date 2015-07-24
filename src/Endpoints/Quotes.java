package Endpoints;


import models.Quote;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by palepail on 7/23/2015.
 */
@Path("/quotes")
public class Quotes {
    @GET
    @Produces("application/json")
    public Quote getAll() {

    Quote quote = new Quote();
        quote.setAuthor("Derek");
        quote.setId(1);
        return quote;

    }
}
