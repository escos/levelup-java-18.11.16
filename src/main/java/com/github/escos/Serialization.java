package com.github.escos;

import java.io.*;
import java.util.List;

public class Serialization {
    public static final String SERIAL_NAME =
            "C:\\Users\\Роман\\Desktop\\levelup-java-18.11.16\\src\\main\\files\\serialTasks.txt";

    //десериализация
    public List<Task> readFromSerializeFile() {
        FileInputStream fis = null;
        List<Task> tasks = null;
        try {
            fis = new FileInputStream(SERIAL_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tasks = (List<Task>) ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    //сериализация
    public void serializeList(Serializable tasks) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(SERIAL_NAME);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tasks);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
