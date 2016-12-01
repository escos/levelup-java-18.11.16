package lesson_3;

public class Message {

    private String sender;
    private String receiver;
    private String body;

    Message(String sender,String receiver, String body){
        this.body = body;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender){
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver){
        this.receiver = receiver;
    }
}