package bot;

import managers.ChannelManager;
import managers.ListenerManager;
import managers.WaifuManager;
import models.Channel;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Created by palepail on 7/31/2015.
 */
public class WaifuListener extends ListenerAdapter {

    WaifuActor actor = new WaifuActor();
    private MessageManager messageManager;
    private static WaifuManager waifuManager = new WaifuManager();
    private static ChannelManager channelManager = new ChannelManager();
    private static ListenerManager listenerManager = new ListenerManager();


    public static final String NAME = "ANIME";
    public static final String WAIFU_SLEEP_KEY = "WAIFU_SLEEP";

    @Override
    public void onMessage(MessageEvent event) {


        if (event.getMessage().startsWith("!")) {
            String channelName = event.getChannel().getName();
            messageManager = MessageManager.getInstance(channelName);
            String userName = event.getUser().getNick();
            Channel channelEntity = channelManager.getChannelByName(channelName.substring(1));
            String message = event.getMessage();


            actor.setValues(event);
            if (messageManager.isMod(channelName, userName)) {
                if (event.getMessage().equals("!waifu on") && !messageManager.overLimit()) {
                    listenerManager.setActive(channelEntity.getId(), WaifuListener.NAME, true);
                    messageManager.sendMessage(event, "The waifu woke up");
                    return;
                }
                if (event.getMessage().equals("!waifu off") && !messageManager.overLimit()) {
                    listenerManager.setActive(channelEntity.getId(), WaifuListener.NAME, false);
                    messageManager.sendMessage(event, "The waifu are getting ready for a nap");
                    return;
                }
            }



            if (!listenerManager.getActive(channelEntity.getId(), WaifuListener.NAME) && (event.getMessage().startsWith("!waifu") || message.startsWith("!yourwaifu") || message.startsWith("!mywaifu"))) {
                if (!actor.tooManyWaifu(event, WAIFU_SLEEP_KEY, 600 * 1000)) {
                    messageManager.sendMessage(event, "Shhhh. The waifu are sleeping.");
                }
                return;
            }

            if (event.getMessage().equals("!waifu") && !messageManager.overLimit()) {

                actor.postRandomWaifu(event);
                return;
            }
            if (message.equals("!mywaifu") && !messageManager.overLimit()) {
                actor.myWaifu(event);
            }

            if (message.startsWith("!yourwaifu") && !messageManager.overLimit()) {

                actor.yourWaifu(event);
                return;
            }

            if (message.startsWith("!waifu search ") && !messageManager.overLimit()) {

                String searchCriteria = message.substring(14);
                actor.waifuSearch(event, searchCriteria);
                return;
            }
            if (message.startsWith("!waifu tier add ") && !messageManager.overLimit()) {

                String rankMessage = message.substring(16);
                actor.waifuAddRank(event, rankMessage);
                return;
            }


            if (message.startsWith("!waifu add ") && !messageManager.overLimit()) {

                actor.waifuAdd(event);
                return;
            }

            if (event.getMessage().startsWith("!waifu best") && !messageManager.overLimit()) {
                actor.waifuBest(event);
                return;
            }

            if (event.getMessage().startsWith("!waifu worst") && !messageManager.overLimit()) {
                actor.waifuWorst(event);
                return;
            }

            if (event.getMessage().equals("!waifu thirst") && !messageManager.overLimit()) {
                actor.waifuThirst(event);
                return;
            }
            if (event.getMessage().startsWith("!waifu thirstiest") && !messageManager.overLimit()) {
                actor.waifuThirstiest(event);
                return;
            }


            //OP commands

            if (event.getMessage().startsWith("!waifu slap ") && !messageManager.overLimit()) {
                actor.deleteWaifu(event);
                return;
            }


            if (event.getMessage().equals("!waifu fight reset")) {
                if (messageManager.isMod(channelName, userName)) {
                    waifuManager.resetFight(channelEntity.getId());
                }
            }

            if (event.getMessage().equals("!waifu fight") && !messageManager.overLimit()) {
                if (messageManager.isMod(channelName, userName)) {

                    actor.waifuFight(event);
                } else {
                    messageManager.reduceMessages(1);
                    event.getBot().sendIRC().message(channelName, userName + ", only mods may start a waifu fight");

                }

            }


        }



        if (event.getMessage().startsWith("1") || event.getMessage().startsWith("2")) {
            actor.waifuVote(event);
            return;
        }


    }

}
