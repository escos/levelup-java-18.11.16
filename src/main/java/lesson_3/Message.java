package lesson_3;

public class Message {
    private String body;
    private String userName;

    Message(String userName, String body){
        this.body = body;
        this.userName = userName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }
}
