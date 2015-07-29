package endpoints;


import javax.ws.rs.*;

import bot.Palebot;
import dao.ChannelDAO;
import dao.QuoteDAO;
import models.Channel;
import models.Quote;

import java.util.List;

/**
 * Created by palepail on 7/25/2015.
 */
@Path("/palebot")
public class PalebotEndpoint {

    Palebot palebot =  Palebot.getInstance();
    ChannelDAO ChannelDao = new ChannelDAO();
    QuoteDAO QuoteDao = new QuoteDAO();

    @GET
    @Path("/isOn")
    @Produces("application/json")
    public boolean palebotIsOn() {
       return palebot.isOn();
    }

    @POST
    @Path("/on")
    @Produces("application/json")
    public void palebotOn() {
        palebot.activateBot();
    }
    @POST
    @Path("/off")
    @Produces("application/json")
    public boolean palebotOff() {
        return palebot.deactivateBot();

    }

//============================ Channels ==============================
    @GET
    @Path("/channels")
    @Produces("application/json")
    public List<Channel> getAll() {
        return ChannelDao.getAll();
    }

    @GET
    @Path("/channels/{id}")
    @Produces("application/json")
    public Channel getById(@PathParam("id") int id) {
        return ChannelDao.getChannelById(id);
    }

    @DELETE
    @Path("/channels/{id}")
    public void deleteById(@PathParam("id") int id) {
        ChannelDao.deleteChannel(id);
    }

    @POST
    @Path("/channels")
    public Channel  addChannel(Channel channel) {
        return ChannelDao.insertChannel(channel);
    }

    //====================== Quotes ===============================
    @GET
    @Path("/quotes")
    @Produces("application/json")
    public List<Quote> getAllQuotes() {
        return QuoteDao.getAll();
    }

    @GET
    @Path("/quotes/{id}")
    @Produces("application/json")
    public Quote getQuoteById(@PathParam("id") int id) {
        return QuoteDao.getQuoteById(id);
    }

    @DELETE
    @Path("/quotes/{id}")
    @Produces("application/json")
    public void deleteQuoteById(@PathParam("id") int id) {
        QuoteDao.deleteQuote(id);
    }

    @POST
    @Path("/quotes")
    @Produces("application/json")
    public void  addQuote(Quote quote) {
        QuoteDao.updateQuote(quote);
    }

}
