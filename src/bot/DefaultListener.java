package bot;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Created by palepail on 7/26/2015.
 */
public class DefaultListener extends ListenerAdapter{
    @Override
    public void onMessage(MessageEvent event) {
        //When someone says helloworld respond with "Hello World"
        if (event.getMessage().startsWith("!helloworld")) {
            event.respond("Hello world!");
        }
        if(event.getMessage().startsWith("!palebot")) {
            event.getBot().sendIRC().message(event.getChannel().getName(), "I'm palebot");
        }



    }


}
