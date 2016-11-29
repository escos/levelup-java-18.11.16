package lesson_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private SenderWorker senderWorker;
    private ServerExample server;
    public String name;

    public ClientHandler(ServerExample server, Socket socket, String name) {
        this.server = server;
        this.socket = socket;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            JsonConvertation jsonConvertation = new JsonConvertation();
            String inputMessage = reader.readLine();
            Message message = jsonConvertation.parsefromJson(inputMessage);
            if (inputMessage!=null) {
                name = message.getUserName();
            }
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            senderWorker = new SenderWorker(writer);
            senderWorker.start();
            while (inputMessage != null) {
                System.out.println(message.getBody());
                String userName  = message.getUserName();
                ClientHandler receiver = new ClientHandler(server,socket, userName);
                if (userName!=null) server.sendToUser(message.getBody(), receiver);
                else server.sendToAll(message.getBody(), this);
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
}
