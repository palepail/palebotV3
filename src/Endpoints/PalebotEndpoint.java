package endpoints;


import javax.ws.rs.*;

import bot.Palebot;
import dao.ChannelDAO;
import dao.QuoteDAO;
import models.Channel;

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
    @Path("/on")
    @Produces("application/json")
    public void palebotOn() {
        palebot.activateBot();
    }
    @GET
    @Path("/off")
    @Produces("application/json")
    public void palebotOff() {
        palebot.deactivateBot();
    }

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
    public void  addChannel(String channelName) {
        ChannelDao.updateChannel(channelName);
    }

}
