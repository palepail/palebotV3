package managers;

import dao.WaifuDAO;
import dto.WaifuDTO;
import models.Waifu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by palepail on 7/31/2015.
 */
public class WaifuManager {
    WaifuDAO waifuDAO = new WaifuDAO();

    public List<WaifuDTO> getAllDTO() {

        return createWaifuDTOs(waifuDAO.getAll());
    }
    public List<Waifu> getAll() {
        return waifuDAO.getAll();
    }

    public Waifu getRandom(){
        return waifuDAO.getRandom();
    }

    public Waifu addWaifu(Waifu waifu) {
        return waifuDAO.addWaifu(waifu);
    }
    private List<WaifuDTO> createWaifuDTOs(List<Waifu> waifus) {

        List<WaifuDTO> dtos = new ArrayList<>();
        for(Waifu waifu : waifus)
        {
            WaifuDTO dto= new WaifuDTO();
            dto.setName(waifu.getName());
            dto.setId(waifu.getId());
            dto.setLink(waifu.getLink());
            dtos.add(dto);
        }
        return dtos;
    }


}
