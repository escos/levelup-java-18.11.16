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
            JsonConvertation jsonConvertation = new JsonConvertation();
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!serverReader.readLine().equals(null)) {
                Message inputMessage = jsonConvertation.parsefromJson(serverReader.readLine());
                System.out.println(inputMessage.getSender()+"  "+inputMessage.getBody());
                //System.out.println(serverReader.readLine());
            }
            serverReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
