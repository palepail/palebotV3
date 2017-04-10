package bot;

import java.util.Random;

/**
 * Created by palepail on 4/9/2017.
 */
public class RNGManager {
    private static RNGManager RNGManager = new RNGManager();
    private static Random rand = new Random(System.nanoTime());

    public static RNGManager getInstance() {
        return RNGManager;
    }

    public int getRandom(int min, int max)
    {

        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public String getRarity(){
        int num = getRandom(0,1000);
        String rarity;
       if(num>970)
       {
            rarity = "SSR";
       }else if (num > 900)
       {
           rarity = "SR";
       }else if (num > 750)
       {
           rarity = "R";
       }else if (num > 500){
           rarity = "U";
       }else{
           rarity = "C";
       }
        return rarity;
    }



}
