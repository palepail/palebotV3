package bot;

import managers.ChannelManager;
import models.Channel;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;



/**
 * Created by palepail on 8/30/2015.
 */
public class QuoteListener  extends ListenerAdapter{

        QuoteActor actor = new QuoteActor();
        private MessageManager messageManager;

        private static ChannelManager channelManager = new ChannelManager();


        public static final String NAME = "QUOTES";


        @Override
        public void onMessage(MessageEvent event) {



            if (event.getMessage().startsWith("!")) {
                String channelName = event.getChannel().getName();
                messageManager = MessageManager.getInstance(channelName);

                String message = event.getMessage();
                actor.setValues(event);
                if (event.getMessage().equals("!quote") && !messageManager.overLimit() && !actor.tooManyQuotes(event)) {
                    actor.postRandomQuote(event);
                    return;
                }

                if (event.getMessage().equals("!quote #") && !messageManager.overLimit() && !actor.tooManyQuotes(event)) {
                    actor.postQuoteById(event);
                    return;
                }

                if (message.startsWith("!quote search ") && !messageManager.overLimit() && !actor.tooManyQuotes(event)) {

                    String searchCriteria = message.substring(14);
                    actor.quoteSearch(event, searchCriteria);
                    return;
                }


                if (message.startsWith("!quote add ") && !messageManager.overLimit() && !actor.tooManyQuotes(event)) {

                    actor.quoteAdd(event);
                    return;
                }


                //OP commands

                if (event.getMessage().startsWith("!quote delete ") && !messageManager.overLimit()) {
                    actor.deleteQuote(event);
                    return;
                }

            }


        }

    }
