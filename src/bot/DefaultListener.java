package bot;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Random;

/**
 * Created by palepail on 7/26/2015.
 */
public class DefaultListener extends ListenerAdapter{
    MessageManager messageManager = MessageManager.getInstance();
    @Override
    public void onMessage(MessageEvent event) {

        if(event.getMessage().startsWith("!palebot")) {
            if(!messageManager.overLimit()) {
                event.getBot().sendIRC().message(event.getChannel().getName(), "I'm palebot");
            }
        }

        if(event.getMessage().startsWith("!suicide")) {
            if(!messageManager.overLimit()) {
                event.getBot().sendIRC().message(event.getChannel().getName(), "/timeout " + event.getUser().getNick() + " 1");
            }
        }

        if(event.getMessage().startsWith("!dice")) {
            if(!messageManager.overLimit()) {
                Random rand = new Random();
                int number = rand.nextInt(5);
                number+=1;
                String flair="";
                switch (number)
                {
                    case 1:{
                        flair = "Kappa";
                        break;
                    }
                    case 2:{
                        flair = "BibleThump";
                        break;
                    }
                    case 3:{
                        flair = "DansGame";
                        break;
                    }
                    case 4:{
                        flair = "MVGame";
                        break;
                    }
                    case 5:{
                        flair = "FrankerZ";
                        break;
                    }
                    case 6:{
                        flair = "PogChamp";
                        break;
                    }
                }
                event.respond("rolled a "+number + " "+flair);
            }
        }



    }


}
