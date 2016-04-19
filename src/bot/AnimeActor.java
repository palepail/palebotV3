package bot;

import managers.ChannelManager;
import managers.WaifuManager;
import models.Channel;
import models.Waifu;
import models.WaifuRank;
import models.WaifuThirst;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.*;

/**
 * Created by palepail on 8/3/2015.
 */
public class AnimeActor {
    private static MessageManager messageManager ;
    private static WaifuManager waifuManager = WaifuManager.getInstance();
    private static ChannelManager channelManager = new ChannelManager();
    public static HashMap<String, ArrayList> WAIFU_VOTE_MAP = new HashMap<>();
    public static HashMap<String, ArrayList> WAIFU_VOTERS_MAP = new HashMap<>();

    private static String WAIFU_ID;
    private static String WAIFU_ADD_ID;
    private static String WAIFU_FIGHT_ID;
    private static String WAIFU_SEARCH_ID;
    private static int WAIFU_FIGHT_TIME = 30000;
    private static String NOT_FOUND_IMG = "http://i.imgur.com/c5IHJC9.png";

    String channelName;
    String userName;
    Channel channelEntity;
    String message;

    public void setValues(MessageEvent event){
         channelName = event.getChannel().getName();

         userName = event.getUser().getNick();
         channelEntity = channelManager.getChannelByName(channelName.substring(1));
         WAIFU_ID = "WAIFU_"+channelEntity.getId();
         WAIFU_SEARCH_ID = "WAIFU_SEARCH_"+channelEntity.getId();
         WAIFU_FIGHT_ID = "WAIFU_FIGHT_"+channelEntity.getId();
         WAIFU_ADD_ID = "WAIFU_ADD_"+channelEntity.getId();
         messageManager = MessageManager.getInstance(channelName);
         message = event.getMessage();
    }

    public boolean tooManyWaifu(MessageEvent event, String ID){

        if(messageManager.isMod(channelName,userName))
        {
            return false;
        }

        if (messageManager.overLimit() || !messageManager.lock(ID, 30000)) {
            messageManager.sendMessage(event, userName + ", I can't handle that many waifu right now");
            return true;
        }else{
            return false;
        }
    }



    public void postRandomWaifu(MessageEvent event)
    {
        if(tooManyWaifu(event, WAIFU_ID)){
            return;
        }
        Waifu waifu = waifuManager.getRandomFromChannel(channelEntity.getId());
        if(waifu==null){
            messageManager.sendMessage(event,  userName + "'s waifu does not exist " + NOT_FOUND_IMG);
        }else{
            WaifuThirst thirst = new WaifuThirst();

            thirst.setUser(userName);
            thirst.setChannelId(channelEntity.getId());
            waifuManager.updateWaifuThirst(thirst);
            waifu.setPoints(waifu.getPoints() + 1);
            waifuManager.updateWaifu(waifu);
            messageManager.sendMessage(event, userName + "'s waifu is " +waifu.getName() + " - " + waifu.getLink());
        }

    }


    public void waifuSearch(MessageEvent event, String searchCriteria){
        if(tooManyWaifu(event, WAIFU_SEARCH_ID)){
            return;
        }
        if(searchCriteria.length()<3){
            messageManager.sendMessage(event, "Are trying to start a harem, " + userName + "?");
            return;
        }
        List<Waifu> waifu = waifuManager.getWaifuFromChannel(searchCriteria, channelEntity.getId());

        if(waifu.size()>5)
        {
            long seed = System.nanoTime();
            Collections.shuffle(waifu, new Random(seed));
            waifu = waifu.subList(0,5);
            messageManager.sendMessage(event, "Some waifu got stuck in the door");
        }
        String result = "";
        if (waifu.size() == 0) {
            messageManager.sendMessage(event, NOT_FOUND_IMG);
            return;
        } else {
            for (Waifu currentWaifu : waifu) {

                result += currentWaifu.getName() + " - "+ currentWaifu.getLink() + " ";
            }
            messageManager.sendMessage(event, result);

        }
    }

    public void waifuAdd(MessageEvent event){
        if(tooManyWaifu(event, WAIFU_ADD_ID)){
           return;

        }
        String regex = "\\!waifu add ?\\((.+)\\) ?(.{0,240})";
        if (!message.matches(regex)) {
            messageManager.sendMessage(event, userName + ", correct waifu syntax is !waifu add (NAME) LINK");
            return;
        }
        if (message.substring(message.indexOf(")") + 2).length() > 40) {
            messageManager.sendMessage(event, userName + ", that Link is too long to comprehend");
            return;
        }

        String name = message.substring(message.indexOf("(") + 1, message.indexOf(")"));
        String link = message.substring(message.indexOf(")") + 2);
        if(!link.contains("imgur"))
        {
            messageManager.sendMessage(event, userName + ", please use imgur. It just makes things easier");
            return;
        }
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
        messageManager.updateMods(channelName);
        if (messageManager.isMod(channelName,userName)) {

            String link = message.substring(12);

            List<Waifu> foundWaifu = waifuManager.getWaifuByLink(link, channelEntity.getId());

            for(Waifu waifu : foundWaifu)
            {
                   messageManager.sendMessage(event, waifu.getName()+" has left you for someone else");
                   waifuManager.deleteWaifuById(waifu.getId());
            }
            if(foundWaifu.size()==0)
            {
                messageManager.sendMessage(event, "You never had that waifu");
            }

        }
        else{
            messageManager.sendMessage(event, userName + ", how dare you try to slap a waifu");
        }
    }

