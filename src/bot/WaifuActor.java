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
public class WaifuActor {
    private static MessageManager messageManager;
    private static TwitchManager twitchManager = TwitchManager.getInstance();
    private static WaifuManager waifuManager = WaifuManager.getInstance();
    private static ChannelManager channelManager = new ChannelManager();
    public static HashMap<String, ArrayList> WAIFU_VOTE_MAP = new HashMap<>();
    public static HashMap<String, ArrayList> WAIFU_VOTERS_MAP = new HashMap<>();
    private static RNGManager rng = RNGManager.getInstance();

    public static int BASE_TIME = 60;
    public static int TIME_RANGE = 240;

    private static String WAIFU_ID;
    private static String WAIFU_ADD_ID;
    private static String WAIFU_FIGHT_ID;
    private static String WAIFU_SEARCH_ID;

    private static int WAIFU_COOLDOWN_TIME =  5 * 60 * 1000;

    private static int WAIFU_FIGHT_TIME = 30000;
    private static String NOT_FOUND_IMG = "http://i.imgur.com/c5IHJC9.png";

    String channelName;
    Channel channelEntity;




    public void setValues(MessageEvent event) {
        channelName = event.getChannel().getName();

        channelEntity = channelManager.getChannelByName(channelName.substring(1));
        WAIFU_ID = "WAIFU_" + channelEntity.getId();
        WAIFU_SEARCH_ID = "WAIFU_SEARCH_" + channelEntity.getId();
        WAIFU_FIGHT_ID = "WAIFU_FIGHT_" + channelEntity.getId();
        WAIFU_ADD_ID = "WAIFU_ADD_" + channelEntity.getId();
        messageManager = MessageManager.getInstance(channelName);
    }


    public boolean tooManyWaifu(MessageEvent event, String ID) {

        if (twitchManager.isMod(channelName, event.getUser().getNick())) {
            twitchManager.updateMods(channelName);
            return false;

        }

        if (messageManager.overLimit() || !messageManager.lock(ID, BASE_TIME * 1000)) {
            messageManager.sendMessage(event, event.getUser().getNick() + ", don't be greedy.");
            return true;
        } else {
            return false;
        }
    }

    public boolean tooManyWaifu(MessageEvent event, String ID, int time) {

        if (twitchManager.isMod(channelName, event.getUser().getNick())) {
            twitchManager.updateMods(channelName);
            return false;

        }

        if (messageManager.overLimit() || !messageManager.lock(ID, time)) {
                messageManager.sendMessage(event, "/w " + event.getUser().getNick() + " Remaining cooldown is "+ messageManager.getRemainingTime(ID).toString("mm:ss")+ ". Please wait" );
            return true;
        } else {
            return false;
        }
    }

    public void myWaifu(MessageEvent event) {


        if (tooManyWaifu(event, WAIFU_ID + event.getUser().getNick(), getWaifuCooldown(event.getUser().getNick()))) {

            return;
        }

        Waifu waifu = waifuManager.getClaimed(channelEntity.getId(), event.getUser().getNick());

        if (waifu != null) {
            messageManager.sendMessage(event, event.getUser().getNick() + "'s waifu for laifu is " + waifu.getName() +" (" + waifuManager.findWaifuRarity(waifu.getId(),channelEntity.getId()) +") - " + waifu.getLink());

        } else {

            messageManager.sendMessage(event, event.getUser().getNick() + " may be forever alone. BibleThump");
        }

        return;

    }

    public void yourWaifu(MessageEvent event) {

        if (tooManyWaifu(event, WAIFU_ID + event.getUser().getNick(), getWaifuCooldown(event.getUser().getNick()))) {
            return;
        }

        String message = event.getMessage().replace("!yourwaifu","").trim();

        if(message.equalsIgnoreCase(event.getUser().getNick()))
        {
            return;
        }

        Waifu waifu = waifuManager.getClaimed(channelEntity.getId(), message);
        if (waifu != null) {
            String rarity = waifuManager.findWaifuRarity(waifu.getId(),channelEntity.getId());
            messageManager.sendMessage(event, message + "'s waifu for laifu is " + waifu.getName() + "("+ rarity + ") - " + waifu.getLink());

        } else {

            messageManager.sendMessage(event, message + " may be forever alone. BibleThump");
        }

        return;

    }

