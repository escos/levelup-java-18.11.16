package lesson_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientReceiver extends Thread {
    private Socket socket;

    ClientReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!serverReader.readLine().equals(null)) {
                Message inputMessage = JsonConvertation.getInstance().parsefromJson(serverReader.readLine());
                if (inputMessage.getReceiver().equals("server")) {
                    System.out.println(" User " + inputMessage.getBody());
                } else
                    System.out.println("Sender: " + inputMessage.getSender() + " Message: " + inputMessage.getBody());
            }
            serverReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
