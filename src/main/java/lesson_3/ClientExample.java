package lesson_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class ClientExample {
    public static final String IP = "127.0.0.1";
    public static final int PORT = 7071;
    public static Scanner sc =new Scanner(System.in);

    public static void main(String[] args) {
        new ClientExample().start();
    }

    private void start() {
        try {
            Socket socket = new Socket(IP, PORT);
            System.out.println("Enter login");
            String userName = "@"+sc.next();
            ClientGetter clientGetter = new ClientGetter(socket, this, userName);
            clientGetter.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