    public void postRandomWaifu(MessageEvent event) {

        if (tooManyWaifu(event, WAIFU_ID + event.getUser().getNick(), getWaifuCooldown(event.getUser().getNick()))) {
            return;
        }
       String rarity = rng.getRarity();
        Waifu waifu = waifuManager.getRandomFromChannel(channelEntity.getId(), rarity);
        if (waifu == null) {
            messageManager.sendMessage(event, event.getUser().getNick() + "'s waifu does not exist " + NOT_FOUND_IMG);
        } else {
            WaifuThirst thirst = new WaifuThirst();

            thirst.setUser(event.getUser().getNick());
            thirst.setChannelId(channelEntity.getId());
            waifuManager.updateWaifuThirst(thirst , 1);
            waifu.setPoints(waifu.getPoints() + 1);


            waifuManager.removeClaimed(channelEntity.getId(), event.getUser().getNick());
            if (waifu.getClaimed() == null || waifu.getClaimed().equalsIgnoreCase(event.getUser().getNick())) {
                messageManager.sendMessage(event, event.getUser().getNick() + "'s waifu is " + waifu.getName() + " ("+ rarity + ") - " + waifu.getLink());
            } else {

                messageManager.sendMessage(event, event.getUser().getNick() + " has stolen " + waifu.getName() + " ("+ rarity + ") from " + waifu.getClaimed() + " - " + waifu.getLink());
            }

            waifu.setClaimed(event.getUser().getNick());
            waifuManager.updateWaifu(waifu);
        }

    }


    public void waifuSearch(MessageEvent event, String searchCriteria) {
        if (tooManyWaifu(event, WAIFU_ID + event.getUser().getNick(), getWaifuCooldown(event.getUser().getNick()))) {
            return;
        }
        if (searchCriteria.length() < 3) {
            messageManager.sendMessage(event, "Are trying to start a harem, " + event.getUser().getNick() + "?");
            return;
        }
        List<Waifu> waifu = waifuManager.getWaifuFromChannel(searchCriteria, channelEntity.getId());

        if (waifu.size() > 5) {
            long seed = System.nanoTime();
            Collections.shuffle(waifu, new Random(seed));
            waifu = waifu.subList(0, 5);
            messageManager.sendMessage(event, "Some waifu got stuck in the door");
        }
        String result = "";
        if (waifu.size() == 0) {
            messageManager.sendMessage(event, NOT_FOUND_IMG);
            return;
        } else {
            for (Waifu currentWaifu : waifu) {
                String rarity = waifuManager.findWaifuRarity(currentWaifu.getId(),channelEntity.getId());
                result += currentWaifu.getName() + "("+ rarity + ") - " + currentWaifu.getLink() + " " + (currentWaifu.getClaimed()!= null ? " ("+currentWaifu.getClaimed()+") " : "");
            }
            messageManager.sendMessage(event, result);

        }
    }

    public void lureWaifu(MessageEvent event) {

        if (tooManyWaifu(event, WAIFU_ID + event.getUser().getNick(), getWaifuCooldown(event.getUser().getNick()))) {
            return;
        }
        int points = 1;

        String regex =  "\\!waifu lure (\\d{1,3}) (.{0,250})";
        if (!event.getMessage().matches(regex)) {
            messageManager.sendMessage(event, event.getUser().getNick() + ", correct waifu lure syntax is !waifu lure NUMBER QUERY");
            return;
        }
       String message = event.getMessage().replace("!waifu lure", "").trim();
        String[] split = message.split(" ");
        int number = Integer.parseInt(split[0]);

        if(number>100 || number < 1)
        {
            messageManager.sendMessage(event, event.getUser().getNick() + ", NUMBER must be between 1-100");
            return;
        }
        String searchCriteria = split[1];

        WaifuThirst thirst = waifuManager.getThirst(event.getUser().getNick(), channelEntity.getId());
        if(thirst.getCount() > number)
        {
            waifuManager.updateWaifuThirst(thirst, -number);

            List<Waifu> waifuList = waifuManager.getWaifuFromChannel(searchCriteria, channelEntity.getId());
            int odds = rng.getRandom(1,100);
            Waifu waifu;
            if(number < odds || waifuList.size()==0)
            {
                String rarity = rng.getRarity();
                waifu = waifuManager.getRandomFromChannel(channelEntity.getId(),rarity);
                messageManager.sendMessage(event, event.getUser().getNick() + ", "+ waifu.getName()+ " ("+rarity+") became your waifu instead -"+ waifu.getLink());

            }else{
                if(waifuList.size()>1) {
                    Collections.shuffle(waifuList, new Random(System.nanoTime()));
                }
                waifu = waifuList.get(0);
                if(waifu.getClaimed() == null || waifu.getClaimed().equalsIgnoreCase(event.getUser().getNick())) {
                    messageManager.sendMessage(event, waifu.getName() + " ("+waifuManager.findWaifuRarity(waifu.getId(),channelEntity.getId())+") has fallen head over heels for " + event.getUser().getNick() +". "+ waifu.getLink());
                    points=15;
                }else {

                    messageManager.sendMessage(event, waifu.getName() + " left " +waifu.getClaimed()+" to be with "+ event.getUser().getNick() +". " + waifu.getLink() );
                }

            }
            waifuManager.removeClaimed(channelEntity.getId(), event.getUser().getNick());
            waifu.setClaimed(event.getUser().getNick());
            waifu.setPoints(waifu.getPoints() + points);
            waifuManager.updateWaifu(waifu);

        } else{

            messageManager.sendMessage(event, event.getUser().getNick() + ", you aren't thirsty enough for that.");

        }


    }

