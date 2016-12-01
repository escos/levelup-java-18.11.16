package lesson_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket socket;
    private SenderWorker senderWorker;
    private ServerExample server;
    private String userName;

    public ClientHandler(ServerExample server, Socket socket, String userName) {
        this.server = server;
        this.socket = socket;
        this.userName = userName;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            senderWorker = new SenderWorker(writer);
            senderWorker.start();
            String inputMessage;

            if ((inputMessage = reader.readLine()) != null) {
                System.out.println(inputMessage);
                Message message = JsonConvertation.getInstance().parsefromJson(inputMessage);
                userName = message.getSender();
            }
            while ((inputMessage = reader.readLine()) != null) {
                System.out.println(inputMessage);
                Message message = JsonConvertation.getInstance().parsefromJson(inputMessage);
                if (message.getReceiver().equals("all")) {
                    server.sendToAll(JsonConvertation.getInstance().saveToJson(message), this);
                }
                if (message.getReceiver().equals("server")) {
                    ArrayList<String> clients = server.clientList();
                    for (int i = 0; i < clients.size(); i++) {
                        String user = message.getSender();
                        message.setBody(clients.get(i));
                        System.out.println(JsonConvertation.getInstance().saveToJson(message));
                        server.sendToUser(JsonConvertation.getInstance().saveToJson(message), user);
                    }
                }
                server.sendToUser(JsonConvertation.getInstance().saveToJson(message), message.getReceiver());
            }
            server.disconnectClient(this);
            senderWorker.stopWorker();
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (senderWorker != null) {
            senderWorker.addMessage(message);
        }
    }

    public String getUserName() {
        return this.userName;
    }
}