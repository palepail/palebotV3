package bot;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by palepail on 7/31/2015.
 */
public class MessageManager {

    public static MessageManager messageManager = new MessageManager();


    static final int MAX_MESSAGES = 19;
    static int messageCount = MAX_MESSAGES;
    static Timer timer = new Timer();

    public static MessageManager getInstance(){
        return messageManager;
    }

    public static void startTimer(){
        timer.schedule(new addMessage(), 0, // initial delay
                666);

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
