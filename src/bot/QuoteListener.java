package bot;

import managers.ChannelManager;
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
                if ((event.getMessage().equals("!quote")|| event.getMessage().equals("!fkosays")) && !messageManager.overLimit() && !actor.tooManyQuotes(event)) {

                    actor.postRandomQuote(event);
                    return;
                }

                if ((event.getMessage().startsWith("!quote #") || event.getMessage().startsWith("!fkosays #")) && !messageManager.overLimit() && !actor.tooManyQuotes(event)) {
                    actor.postQuoteById(event);
                    return;
                }

                if ((message.startsWith("!quote search ")|| event.getMessage().startsWith("!fkosays search ")) && !messageManager.overLimit() && !actor.tooManyQuotes(event)) {


                    actor.quoteSearch(event);
                    return;
                }


                if ((message.startsWith("!quote add ")|| event.getMessage().startsWith("!fkosays add ")) && !messageManager.overLimit() && !actor.tooManyQuotes(event)) {

                    actor.quoteAdd(event);
                    return;
                }


                //OP commands

                if ((event.getMessage().startsWith("!quote delete ") || event.getMessage().startsWith("!fkosays delete")) && !messageManager.overLimit()) {
                    actor.deleteQuote(event);
                    return;
                }

            }


        }

    }
