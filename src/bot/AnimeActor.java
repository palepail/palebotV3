package bot;

import managers.ChannelManager;
import managers.WaifuManager;
import models.Channel;
import models.Waifu;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by palepail on 8/3/2015.
 */
public class AnimeActor {
    private static MessageManager messageManager = MessageManager.getInstance();
    private static WaifuManager waifuManager = new WaifuManager();
    private static ChannelManager channelManager = new ChannelManager();

    public static Boolean waifuFightOpen = false;
    public static int waifu1Votes = 0;
    public static int waifu2Votes = 0;

    public static ArrayList<String> voters = new ArrayList();

    private static String WAIFU_ID = "WAIFU";
    private static String NOT_FOUND_IMG = "http://i.imgur.com/c5IHJC9.png";

    String channelName;
    String userName;
    Channel channelEntity;
    String message;

    public void setValues(MessageEvent event){
         channelName = event.getChannel().getName();
         userName = event.getUser().getNick();
         channelEntity = channelManager.getChannelByName(channelName.substring(1));
         message = event.getMessage();
    }

    public boolean tooManyWaifu(MessageEvent event){
        if (messageManager.overLimit() || !messageManager.lock(WAIFU_ID, 30000)) {
            messageManager.sendMessage(event, userName + ", I can't handle that many waifu right now");
            return true;
        }else{
            return false;
        }
    }

    public void postRandomWaifu(MessageEvent event)
    {
        Waifu waifu = waifuManager.getRandomFromChannel(channelEntity.getId());
        if(waifu==null){
            messageManager.sendMessage(event,  userName + "'s waifu is " + NOT_FOUND_IMG);
        }else{
            messageManager.sendMessage(event, userName + "'s waifu is " + waifu.getLink());
        }

    }


    public void waifuSearch(MessageEvent event, String searchCriteria){

        if(searchCriteria.length()<3){
            messageManager.sendMessage(event, "Are trying to start a harem, " + userName + "?");
            return;
        }
        List<Waifu> waifu = waifuManager.getWaifuByNameFromChannel(searchCriteria, channelEntity.getId());

        if(waifu.size()>5)
        {
            long seed = System.nanoTime();
            Collections.shuffle(waifu, new Random(seed));
            waifu = waifu.subList(0,5);
            messageManager.sendMessage(event, "Some wifu got stuck in the door");
        }
        String result = "";
        if (waifu.size() == 0) {
            messageManager.sendMessage(event, NOT_FOUND_IMG);
            return;
        } else {
            for (Waifu currentWaifu : waifu) {

                result += currentWaifu.getLink() + " ";
            }
            messageManager.sendMessage(event, result);

        }
    }

    public void waifuAdd(MessageEvent event){
        if (message.indexOf("(") == -1 || message.indexOf(")") == -1 || message.substring(message.indexOf(")") + 1).isEmpty()) {
            messageManager.sendMessage(event, userName + ", correct waifu syntax is !waifu add (NAME) LINK");
            return;
        }
        if (message.substring(message.indexOf(")") + 2).length() > 40) {
            messageManager.sendMessage(event, userName + ", that Link is too long to comprehend");
            return;
        }

        String name = message.substring(message.indexOf("(") + 1, message.indexOf(")"));
        String link = message.substring(message.indexOf(")") + 2);
        Waifu waifu = new Waifu();
        waifu.setLink(link);
        waifu.setName(name);
        waifu.setUploader(userName);
        waifu.setChannelId(channelEntity.getId());
        waifuManager.addWaifu(waifu);
        messageManager.sendMessage(event, "Waifu Added");

    }

    public void deleteWaifu(MessageEvent event)
    {
        if (messageManager.isMod(channelName,userName)) {

            String link = message.substring(12);
            if (waifuManager.deleteWaifuByLinkFromChannel(link, channelEntity.getId())) {
                messageManager.sendMessage(event, "Your waifu has left you for someone else");
            } else {
                messageManager.sendMessage(event, "Your waifu was a figment of your imagination the whole time");
            }
        }
        else{
            messageManager.sendMessage(event, userName + ", how dare you try to slap a waifu");
        }
    }

    public void waifuBest(MessageEvent event)
    {
        long seed = System.nanoTime();
        List<Waifu> waifuList = waifuManager.getBest(channelEntity.getId());
        if(waifuList.size()>0)
        {
            Collections.shuffle(waifuList, new Random(seed));
            Waifu waifu = waifuList.get(0);
            messageManager.sendMessage(event, waifu.getName() + " is the one true waifu. Gaze upon her glory. " + waifu.getLink());
        }
    }
    public void waifuWorst(MessageEvent event)
    {
        long seed = System.nanoTime();
        List<Waifu> waifuList = waifuManager.getWorst(channelEntity.getId());
        if(waifuList.size()>0)
        {
            Collections.shuffle(waifuList, new Random(seed));
            Waifu waifu = waifuList.get(0);
            messageManager.sendMessage(event, waifu.getName() + " is lower than dirt. See for yourself. " + waifu.getLink());

        }
    }

    public void waifuFight(MessageEvent event)
    {
        Waifu waifu1 = waifuManager.getRandomFromChannel(channelEntity.getId());
        Waifu waifu2 = waifuManager.getRandomFromChannel(channelEntity.getId());
        int loopBreaker=0;
        while(waifu1.equals(waifu2))
        {
            waifu2 = waifuManager.getRandomFromChannel(channelEntity.getId());
            loopBreaker++;
            if(loopBreaker>10){
                messageManager.sendMessage(event, waifu1.getName() + " stands unopposed");
                return;
            }
        }
        messageManager.sendMessage(event, waifu1.getName() + waifu1.getName() + " - " + waifu1.getLink() + " VS " + waifu2.getName() + " - " + waifu2.getLink());
        messageManager.sendMessage(event, "Voting open for 30 seconds. 1: " + waifu1.getName() + " 2: " + waifu2.getName());

        waifuFightOpen = true;
        messageManager.delayMessage(30000);
        waifuFightOpen = false;

        if(waifu1Votes > waifu2Votes){
            messageManager.sendMessage(event, waifu1.getName() + " wins " + waifu1Votes + " to " + waifu2Votes);
            waifu1.setPoints(waifu1.getPoints() + 1);
            waifuManager.updateWaifu(waifu1);
        }
        else if(waifu1Votes < waifu2Votes){
            messageManager.sendMessage(event, waifu2.getName() + " wins " + waifu2Votes + " to " + waifu1Votes);
            waifu2.setPoints(waifu2.getPoints()+1);
            waifuManager.updateWaifu(waifu2);
        }else{
            messageManager.sendMessage(event, "It's a draw. " + waifu2Votes + " to " + waifu1Votes + ". Everyone wins!");
            waifu2.setPoints(waifu2.getPoints() + 1);
            waifu1.setPoints(waifu1.getPoints() + 1);
            waifuManager.updateWaifu(waifu2);
            waifuManager.updateWaifu(waifu1);
        }
        waifu1Votes=0;
        waifu2Votes =0;
        voters.clear();

    }

}
