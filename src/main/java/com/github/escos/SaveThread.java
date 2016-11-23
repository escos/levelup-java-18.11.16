package com.github.escos;

import java.util.List;

public class SaveThread extends Thread {

    @Override
    public void run() {
        Serialization serialization = new Serialization();
        List<Task> tasks = serialization.readFromSerializeFile();
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            serialization.serializeTask(tasks.get(i), "files\\" + i + "_task.txt");
            count++;
            System.out.printf("Выполнение: %d\r", count);
            try {
                SaveThread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
