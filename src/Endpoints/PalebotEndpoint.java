package endpoints;


import javax.ws.rs.*;

import dto.ChannelDTO;
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
    @Path("/status")
    @Produces("application/json")
    public String palebotGetStatus(@QueryParam("channel") String channel) {
        return palebotManager.getStatus(channel);
    }

    @POST
    @Path("/on")
    @Produces("application/json")
    public void palebotOn(@QueryParam("channel") String channel) {
        palebotManager.activateBot(channel);
    }

    @POST
    @Path("/off")
    @Produces("application/json")
    public boolean palebotOff(@QueryParam("channel") String channel) {
        return palebotManager.deactivateBot(channel);

    }


//============================ Channels ==============================
    @GET
    @Path("/channels")
    @Produces("application/json")
    public List<ChannelDTO> getAllChannels() {
        return channelManager.getAllDTO();
    }

    @GET
    @Path("/channels/{id}")
    @Produces("application/json")
    public Channel getById(@PathParam("id") int id) {
        return channelManager.getChannelById(id);
    }

    @DELETE
    @Path("/channels/{id}")
    public List<ChannelDTO> deleteById(@PathParam("id") int id) {

        Channel channelToDelete = channelManager.getChannelById(id);
        channelManager.deleteChannel(id);
        palebotManager.deleteChannelByName(channelToDelete.getName());
       return getAllChannels();
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
