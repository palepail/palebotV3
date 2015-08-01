package bot;

import managers.WaifuManager;
import models.Waifu;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.OpEvent;

/**
 * Created by palepail on 7/31/2015.
 */
public class AnimeListener extends ListenerAdapter {

    MessageManager messageManager = MessageManager.getInstance();
    WaifuManager waifuManager = new WaifuManager();
    private String ADD_WAIFU_ID = "ADD_WAIFU";
    private String WAIFU_ID = "WAIFU";
    public static final String NAME = "ANIME";
    @Override
    public void onMessage(MessageEvent event) {
        if(event.getMessage().equals("!waifu")) {
            if(!messageManager.overLimit() && messageManager.lock(WAIFU_ID, 2000)) {
                messageManager.reduceMessages(1);
                Waifu waifu = waifuManager.getRandom();
                event.getBot().sendIRC().message(event.getChannel().getName(), event.getUser().getNick() + "'s waifu is " +waifu.getLink());
            }
        }



        if(event.getMessage().startsWith("!waifu add ")) {


            if(event.getMessage().indexOf("(")==-1 || event.getMessage().indexOf(")")==-1 || event.getMessage().substring(event.getMessage().indexOf(")")+1).isEmpty())
            {
                messageManager.reduceMessages(1);
                event.respond(", correct waifu syntax is !waifu add (NAME) LINK");
                return;
            }
            if(event.getMessage().substring(event.getMessage().indexOf(")")+2).length()>40)
            {
                event.respond(", that Link is too long to comprehend");
                return;
            }

            if(!messageManager.overLimit() && messageManager.lock(ADD_WAIFU_ID, 3000)) {

                String name = event.getMessage().substring(event.getMessage().indexOf("(") + 1, event.getMessage().indexOf(")"));
                String link = event.getMessage().substring(event.getMessage().indexOf(")")+2);
                Waifu waifu = new Waifu();
                waifu.setLink(link);
                waifu.setName(name);
                waifuManager.addWaifu(waifu);
                messageManager.reduceMessages(1);
                event.getBot().sendIRC().message(event.getChannel().getName(),"waifu added");
            }
        }

        //OP commands
        if(event.getUser().getChannelsOpIn().contains(event.getChannel()))
        {

        }
    }

}