    public void waifuAdd(MessageEvent event) {

        String regex = "\\!waifu add ?\\((.+)\\) ?(.{0,240})";
        if (!event.getMessage().matches(regex)) {
            messageManager.sendMessage(event, event.getUser().getNick() + ", correct waifu syntax is !waifu add (NAME) LINK");
            return;
        }


        String name = event.getMessage().substring(event.getMessage().indexOf("(") + 1, event.getMessage().lastIndexOf(")"));
        String link = event.getMessage().substring(event.getMessage().lastIndexOf(")") + 2);
        if (name.length() > 45) {
            messageManager.sendMessage(event, event.getUser().getNick() + ", that Name is too long.");
            return;
        }

        if (link.length() > 40) {
            messageManager.sendMessage(event, event.getUser().getNick() + ", that Link is too long to comprehend");
            return;
        }
        if (!link.contains("imgur") ) {
            messageManager.sendMessage(event, event.getUser().getNick() + ", please use imgur. It just makes things easier");
            return;
        }
        if (link.contains("/a/") || link.contains("gallery") || link.lastIndexOf('.') < (link.length()-5)) {
            messageManager.sendMessage(event, event.getUser().getNick() + ", please use the direct link. It just makes things easier");
            return;
        }

        List<Waifu> check = waifuManager.getWaifuByLink(link, channelEntity.getId());

        if(check==null || check.size()==0) {

            Waifu waifu = new Waifu();
            waifu.setLink(link);
            waifu.setName(name);
            waifu.setUploader(event.getUser().getNick());
            waifu.setChannelId(channelEntity.getId());
            waifuManager.addWaifu(waifu);
            messageManager.sendMessage(event, "Waifu Added");
        }else{
            messageManager.sendMessage(event, "That Waifu Is already here");
        }

    }

    public void deleteWaifu(MessageEvent event) {

        if (twitchManager.isMod(channelName, event.getUser().getNick())) {

            String link = event.getMessage().substring(12);

            List<Waifu> foundWaifu = waifuManager.getWaifuByLink(link, channelEntity.getId());
            if (foundWaifu == null || foundWaifu.size() == 0) {
                messageManager.sendMessage(event, "You never had that waifu");
            }
            for (Waifu waifu : foundWaifu) {
                messageManager.sendMessage(event, waifu.getName() + " has left you for someone else");
                waifuManager.deleteWaifuById(waifu.getId());
            }


        } else {
            messageManager.sendMessage(event, event.getUser().getNick() + ", how dare you try to slap a waifu");
        }
    }

    public void waifuBest(MessageEvent event) {
        if (tooManyWaifu(event, WAIFU_ID)) {
            return;
        }
        long seed = System.nanoTime();
        List<Waifu> waifuList = waifuManager.getBest(channelEntity.getId());
        if (waifuList.size() > 0) {
            Collections.shuffle(waifuList, new Random(seed));
            Waifu waifu = waifuList.get(0);
            messageManager.sendMessage(event, waifu.getName() + " gets around. She has been claimed " + waifu.getPoints() + " times. " + waifu.getLink());
        }
    }

    public void waifuWorst(MessageEvent event) {
        if (tooManyWaifu(event, WAIFU_ID)) {
            return;
        }
        long seed = System.nanoTime();
        List<Waifu> waifuList = waifuManager.getWorst(channelEntity.getId());
        if (waifuList.size() > 0) {
            Collections.shuffle(waifuList, new Random(seed));
            Waifu waifu = waifuList.get(0);
            messageManager.sendMessage(event, waifu.getName() + " is so pure, they have been claimed " + waifu.getPoints() + " times. " + waifu.getLink());

        }
    }

