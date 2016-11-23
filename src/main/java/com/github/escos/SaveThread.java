package com.github.escos;

import java.util.List;

public class SaveThread extends Thread {

    @Override
    public void run() {
        Serialization serialization = new Serialization();
        List<Task> tasks = serialization.readFromSerializeFile();
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            serialization.serializeTask(tasks.get(i),
                           "C:\\Users\\Роман\\Desktop\\levelup-java-18.11.16\\src\\main\\tasks\\" + i + "_task.txt");
            count += 100/tasks.size();
            if (i == tasks.size()-1) System.out.printf("\r % 5d %%   ", 100);
            else System.out.printf("\r % 5d %%   ", count);

            try {
                SaveThread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
