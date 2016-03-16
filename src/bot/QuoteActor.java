package bot;

import managers.ChannelManager;
import managers.QuoteManager;
import models.Channel;
import models.Quote;
import org.pircbotx.hooks.events.MessageEvent;

import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created by palepail on 8/30/2015.
 */
public class QuoteActor {


        private static MessageManager messageManager ;
        private static QuoteManager quoteManager = QuoteManager.getInstance();
        private static ChannelManager channelManager = new ChannelManager();
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");


        private static String QUOTE_ID;

        String channelName;
        String userName;
        Channel channelEntity;
        String message;
        String command = "";

        public void setValues(MessageEvent event){
            channelName = event.getChannel().getName();

            userName = event.getUser().getNick();
            channelEntity = channelManager.getChannelByName(channelName.substring(1));
            QUOTE_ID = "QUOTES_"+channelEntity.getId();
            messageManager = MessageManager.getInstance(channelName);
            message = event.getMessage();
        }

        public boolean tooManyQuotes(MessageEvent event){
            if (messageManager.overLimit() || !messageManager.lock(QUOTE_ID, 10000)) {
                messageManager.sendMessage(event, userName + ", hold your horses.");
                return true;
            }else{
                return false;
            }
        }

        public void postRandomQuote(MessageEvent event)
        {
            Quote quote = quoteManager.getRandomFromChannel(channelEntity.getId());
            if(quote==null){
                messageManager.sendMessage(event,  "Quote Not Found");
            }else{
                messageManager.sendMessage(event, "Quote #" + quote.getId() + ": " + quote.getQuote() + " - " + quote.getQuotee() + " " + dateFormat.format(quote.getDate()));
            }

        }
    public void postQuoteById(MessageEvent event)
    {
       String quoteId =  message.substring(message.indexOf('#') + 1);
        Quote quote = quoteManager.getQuoteByIdFromChannel( quoteId,channelEntity.getId());
        if(quote==null){
            messageManager.sendMessage(event,  "Quote Not Found");
        }else{
            messageManager.sendMessage(event, "Quote #"+quote.getId() +": "+ quote.getQuote() +" - " + quote.getQuotee() + " " +  dateFormat.format(quote.getDate()));
        }

    }


        public void quoteSearch(MessageEvent event){

            if(message.contains("!quote search "))
            {
               message = message.replace("!quote search ","");
                command = "quote";
            }
            else if( message.contains("!fkosays search "))
            {
                message = message.replace("!fkosays search ","");
                command ="fkosays";
            }

            if(message.length()<3){
                messageManager.sendMessage(event, userName + ", please search for more than three characters.");
                return;
            }
            List<Quote> quotes = quoteManager.getQuoteResultsFromChannel(message, channelEntity.getId());

            if(quotes.size()>5)
            {
                long seed = System.nanoTime();
                Collections.shuffle(quotes, new Random(seed));
                quotes = quotes.subList(0,3);
                messageManager.sendMessage(event, "Showing 3 of " + quotes.size() + " quotes.");
            }
            String result = "";
            if (quotes.size() == 0) {
                messageManager.sendMessage(event, "Quote Not Found");
                return;
            } else {
                for (Quote currentQuote : quotes) {

                    result += "Quote #"+currentQuote.getId() +": "+  currentQuote.getQuote() +" - " + currentQuote.getQuotee() + " " + dateFormat.format(currentQuote.getDate()) + " ";
                }
                messageManager.sendMessage(event, result);

            }
        }

        public void quoteAdd(MessageEvent event){


            if(message.contains("!quote add "))
            {
               message = message.replace("!quote add ","");
                command = "quote";
            }
            else if( message.contains("!fkosays add "))
            {
               message = message.replace("!fkosays add ","");
                command ="fkosays";
            }

            if (message.indexOf("(") == -1 || message.indexOf(")") == -1 || message.substring(message.indexOf(")") + 1).isEmpty()) {
                messageManager.sendMessage(event, userName + ", correct quote syntax is !"+command+" add (NAME) quote");
                return;
            }
            if (message.substring(message.indexOf(")") + 2).length() > 140) {
                messageManager.sendMessage(event, userName + ", that quote is too long to comprehend");
                return;
            }

            String name = message.substring(message.indexOf("(") + 1, message.indexOf(")"));
            String quote = message.substring(message.indexOf(")") + 2);
            Quote quoteObject = new Quote();
            quoteObject.setAuthor(userName);
            quoteObject.setChannelId(channelEntity.getId());
            quoteObject.setQuote(quote);
            quoteObject.setQuotee(name);
            quoteObject.setDate(new Date());
            quoteManager.addQuote(quoteObject);
            messageManager.sendMessage(event, "Quote Added");

        }


        public void deleteQuote(MessageEvent event)
        {
            if (messageManager.isMod(channelName,userName)) {

                if(message.contains("!quote delete "))
                {
                  message = message.replace("!quote delete ","");
                }
                else if( message.contains("!fkosays delete "))
                {
                   message = message.replace("!fkosays delete ","");
                }

                String quoteId = message;
                quoteId = quoteId.replace("#","");

                if (quoteManager.deleteQuoteByIdFromChannel(quoteId, channelEntity.getId())) {
                    messageManager.sendMessage(event, "Quote Deleted");
                } else {
                    messageManager.sendMessage(event, "Quote Not Found");
                }
            }
            else{
                messageManager.sendMessage(event, userName + ", only mods can delete a quote.");
            }
        }


    }