    public void waifuThirst(MessageEvent event) {

        WaifuThirst thirst = waifuManager.getThirst(event.getUser().getNick(), channelEntity.getId());
        int tier = 0;
        int count = 0;
        if (thirst != null) {
            tier = thirst.getCount() / 5;
            count = thirst.getCount();
        }
        WaifuRank rank = waifuManager.getRank(channelEntity.getId(), tier);
        if (rank == null) {
            messageManager.sendMessage(event, "Tier " + tier + ": " + event.getUser().getNick() + " has " + count + " waifu");
            return;
        }

        String text = rank.getRank().replace("NAME", event.getUser().getNick()).replace("COUNT", Integer.toString(count));

        messageManager.sendMessage(event, "Tier " + tier + ": " + text);
        return;

    }

    public void waifuThirstiest(MessageEvent event) {
        long seed = System.nanoTime();
        List<WaifuThirst> thirstList = waifuManager.getThirstiest(channelEntity.getId());

        if (thirstList.size() > 0) {
            Collections.shuffle(thirstList, new Random(seed));
            WaifuThirst waifuThirst = thirstList.get(0);
            messageManager.sendMessage(event, waifuThirst.getUser() + " went for the harem route with " + waifuThirst.getCount() + " waifu.");
        }
    }

    public void waifuVote(MessageEvent event) {
        setValues(event);
        ArrayList<String> voters = WAIFU_VOTERS_MAP.get(channelName);
        ArrayList<Integer> votes = WAIFU_VOTE_MAP.get(channelName);

        if (messageManager.isLocked(WAIFU_FIGHT_ID) && (!voters.contains(event.getUser().getNick()))) {
            voters.add(event.getUser().getNick());
            if (event.getMessage().startsWith("1")) {
                votes.add(1);
            } else if (event.getMessage().startsWith("2")) {
                votes.add(2);
            }
            WAIFU_VOTE_MAP.put(channelName, votes);
            WAIFU_VOTERS_MAP.put(channelName, voters);
        }

    }

    public void waifuAddRank(MessageEvent event, String trimmedMessage) {

        String regex = "\\!waifu tier add ?\\(([0-9]+)\\) ([A-Za-z0-9 _.,!\"'/$]+)";
        if (!event.getMessage().matches(regex)) {
            messageManager.sendMessage(event, event.getUser().getNick() + ", correct tier syntax is !waifu tier add (NUMBER) MESSAGE");
            return;
        }
        WaifuRank rank = new WaifuRank();
        String number = event.getMessage().substring(event.getMessage().indexOf("(") + 1, event.getMessage().indexOf(")"));
        String text = event.getMessage().substring(event.getMessage().indexOf(")") + 2);
        rank.setChannelId(channelEntity.getId());
        rank.setRank(text);
        rank.setTier(Integer.parseInt(number));

        waifuManager.addRank(rank);
        messageManager.sendMessage(event, "Tier Added");
        return;
    }

    public void waifuFight(MessageEvent event) {
        twitchManager.updateMods(channelName);
        if (!messageManager.lock(WAIFU_FIGHT_ID, WAIFU_FIGHT_TIME)) {
            return;
        }
        String rarity = rng.getRarity();
        Waifu waifu1 = waifuManager.getRandomFromChannel(channelEntity.getId(),rarity);
        Waifu waifu2 = waifuManager.getRandomFromChannel(channelEntity.getId(),rarity);
        int loopBreaker = 0;
        while (waifu1.equals(waifu2)) {
            waifu2 = waifuManager.getRandomFromChannel(channelEntity.getId(),rarity);
            loopBreaker++;
            if (loopBreaker > 10) {
                messageManager.sendMessage(event, waifu1.getName() + " stands unopposed");
                return;
            }
        }
        messageManager.sendMessage(event, waifu1.getName() + " - " + waifu1.getLink() + " VS " + waifu2.getName() + " - " + waifu2.getLink());
        messageManager.sendMessage(event, "Voting open for 30 seconds. 1: " + waifu1.getName() + " 2: " + waifu2.getName());

        if (WAIFU_VOTE_MAP.get(channelName) == null) {
            WAIFU_VOTE_MAP.put(channelName, new ArrayList());
            WAIFU_VOTERS_MAP.put(channelName, new ArrayList());
        } else {
            WAIFU_VOTERS_MAP.get(channelName).clear();
            WAIFU_VOTE_MAP.get(channelName).clear();
        }

        messageManager.delayMessage(WAIFU_FIGHT_TIME);

        int waifu1Votes = 0;
        int waifu2Votes = 0;
        ArrayList<Integer> votes = WAIFU_VOTE_MAP.get(channelName);
        for (int vote : votes) {
            if (vote == 1) {
                waifu1Votes++;
            } else if (vote == 2) {
                waifu2Votes++;
            }
        }

        if (waifu1Votes > waifu2Votes) {
            messageManager.sendMessage(event, waifu1.getName() + " wins " + waifu1Votes + " to " + waifu2Votes);
            waifu1.setPoints(waifu1.getPoints() + 1);
            waifuManager.updateWaifu(waifu1);
        } else if (waifu1Votes < waifu2Votes) {
            messageManager.sendMessage(event, waifu2.getName() + " wins " + waifu2Votes + " to " + waifu1Votes);
            waifu2.setPoints(waifu2.getPoints() + 1);
            waifuManager.updateWaifu(waifu2);
        } else {
            messageManager.sendMessage(event, "It's a draw. " + waifu2Votes + " to " + waifu1Votes + ". Everyone wins!");
            waifu2.setPoints(waifu2.getPoints() + 1);
            waifu1.setPoints(waifu1.getPoints() + 1);
            waifuManager.updateWaifu(waifu2);
            waifuManager.updateWaifu(waifu1);
        }
        WAIFU_VOTE_MAP.get(channelName).clear();
        WAIFU_VOTERS_MAP.get(channelName).clear();

    }

