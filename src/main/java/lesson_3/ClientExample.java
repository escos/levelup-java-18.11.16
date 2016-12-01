package lesson_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientExample {
    public static final String IP = "127.0.0.1";
    public static final int PORT = 7071;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(IP, PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            ClientReceiver clientReceiver = new ClientReceiver(socket);
            clientReceiver.start();
            String inputConsole = "";
            String login = "";
            Message message = new Message(login, "login", null);
            if (!(inputConsole = console.readLine()).equals(null)) {
                if (inputConsole.contains(" ")) {
                    int pos = inputConsole.indexOf(" ");
                    message.setSender(inputConsole.substring(0, pos));
                } else message.setSender(inputConsole);
                writer.println(JsonConvertation.getInstance().saveToJson(message));
                writer.flush();
            }
            while (!(inputConsole = console.readLine()).equals("exit")) {
                if ((inputConsole.indexOf("@") == 0) && inputConsole.contains(":")) {
                    int pos = inputConsole.indexOf(":");
                    message.setReceiver(inputConsole.substring(1, pos));
                    message.setBody(inputConsole.substring(pos + 1));
                } else {
                    message.setBody(inputConsole);
                    message.setReceiver("all");
                }
                writer.println(JsonConvertation.getInstance().saveToJson(message));
                writer.flush();
            }
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
