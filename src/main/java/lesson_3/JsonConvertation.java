package lesson_3;

import com.google.gson.Gson;

public class JsonConvertation {

    private Gson gson = new Gson();

    public Message parsefromJson(String jsonMessage) {
        return gson.fromJson(jsonMessage,Message.class);
    }

    public String saveToJson(Message message) {
        String messageJson = gson.toJson(message);
        return messageJson;
    }
}
