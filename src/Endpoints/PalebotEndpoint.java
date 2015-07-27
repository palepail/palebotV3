package endpoints;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import bot.Palebot;

/**
 * Created by palepail on 7/25/2015.
 */
@Path("/palebot")
public class PalebotEndpoint {

    Palebot palebot =  Palebot.getInstance();

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
    @Path("/join/{channel}")
    @Produces("application/json")
    public void palebotJoin(@PathParam("channel") String channelName) {
        palebot.joinServer(channelName);
    }

}
