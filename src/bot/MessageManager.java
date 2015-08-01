package bot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by palepail on 7/31/2015.
 */
public class MessageManager {

    public static MessageManager messageManager = new MessageManager();


    static final int MAX_MESSAGES = 19;
    static ArrayList<String> lockArray = new ArrayList();
    static int messageCount = MAX_MESSAGES;
    static Timer timer = new Timer();

    public static MessageManager getInstance(){
        return messageManager;
    }
    public static void delayMessage(int time){
        try {
            Thread.sleep(time);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

    }
    public static boolean lock(String id, int time){
        if(lockArray.contains(id))
        {
            return false;
        }
        lockAction(id, time);
        return true;
    }

    public static void startTimer(){
        timer.schedule(new addMessage(), 0, // initial delay
                666);

    }


    public static void lockAction(String id, int time){
        lockArray.add(id);
        timer.schedule(new unlockAction(id), time);

    }

    static class unlockAction extends TimerTask {
        private String id;
        unlockAction(String id){
            this.id = id;
        }
        public void  run(){
            lockArray.remove(id);

        }
    }

    public static void stopTimer(){
        timer.cancel();

    }

    static class addMessage extends TimerTask {
        public void  run(){
            if(messageCount<19) {
                messageCount++;
            }
        }
    }



    public boolean overLimit(){
        if(messageCount<0)
        {
            return true;
        }else {
            return false;
        }

    }

    public void reduceMessages(int num)
    {
        messageCount -= num;
    }



}
