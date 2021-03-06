package managers;

import dao.WaifuDAO;
import dto.WaifuDTO;
import models.Waifu;
import models.WaifuRank;
import models.WaifuThirst;

import java.util.*;

/**
 * Created by palepail on 7/31/2015.
 */
public class WaifuManager {

    static WaifuManager waifuManager = new WaifuManager();
   static WaifuDAO waifuDAO = new WaifuDAO();
    static Boolean lock = false;

    public static boolean isLocked(){
        return lock;
    }

    public static void setLocked(Boolean set){
        lock = set;
    }

    public static  WaifuManager getInstance(){return waifuManager;};

    public List<WaifuDTO> getWaifuDTOByChannel(int channelId)
    {
        return createWaifuDTOs(waifuDAO.getWaifuByChannel(channelId));
    }

    public List<WaifuDTO> getAllDTO() {

        return createWaifuDTOs(waifuDAO.getAll());
    }

    public void updateWaifuThirst(WaifuThirst thirst, int amount)
    {
        waifuDAO.updateWaifuThirst(thirst, amount);
    }

    public WaifuThirst getThirst(String userName, int channelId)
    {
       return waifuDAO.getThirst(userName, channelId);
    }
    public List<Waifu> getWaifuByLink(String link, int id)
    {
        return waifuDAO.getWaifuByLink(link, id);
    }

    public List<Waifu> getAll() {
        return waifuDAO.getAll();
    }

    public List<Waifu> getWaifuByName(String name) {
        return waifuDAO.getWaifuByName(name);
    }

    public List<Waifu> getWaifuByNameFromChannel(String name, int channelId) {
        return waifuDAO.getWaifuByNameFromChannel(name, channelId);
    }

    public List<Waifu> getWaifuFromChannel(String query, int channelId) {
        List<Waifu> waifu = new ArrayList<Waifu>();
        List<Waifu> waifuByName = waifuDAO.getWaifuByNameFromChannel(query, channelId);
        List<Waifu> waifuByLink = waifuDAO.getWaifuByLink(query, channelId);
        if(waifuByLink != null){
            waifu.addAll(waifuByLink);
        }
        if(waifuByName != null) {
            waifu.addAll(waifuByName);
        }
        return waifu;
    }

    public WaifuRank addRank(WaifuRank rank){
        return waifuDAO.addRank(rank);
    }

    public WaifuRank getRank(int channelId, int tier)
    {
        return waifuDAO.getRandomRankFromChannel(channelId, tier);
    }

    public List<Waifu> getBest(int channelId) {
        return waifuDAO.getBest(channelId);
    }

    public List<WaifuThirst> getThirstiest(int channelId) {
        return waifuDAO.getThirstiest(channelId);
    }

    public List<Waifu> getWorst(int channelId) {
        return waifuDAO.getWorst(channelId);
    }

    public void resetFight(int channelId) {
        waifuDAO.resetFight(channelId);
    }

    public Waifu getRandom() {
        return waifuDAO.getRandom();
    }


    public Waifu getRandomFromChannel(int channelId, String rarity) {
        List<Waifu> waifu = waifuDAO.getWaifuByChannel(channelId);

        int top3p = (int) (waifu.size()-(waifu.size()*.03));
        int top10p = (int) (waifu.size()-(waifu.size()*.1));
        int top30p = (int) (waifu.size()-(waifu.size()*.3));
        int top50p = (int) (waifu.size()-(waifu.size()*.5));

        switch(rarity)
        {
            case "SSR": waifu = waifu.subList(top3p,waifu.size());
                break;
            case "SR": waifu = waifu.subList(top10p,top3p);
                break;
            case "R": waifu = waifu.subList(top30p,top10p);
                break;
            case "U": waifu = waifu.subList(top50p,top30p);
                break;
            case "C": waifu = waifu.subList(0,top50p);
        }
        Collections.shuffle(waifu, new Random(System.nanoTime()));

        return waifu.get(0);
    }

    public String findWaifuRarity(int waifuId,int channelId)
    {
        Waifu waifu = waifuDAO.getWaifuById(waifuId);
        List<Waifu> list = waifuDAO.getWaifuByChannel(channelId);
        int position = list.indexOf(waifu);

        return findRarity(position,list.size());

    }

    public String findRarity(int position, int total){
        int top3p = (int) (total-(total*.03));
        int top10p = (int) (total-(total*.1));
        int top30p = (int) (total-(total*.3));
        int top50p = (int) (total-(total*.5));

        String rarity;
        if(position<top50p)
        {
            rarity = "C";

        }else if (position < top30p)
        {
            rarity = "U";
        }else if (position < top10p)
        {
            rarity = "R";
        }else if (position < top3p){
            rarity = "SR";
        }else{
            rarity = "SSR";
        }
        return rarity;
    }

    public Waifu addWaifu(Waifu waifu) {
        return waifuDAO.addWaifu(waifu);
    }

    public Waifu updateWaifu(Waifu waifu) {
        return waifuDAO.updateWaifu(waifu);
    }

    public boolean deleteWaifuByLink(String link, int id) {
        return waifuDAO.deleteWaifuByLink(link, id);
    }
    public boolean deleteWaifuByLinkFromChannel(String link, int channelId) {
        return waifuDAO.deleteWaifuByLinkFromChannel(link, channelId);
    }

    public boolean deleteWaifuById(int id){
        return waifuDAO.deleteWaifu(id);
    }

    public  List<WaifuDTO> saveWaifuByChannel(int channelId, List<WaifuDTO> newWaifu){
        List<Waifu> currentWaifu = waifuDAO.getWaifuByChannel(channelId);
        List<Integer> newWaifuIds = new ArrayList<>();


        for(WaifuDTO waifuToSave : newWaifu){
            Waifu waifuToUpdate = waifuDAO.getWaifuById(waifuToSave.getId());
            if(waifuToUpdate==null){
                waifuToUpdate = new Waifu();
                waifuToUpdate.setChannelId(channelId);
            }
            waifuToUpdate.setUploader(waifuToSave.getUploader());
            waifuToUpdate.setLink(waifuToSave.getLink());
            waifuToUpdate.setName(waifuToSave.getName());
            waifuDAO.updateWaifu(waifuToUpdate);
            newWaifuIds.add(waifuToSave.getId());

        }
        for(Waifu oldWaifu : currentWaifu)
        {
            if(!newWaifuIds.contains(oldWaifu.getId())){
                waifuDAO.deleteWaifu(oldWaifu.getId());
            }
        }

        return getWaifuDTOByChannel(channelId);

    }

    private List<WaifuDTO> createWaifuDTOs(List<Waifu> waifus) {

        List<WaifuDTO> dtos = new ArrayList<>();
        for (Waifu waifu : waifus) {
            WaifuDTO dto = new WaifuDTO();
            dto.setName(waifu.getName());
            dto.setId(waifu.getId());
            dto.setPoints(waifu.getPoints());
            dto.setClaimed(waifu.getClaimed());
            dto.setLink(waifu.getLink());
            dto.setUploader(waifu.getUploader());
            dtos.add(dto);
        }
        return dtos;
    }
    public Waifu getClaimed(int channelId,String userName) {
        List<Waifu> list = waifuDAO.getWaifuByClaimed(channelId,userName);

        if(list.size()>0) {
            return list.get(0);
        }else {
            return null;
        }

    }


    public void removeClaimed(int channelId,String userName) {
        List<Waifu> list = waifuDAO.getWaifuByClaimed(channelId,userName);

        for (Waifu waifu: list){
            waifu.setClaimed(null);
            waifuDAO.updateWaifu(waifu);
        }
    }
}
