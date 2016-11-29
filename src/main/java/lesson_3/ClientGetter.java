package lesson_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientGetter extends Thread {
    private ClientExample client;
    private Socket socket;
    private String userName;

    ClientGetter(Socket socket, ClientExample client, String userName){
        this.socket = socket;
        this.client = client;
        this.userName = userName;
    }

    @Override
    public void run(){
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputConsole;
            JsonConvertation json = new JsonConvertation();
            Message message = new Message(userName, "");
            writer.println(json.saveToJson(message));
            writer.flush();
            while (!(inputConsole=console.readLine()).equals("exit")) {
                int pos = inputConsole.indexOf(":");
                message.setUserName(inputConsole.substring(2,pos));
                message.setBody(inputConsole.substring(pos,inputConsole.length()));
                writer.println(json.saveToJson(message));
                writer.flush();
                System.out.println(serverReader.readLine());
            }
            writer.close();
            serverReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
