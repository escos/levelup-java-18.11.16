package com.github.escos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

class MyFileCounter implements FileFilter {
    private static final String RESULT_NAME = "src\\main\\files\\result.txt";

    public boolean accept(File pathname) {
        return pathname.isFile() && pathname.getName().endsWith(".txt");
    }
    // подсчет количества файлов в папке
    public int counter() {
        File f = new File("C:\\Users\\Роман\\Desktop\\levelup-java-18.11.16\\src\\main\\tasks");
        MyFileCounter filter = new MyFileCounter();
        File[] list = f.listFiles(filter);
        return list.length;
    }

    // запись в файл строки
    public void writeFlag(String st) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(RESULT_NAME), StandardCharsets.UTF_8)) {
            writer.write(st);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // читаем из файла в список строк
    public List<String> readFileToList() {
        try {
            return Files.readAllLines(Paths.get(RESULT_NAME), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
