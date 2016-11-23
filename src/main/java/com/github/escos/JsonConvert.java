package com.github.escos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class JsonConvert {
    public static final String JSON_NAME =
            "src\\main\\files\\tasksJSON.txt";

    // чтение из файла json в строку
    public  String readFileJsonToString() {
        String s = "";
        try {
            List<String> str = Files.readAllLines(Paths.get(JSON_NAME), StandardCharsets.UTF_8);
            for (int i = 0; i < str.size(); i++) {
                s += str.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Collections.emptyList();
        }
        return s;
    }

    // запись в файл json
    public void writeToFileGson(String tasks) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(JSON_NAME), StandardCharsets.UTF_8)) {
            writer.write(tasks);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //parsing из JSON
    public List<Task> parsefromJson(String tasksGson, Gson gson) {
        List<Task> parsedTasks = gson.fromJson(tasksGson, new TypeToken<List<Task>>() {
        }.getType());
        return parsedTasks;
    }

    // сохранение в JSON
    public String saveToJson(Gson gson, List<Task> taskList) {
        String tasksGson = gson.toJson(taskList);
        return tasksGson;
    }
}
