package managers;

import dao.WaifuDAO;
import dto.WaifuDTO;
import models.Waifu;
import models.WaifuThirst;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by palepail on 7/31/2015.
 */
public class WaifuManager {

    static WaifuManager waifuManager = new WaifuManager();
   static WaifuDAO waifuDAO = new WaifuDAO();

    public static  WaifuManager getInstance(){return waifuManager;};

    public List<WaifuDTO> getWaifuDTOByChannel(int channelId)
    {
        return createWaifuDTOs(waifuDAO.getWaifuByChannel(channelId));
    }

    public List<WaifuDTO> getAllDTO() {

        return createWaifuDTOs(waifuDAO.getAll());
    }

    public void updateWaifuThirst(WaifuThirst thirst)
    {
        waifuDAO.updateWaifuThirst(thirst);
    }

    public WaifuThirst getThirst(String userName, int channelId)
    {
       return waifuDAO.getThirst(userName, channelId);
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

    public List<Waifu> getBest(int channelId) {
        return waifuDAO.getBest(channelId);
    }

    public List<Waifu> getThirstiest(int channelId) {
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

    public Waifu getRandomFromChannel(int channelId) {
        return waifuDAO.getRandomFromChannel(channelId);
    }

    public Waifu addWaifu(Waifu waifu) {
        return waifuDAO.addWaifu(waifu);
    }

    public Waifu updateWaifu(Waifu waifu) {
        return waifuDAO.updateWaifu(waifu);
    }

    public boolean deleteWaifuByLink(String link) {
        return waifuDAO.deleteWaifuByLink(link);
    }

    public boolean deleteWaifuByLinkFromChannel(String link, int channelId) {
        return waifuDAO.deleteWaifuByLinkFromChannel(link, channelId);
    }


    private List<WaifuDTO> createWaifuDTOs(List<Waifu> waifus) {

        List<WaifuDTO> dtos = new ArrayList<>();
        for (Waifu waifu : waifus) {
            WaifuDTO dto = new WaifuDTO();
            dto.setName(waifu.getName());
            dto.setId(waifu.getId());
            dto.setLink(waifu.getLink());
            dtos.add(dto);
        }
        return dtos;
    }


}