    public void waifuBest(MessageEvent event)
    {
        if(tooManyWaifu(event, WAIFU_ID)){
            return;
        }
        long seed = System.nanoTime();
        List<Waifu> waifuList = waifuManager.getBest(channelEntity.getId());
        if(waifuList.size()>0)
        {
            Collections.shuffle(waifuList, new Random(seed));
            Waifu waifu = waifuList.get(0);
            messageManager.sendMessage(event, waifu.getName() + " gets around. She has been claimed "+waifu.getPoints()+" times. " + waifu.getLink());
        }
    }
    public void waifuWorst(MessageEvent event)
    {
        if(tooManyWaifu(event, WAIFU_ID)){
            return;
        }
        long seed = System.nanoTime();
        List<Waifu> waifuList = waifuManager.getWorst(channelEntity.getId());
        if(waifuList.size()>0)
        {
            Collections.shuffle(waifuList, new Random(seed));
            Waifu waifu = waifuList.get(0);
            messageManager.sendMessage(event, waifu.getName() + " is so pure, they have been claimed "+ waifu.getPoints() +" times. " + waifu.getLink());

        }
    }

    public void waifuThirst(MessageEvent event)
    {

       WaifuThirst thirst = waifuManager.getThirst(userName, channelEntity.getId());
        int tier=0;
        int count =0;
        if(thirst!=null){
            tier = thirst.getCount()/5;
            count = thirst.getCount();
        }
        WaifuRank rank = waifuManager.getRank(channelEntity.getId(), tier);
        if(rank==null)
        {
            messageManager.sendMessage(event, "Tier "+ tier +": "+ userName+ " has "+ count+ " waifu");
            return;
        }

        String text = rank.getRank().replace("NAME", userName).replace("COUNT", Integer.toString(count));

        messageManager.sendMessage(event, "Tier "+ tier +": " + text);
        return;

    }

    public void waifuThirstiest(MessageEvent event)
    {
        long seed = System.nanoTime();
        List<WaifuThirst> thirstList = waifuManager.getThirstiest(channelEntity.getId());

        if(thirstList.size()>0)
        {
            Collections.shuffle(thirstList, new Random(seed));
            WaifuThirst waifuThirst = thirstList.get(0);
            messageManager.sendMessage(event, waifuThirst.getUser() + " went for the harem route with " + waifuThirst.getCount() + " waifu.");
        }
    }

    public void waifuVote(MessageEvent event){
        setValues(event);
        ArrayList<String> voters = WAIFU_VOTERS_MAP.get(channelName);
        ArrayList<Integer> votes = WAIFU_VOTE_MAP.get(channelName);

        if(messageManager.isLocked(WAIFU_FIGHT_ID) && (!voters.contains(userName))) {
            voters.add(userName);
            if (event.getMessage().startsWith("1")) {
                votes.add(1);
            }else if(event.getMessage().startsWith("2")){
                votes.add(2);
            }
            WAIFU_VOTE_MAP.put(channelName, votes);
            WAIFU_VOTERS_MAP.put(channelName, voters);
        }

    }

    public void waifuAddRank(MessageEvent event, String trimmedMessage)
    {

        String regex = "\\!waifu tier add ?\\(([0-9]+)\\) ([A-Za-z0-9 _.,!\"'/$]+)";
        if (!message.matches(regex)) {
            messageManager.sendMessage(event, userName + ", correct tier syntax is !waifu tier add (NUMBER) MESSAGE");
            return;
        }
        WaifuRank rank = new WaifuRank();
        String number = message.substring(message.indexOf("(") + 1, message.indexOf(")"));
        String text = message.substring(message.indexOf(")") + 2);
        rank.setChannelId(channelEntity.getId());
        rank.setRank(text);
        rank.setTier(Integer.parseInt(number));

        waifuManager.addRank(rank);
        messageManager.sendMessage(event, "Tier Added");
        return;
    }

    public void waifuFight(MessageEvent event)
    {
        messageManager.updateMods(channelName);
        if(!messageManager.lock(WAIFU_FIGHT_ID, WAIFU_FIGHT_TIME))
        {
            return;
        }
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
        messageManager.sendMessage(event, waifu1.getName() + " - " + waifu1.getLink() + " VS " + waifu2.getName() + " - " + waifu2.getLink());
        messageManager.sendMessage(event, "Voting open for 30 seconds. 1: " + waifu1.getName() + " 2: " + waifu2.getName());

        if(WAIFU_VOTE_MAP.get(channelName)==null) {
            WAIFU_VOTE_MAP.put(channelName, new ArrayList());
            WAIFU_VOTERS_MAP.put(channelName, new ArrayList());
        }else
        {
            WAIFU_VOTERS_MAP.get(channelName).clear();
            WAIFU_VOTE_MAP.get(channelName).clear();
        }

        messageManager.delayMessage(WAIFU_FIGHT_TIME);

        int waifu1Votes = 0;
        int waifu2Votes = 0;
        ArrayList<Integer> votes = WAIFU_VOTE_MAP.get(channelName);
        for (int vote : votes){
            if(vote == 1)
            {
                waifu1Votes++;
            }else if(vote ==2)
            {
                waifu2Votes++;
            }
        }

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
        WAIFU_VOTE_MAP.get(channelName).clear();
        WAIFU_VOTERS_MAP.get(channelName).clear();

    }

}