    public void postBooru(MessageEvent event){


        if (tooManyWaifu(event, WAIFU_ID + event.getUser().getNick(), WAIFU_COOLDOWN_TIME)) {
            return;
        }


        ArrayList categories;

        categories = getBooruList();

        BooruImage[] images = null;
        int retries = 0;
        while (images == null && retries < 3)
        {
            String tag = (String) categories.get(rng.getRandom(0, categories.size()));
            images = BooruManager.getInstance().getBooruImages(tag);
            retries++;
        }

        if(images.length>0) {

            List<BooruImage> list = Arrays.asList(images);
            Collections.shuffle(Arrays.asList(list), new Random(System.nanoTime()));
            BooruImage image = list.get(0);
            String longUrl = "http://danbooru.donmai.us" + image.file_url;

            String charName= "Original - ";
            if(image.tag_string_character.split(" ")[0] != null && image.tag_string_character.split(" ")[0].length() > 1) {
             charName = image.tag_string_character.split(" ")[0] + " - ";
            }
            messageManager.sendMessage(event, charName + BooruManager.getInstance().getShortenedUrl(longUrl));
        }

    }

    public void postGelBooru(MessageEvent event){

        int time = (int) ((BASE_TIME * 1000) + ((Math.random() * TIME_RANGE + 1) * 1000));
        if (tooManyWaifu(event, WAIFU_ID + event.getUser().getNick(), time)) {
            return;
        }


        ArrayList categories;

        GelBooruImage[] images = null;
        int retries = 0;
        categories = getGelBooruList();
        while (images == null && retries < 3)
        {
            String tag = (String) categories.get(rng.getRandom(0, categories.size()));
            images = BooruManager.getInstance().getGelBooruImages(tag);
            retries++;
        }

        if(images.length>0) {
            GelBooruImage image = images[rng.getRandom(0,images.length)];
            String longUrl = image.file_url;

            messageManager.sendMessage(event,longUrl);
        }
    }


    private int getWaifuCooldown(String username){

//        if(twitchManager.isSubscriber(username))
//        {
//            return WAIFU_COOLDOWN_TIME - 1*60*1000;
//        }
        return WAIFU_COOLDOWN_TIME;
    }
    private ArrayList getBooruList(){

        ArrayList replies = new ArrayList();
        replies.add("armpits");
        replies.add("breasts");
        replies.add("large_breasts");
        replies.add("bikini");
        replies.add("small_breasts");
        replies.add("cleavage");
        replies.add("panties");
        replies.add("underwear");
        replies.add("navel");
        replies.add("medium_breats");
        replies.add("open_clothes");
        replies.add("sideboob");
        replies.add("ass");
        replies.add("skirt_lift");
        replies.add("hanging_breasts");
        replies.add("huge_breasts");
        replies.add("side-tie_panties");
        replies.add("cat_lingerie");
        replies.add("underwear_only");
        replies.add("lingerie");

        return replies;
    }

    private ArrayList getGelBooruList(){

        ArrayList replies = new ArrayList();
        replies.add("armpits");
        replies.add("breasts");
        replies.add("large_breasts");
        replies.add("bikini");
        replies.add("small_breasts");
        replies.add("cleavage");
        replies.add("thighhighs");
        replies.add("panties");
        replies.add("underwear");
        replies.add("skirt");
        replies.add("navel");
        replies.add("bare_shoulders");
        replies.add("sweat");
        replies.add("open_clothes");
        replies.add("medium_breasts");
        replies.add("loli");
        replies.add("restrained");
        replies.add("huge_breasts");
        replies.add("ass");
        replies.add("underboob");
        return replies;
    }

}
