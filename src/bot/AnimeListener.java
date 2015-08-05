package bot;

import managers.ChannelManager;
import managers.WaifuManager;
import models.Channel;
import models.Waifu;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.OpEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by palepail on 7/31/2015.
 */
public class AnimeListener extends ListenerAdapter {

    AnimeActor actor = new AnimeActor();
    private static MessageManager messageManager = MessageManager.getInstance();
    private static WaifuManager waifuManager = new WaifuManager();
    private static ChannelManager channelManager = new ChannelManager();

    private static String WAIFU_ID = "WAIFU";

    public static final String NAME = "ANIME";


    @Override
    public void onMessage(MessageEvent event) {
        String channelName = event.getChannel().getName();
        String userName = event.getUser().getNick();
        Channel channelEntity = channelManager.getChannelByName(channelName.substring(1));

        String message = event.getMessage();
        actor.setValues(event);

        if (event.getMessage().equals("!waifu")) {
            if (messageManager.overLimit() || !messageManager.lock(WAIFU_ID, 30000)) {
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(channelName, userName + ", I can't handle this many waifu right now");
                return;
            }
            actor.postRandomWaifu(event);
            return;
        }

        if (message.startsWith("!waifu search ")) {

            if (messageManager.overLimit() || !messageManager.lock(WAIFU_ID, 30000)) {
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(channelName, userName + ", I can't handle this many waifu right now");
                return;
            }
            String searchCriteria = message.substring(14);
            actor.waifuSearch(event, searchCriteria);
            return;
        }


        if (message.startsWith("!waifu add " )&& !messageManager.overLimit()) {
            if (messageManager.overLimit() || !messageManager.lock(WAIFU_ID, 30000)) {
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(channelName, userName + ", I can't handle this many waifu right now");
                return;
            }
            actor.waifuAdd(event);
            return;
        }

        if(event.getMessage().equals("!waifu best")&& !messageManager.overLimit())
        {
            if (messageManager.overLimit() || !messageManager.lock(WAIFU_ID, 30000)) {
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(channelName, userName + ", I can't handle this many waifu right now");
                return;
            }
            actor.waifuBest(event);
        }

        if(event.getMessage().equals("!waifu worst")&& !messageManager.overLimit())
        {
            if (messageManager.overLimit() || !messageManager.lock(WAIFU_ID, 30000)) {
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(channelName, userName + ", I can't handle this many waifu right now");
                return;
            }
          actor.waifuWorst(event);
        }


        if(actor.waifuFightOpen && event.getMessage().startsWith("1") && !actor.voters.contains(userName))
        {
            actor.voters.add(userName);
            actor.waifu1Votes++;
            return;
        }
        if(actor.waifuFightOpen && event.getMessage().startsWith("2") && !actor.voters.contains(userName))
        {
            actor.voters.add(userName);
            actor.waifu2Votes++;
            return;
        }

        //OP commands

        if (event.getMessage().startsWith("!waifu slap ")&& !messageManager.overLimit()) {
            actor.deleteWaifu(event);
            return;
        }


        if(event.getMessage().equals("!waifu fight reset")){
            if (messageManager.isMod(channelName,userName)) {
                waifuManager.resetFight(channelEntity.getId());
            }
        }

        if(event.getMessage().equals("!waifu fight") && actor.waifuFightOpen == false && !messageManager.overLimit()){
            if (messageManager.isMod(channelName,userName)) {

                actor.waifuFight(event);
            } else {
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(channelName, userName + ", only mods may start a waifu fight");

            }

        }

    }

}
