package bot;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

/**
 * Created by palepail on 7/26/2015.
 */
public class DefaultListener extends ListenerAdapter{
    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        //When someone says ?helloworld respond with "Hello World"
        if (event.getMessage().startsWith("!helloworld"))
            event.respond("Hello world!");
    }

}
