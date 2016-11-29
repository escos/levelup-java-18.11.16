package lesson_3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerExample {
    private ArrayList<ClientHandler> clients = new ArrayList<>();

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
//            serverSocket.close();
//            System.out.println("Server closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void sendToUser(final String message, final ClientHandler receiver) {
        new Thread(new Runnable() {
            public void run() {
                receiver.sendMessage(message);
            }
        }).start();
    }

    public void disconnectClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    /*
    1) В клиенте получение сообщений должно быть в фоновом потоке
    2) Подумать над MSW
    3) Первое сообщение от клиента - его Username
    4) Отправка сообщений в личку:
        Пользователь вводит "Hello" -> отправка всем
        Пользователь вводит "@username: Hello" -> отправка Hello для username
        Отправка сообщений в формате Json:
            {
                "receiver": "username",  (если null, то всем)
                "body": "Hello"
            }
     */
}
