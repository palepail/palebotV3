package endpoints;


import javax.ws.rs.*;

import bot.Palebot;
import dao.ChannelDAO;
import dao.QuoteDAO;
import managers.ChannelManager;
import managers.PalebotManager;
import managers.QuoteManager;
import models.Channel;
import models.Quote;

import java.util.List;

/**
 * Created by palepail on 7/25/2015.
 */
@Path("/palebot")
public class PalebotEndpoint {

    ChannelManager channelManager = new ChannelManager();
    PalebotManager palebotManager = new PalebotManager();
    QuoteManager quoteManager = new QuoteManager();


    @GET
    @Path("/isOn")
    @Produces("application/json")
    public boolean palebotIsOn() {
       return palebotManager.isOn();
    }

    @POST
    @Path("/on")
    @Produces("application/json")
    public void palebotOn() {
        palebotManager.activateBot();
    }
    @POST
    @Path("/off")
    @Produces("application/json")
    public boolean palebotOff() {
        return palebotManager.deactivateBot();

    }

//============================ Channels ==============================
    @GET
    @Path("/channels")
    @Produces("application/json")
    public List<Channel> getAll() {
        return channelManager.getAll();
    }

    @GET
    @Path("/channels/{id}")
    @Produces("application/json")
    public Channel getById(@PathParam("id") int id) {
        return channelManager.getChannelById(id);
    }

    @DELETE
    @Path("/channels/{id}")
    public void deleteById(@PathParam("id") int id) {
        channelManager.deleteChannel(id);
    }

    @POST
    @Path("/channels")
    public Channel  addChannel(Channel channel) {
        return channelManager.insertChannel(channel);
    }

    //====================== Quotes ===============================
    @GET
    @Path("/quotes")
    @Produces("application/json")
    public List<Quote> getAllQuotes() {
        return quoteManager.getAll();
    }

    @GET
    @Path("/quotes/{id}")
    @Produces("application/json")
    public Quote getQuoteById(@PathParam("id") int id) {
        return quoteManager.getQuoteById(id);
    }

    @DELETE
    @Path("/quotes/{id}")
    @Produces("application/json")
    public void deleteQuoteById(@PathParam("id") int id) {
        quoteManager.deleteQuote(id);
    }

    @POST
    @Path("/quotes")
    @Produces("application/json")
    public void  addQuote(Quote quote) {
        quoteManager.updateQuote(quote);
    }

}
