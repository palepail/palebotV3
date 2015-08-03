package bot;

import managers.ChannelManager;
import managers.WaifuManager;
import models.Channel;
import models.Waifu;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.OpEvent;

import java.util.List;

/**
 * Created by palepail on 7/31/2015.
 */
public class AnimeListener extends ListenerAdapter {

    private static MessageManager messageManager = MessageManager.getInstance();
    private static WaifuManager waifuManager = new WaifuManager();
    private static ChannelManager channelManager = new ChannelManager();
    private static String ADD_WAIFU_ID = "ADD_WAIFU";
    private static String WAIFU_ID = "WAIFU";
    private static String NOT_FOUND_IMG = "http://i.imgur.com/c5IHJC9.png";

    public static final String NAME = "ANIME";


    @Override
    public void onMessage(MessageEvent event) {
        String channelName = event.getChannel().getName();
        String userName = event.getUser().getNick();
        Channel channelEntity = channelManager.getChannelByName(channelName.substring(1));

        String message = event.getMessage();

        if (event.getMessage().equals("!waifu")) {
            if (messageManager.overLimit() || !messageManager.lock(WAIFU_ID, 2000)) {
                messageManager.delayMessage(3000);
            }
            messageManager.reduceMessages(1);
            Waifu waifu = waifuManager.getRandomFromChannel(channelEntity.getId());
            if(waifu==null){
                event.getBot().sendIRC().message(channelName, userName + "'s waifu is " + NOT_FOUND_IMG);
                return;
            }
            event.getBot().sendIRC().message(channelName, userName + "'s waifu is " + waifu.getLink());
            return;
        }

        if (message.startsWith("!waifu search ")) {
            String name = message.substring(14);
            if(name.length()<3){
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(channelName, "Are trying to start a harem, " + userName +"?");
                return;
            }

            List<Waifu> waifu = waifuManager.getWaifuByNameFromChannel(name, channelEntity.getId());

            if(waifu.size()>5)
            {
                waifu = waifu.subList(0,5);
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(channelName, "Some wifu got stuck in the door");
            }
            String result = "";
            if (waifu.size() == 0) {
                event.getBot().sendIRC().message(event.getChannel().getName(), NOT_FOUND_IMG);
                return;
            } else {
                for (Waifu currentWaifu : waifu) {

                    result += currentWaifu.getLink() + " ";
                }
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(channelName, result);
            }
            return;
        }


        if (message.startsWith("!waifu add ")) {


            if (message.indexOf("(") == -1 || message.indexOf(")") == -1 || message.substring(message.indexOf(")") + 1).isEmpty()) {
                messageManager.reduceMessages(1);
                event.respond(", correct waifu syntax is !waifu add (NAME) LINK");
                return;
            }
            if (message.substring(message.indexOf(")") + 2).length() > 40) {
                event.respond(", that Link is too long to comprehend");
                return;
            }

            if (messageManager.overLimit() || !messageManager.lock(ADD_WAIFU_ID, 3000)) {

                messageManager.delayMessage(5000);
            }

            String name = message.substring(message.indexOf("(") + 1, message.indexOf(")"));
            String link = message.substring(message.indexOf(")") + 2);
            Waifu waifu = new Waifu();
            waifu.setLink(link);
            waifu.setName(name);
            waifu.setChannelId(channelEntity.getId());
            waifuManager.addWaifu(waifu);
            messageManager.reduceMessages(1);
            event.getBot().sendIRC().message(channelName, "waifu added");
            return;
        }

        //OP commands

        if (event.getMessage().startsWith("!waifu slap ")) {
            TwitchUsers users = messageManager.getTwitchUsers(channelName.substring(1));

            if (users.getChatters().getModerators().contains(userName)) {

                String link = message.substring(12);
                if (waifuManager.deleteWaifuByLinkFromChannel(link, channelEntity.getId())) {
                    messageManager.reduceMessages(1);
                    event.getBot().sendIRC().message(channelName, "Your waifu has left you for someone else");
                } else {
                    messageManager.reduceMessages(1);
                    event.getBot().sendIRC().message(channelName, "Your waifu was imaginary the entire time");
                }
            }
            else{
                messageManager.reduceMessages(1);
                event.respond(", how dare you try to slap a waifu");
            }
            return;

        }

    }

}
