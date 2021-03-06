package endpoints;


import javax.ws.rs.*;

import bot.TwitchManager;
import dto.ChannelDTO;
import dto.WaifuDTO;
import managers.*;
import models.Channel;
import models.Listener;
import models.Quote;
import models.Waifu;

import java.util.List;

/**
 * Created by palepail on 7/25/2015.
 */
@Path("/palebot")
public class PalebotEndpoint {

    ChannelManager channelManager = ChannelManager.getInstance();
    WaifuManager waifuManager = WaifuManager.getInstance();
    PalebotManager palebotManager = PalebotManager.getInstance();
    QuoteManager quoteManager = QuoteManager.getInstance();
    ListenerManager listenerManager = ListenerManager.getInstance();
    TwitchManager twitchManager = TwitchManager.getInstance();


    @GET
    @Path("/status")
    @Produces("application/json")
    public String palebotGetStatus(@QueryParam("channel") String channel) {
        return palebotManager.getStatus(channel);
    }

    @POST
    @Path("/on")
    @Produces("application/json")
    public ChannelDTO palebotOn(@QueryParam("channel") String channelName) {
        palebotManager.activateBot(channelName);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return channelManager.getChannelDTOByName(channelName);
    }

    @POST
    @Path("/off")
    @Produces("application/json")
    public ChannelDTO palebotOff(@QueryParam("channel") String channelName) {
        palebotManager.deactivateBot(channelName);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return channelManager.getChannelDTOByName(channelName);
    }

    @GET
    @Path("/isPalebotAdmin")
    @Produces("application/json")
    public boolean isPalebotAdmin(@QueryParam("userName") String userName){
        return palebotManager.isPalebotAdmin(userName);
    }

    @GET
    @Path("/isChannelAdmin")
    @Produces("application/json")
    public boolean isChannelAdmin(@QueryParam("userName") String userName, @QueryParam("channelName") String channelName){
        return twitchManager.isMod(userName, channelName);
    }

    @GET
    @Path("/{channelId}/toggleListenerOn")
    @Produces("application/json")
    public ChannelDTO toggleListenerOn(@PathParam("channelId") int channelId, @QueryParam("listenerName") String listenerName) {
        listenerManager.addListenerToChannel(channelId, listenerName);
        palebotManager.updateListeners(channelId);
        return channelManager.getChannelDTOById(channelId);
    }
    @GET
    @Path("/{channelId}/toggleListenerOff")
    @Produces("application/json")
    public ChannelDTO toggleListenerOff(@PathParam("channelId") int channelId, @QueryParam("listenerName") String listenerName) {

        listenerManager.removeListenerFromChannel(channelId, listenerName);
        palebotManager.updateListeners(channelId);
        return channelManager.getChannelDTOById(channelId);
    }

    //============================ Anime =================================

    @GET
    @Path("/anime/waifu")
    @Produces("application/json")
    public List<WaifuDTO> getWaifuByChannel(@QueryParam("channelId") int channelId){

        return waifuManager.getWaifuDTOByChannel(channelId);
    }

    @POST
    @Path("/anime/waifu")
    @Produces("application/json")
    public List<WaifuDTO> SaveWaifu(@QueryParam("channelId") int channelId, List<WaifuDTO> waifu){
        return waifuManager.saveWaifuByChannel(channelId, waifu);
    }

    //============================ Channels ==============================

    @GET
    @Path("/channels/name/{channelName}")
    @Produces("application/json")
    public ChannelDTO getByName(@PathParam("channelName") String channelName) {
        return channelManager.getChannelDTOByName(channelName);
    }


    @GET
    @Path("/channels/id{channelId}")
    @Produces("application/json")
    public Channel getById(@PathParam("channelId") int id) {
        return channelManager.getChannelById(id);
    }

    @GET
    @Path("/channels")
    @Produces("application/json")
    public List<ChannelDTO> getAllChannels() {
        return channelManager.getAllDTO();
    }



    @DELETE
    @Path("/channels")
    public List<ChannelDTO> deleteById(@QueryParam("channelId") int id) {

        Channel channelToDelete = channelManager.getChannelById(id);
        channelManager.deleteChannel(id);
        palebotManager.deleteChannelByName(channelToDelete.getName());
        return getAllChannels();
    }

    @POST
    @Path("/channels")
    public Channel addChannel(Channel channel) {
        return channelManager.addChannel(channel);
    }

    //====================== Quotes ===============================
    @GET
    @Path("/quotes")
    @Produces("application/json")
    public List<Quote> getAllQuotes() {
        return quoteManager.getAll();
    }

    @GET
    @Path("/quotes/channel/{id}")
    @Produces("application/json")
    public List<Quote> getQuotesFromChannel(@PathParam("id") int id) {
        return quoteManager.getQuotesFromChannel(id);
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
    public void addQuote(Quote quote) {
        quoteManager.updateQuote(quote);
    }

    //========================== Listeners ============================


    @GET
    @Path("/listeners")
    @Produces("application/json")
    public List<Listener> getAllListeners() {
        return listenerManager.getAll();
    }

    //============================ Twitch =============================



    @GET
    @Path("/twitch/clientId")
    @Produces("application/json")
    public String getTwitchClientID() {
        return TwitchManager.getClientId();
    }
}
