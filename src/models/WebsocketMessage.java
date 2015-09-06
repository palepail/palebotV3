package models;

/**
 * Created by palepail on 9/6/2015.
 */
public class WebsocketMessage {
    String messageType;
    Object message;

   public WebsocketMessage(String messageType, Object message){
        this.messageType = messageType;
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
