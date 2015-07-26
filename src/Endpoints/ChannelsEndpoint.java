package endpoints;

import dao.ChannelDAO;
import models.Channel;
import models.Quote;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by palepail on 7/25/2015.
 */

@Path("/channels")
public class ChannelsEndpoint {
    ChannelDAO dao = new ChannelDAO();

    @GET
    @Produces("application/json")
    public List<Channel> getAll() {
        return dao.getAll();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Channel getById(@PathParam("id") int id) {
        return dao.getChannelById(id);
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public void deleteById(@PathParam("id") int id) {
        dao.deleteChannel(id);
    }

    @POST
    @Produces("application/json")
    public void  addChannel(Channel channel) {
        dao.updateChannel(channel);
    }
}
