package lesson_3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerExample {
    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    public static void main(String[] args) {
        new ServerExample().start();
    }

    private void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(7071);
            System.out.println("Server started...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                String userName = "";
                ClientHandler clientHandler = new ClientHandler(this, clientSocket, userName);
                clientHandler.start();
                clients.add(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> clientList() {
        ArrayList<String> contacts = new ArrayList<String>();
        for (int i = 0; i < clients.size(); i++) {
            contacts.add(clients.get(i).getUserName());
        }
        return contacts;
    }

    public void sendToAll(final String message, final ClientHandler sender) {
        new Thread(new Runnable() {
            public void run() {
                for (ClientHandler c : clients) {
                    if (c != sender) {
                        c.sendMessage(message);
                    }
                }
            }
        }).start();

    }

    public void sendToUser(final String message, final String userName) {
        new Thread(new Runnable() {
            public void run() {
                for (ClientHandler c : clients) {
                    if (c.getUserName().equals(userName)) {
                        c.sendMessage(message);
                    }
                }
            }
        }).start();
    }

    public void disconnectClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}