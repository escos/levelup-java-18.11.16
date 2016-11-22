package com.github.escos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.text.*;

public class MyTasks {
    private static final String SERIAL_NAME = "C:\\Users\\Роман\\Desktop\\levelup-java-18.11.16\\src\\main\\files\\tasks.txt";
    private static final String JSON_NAME = "C:\\Users\\Роман\\Desktop\\levelup-java-18.11.16\\src\\main\\files\\tasksJSON.txt";
    static Scanner sc = new Scanner(System.in);
    static SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    //перечисления команд управления списком
    private enum Commands {
        ADD,
        LIST,
        CHANGE,
        DEL,
        QUIT,
    }

    public static void main(String[] args) throws IOException, ParseException {
        List<Task> jsonTasks = readFromFile(readListFile(JSON_NAME));
        List<Task> serialTasks = readFromSerializeFile();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Содержимое файла сериализации:");
        printTaskList(serialTasks);
        System.out.println("Содержимое файла JSON:");
        printTaskList(jsonTasks);
        List<Task> taskList = checkTasksAndPrintResultList(jsonTasks, serialTasks);
        int flag = 0;
        boolean id = true;
        while (id) {
            System.out.println("Введите команду:");
            String command = sc.next();
            try {
                switch (Commands.valueOf(command)) {
                    case ADD:
                        Task task = Task.createTask();
                        jsonTasks.add(task);
                        serialTasks.add(task);
                        break;
                    case LIST:
                        //printTaskList(jsonTasks);
                        //printTaskList(serialTasks);
                        printTaskList(taskList);
                        break;
                    case CHANGE:
                        System.out.println("Укажите номер задачи из списка, которую нужно корректировать:");
                        int n = sc.nextByte();
                        if ((n > taskList.size()) || (n < 1)) {
                            System.out.println("Задачи с таким номером не существует!");
                        } else {
                            changeTask(taskList.get(n - 1));
                        }
                        break;
                    case DEL:
                        System.out.println("Введите какой элемент необходимо удалить");
                        int N = sc.nextByte();
                        if ((N > taskList.size()) || (N < 1)) {
                            System.out.println("Задачи с таким номером не существует!");
                        } else {
                            taskList.remove(N);
                        }
                        break;
                    case QUIT:
                        System.out.println("Выберите действие, выполняемое перед выходом из программы:" +
                                " 0 - сериализация, не 0 - сохранение в фомате JSON");
                        int j = sc.nextInt();
                        if (j == 0) {
                            write((Serializable) serialTasks);
                        } else {

                            writeToFile(parsefromJson(saveToJson(gson, jsonTasks), gson), JSON_NAME);
                        }
                        flag = -1;
                        break;
                }
                if (flag == -1) break;
            } catch (IllegalArgumentException ex) {
                System.out.println("Команда \"" + command + "\" введена неверно!");
                id = false;
            }
        }
    }

    // запись в файл
    private static void writeToFile(List<Task> taskList, String file) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file), StandardCharsets.UTF_8)) {
            for (Task task : taskList) {
                writer.write(task.description + " " + format1.format(task.date.getTime()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // распечатка списка задач
    public static void printTaskList(List<Task> taskList) {
        System.out.println("Задач: " + taskList.size());
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println("Задача " + i + ": " + taskList.get(i).description + " дата выполнения: " + format1.format(taskList.get(i).date.getTime()));
        }
    }

    // читаем из файла в список строк
    private static List<String> readListFile(String FILENAME) {
        try {
            return Files.readAllLines(Paths.get(FILENAME), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<Task> readFromFile(List<String> s) {
        if (s != null) {
            return toTasks(s);
        }
        return Collections.emptyList();
    }

    //parsing задач из файла
    private static Task parseDateAndDescription(String s) {
        String s1 = s.substring(s.length() - 16);
        String description = s.substring(0, s.length() - 16);
        Calendar cal;
        try {
            cal = Calendar.getInstance();
            cal.setTime(format1.parse(s1));
        } catch (ParseException e) {
            e.printStackTrace();
            cal = null;
        }
        return new Task(description, cal);
    }

    private static List<Task> toTasks(List<String> s) {
        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < s.size(); i++) {
            taskList.add(parseDateAndDescription(s.get(i)));
        }
        return taskList;
    }

    //изменение параметров задач
    private static Task changeTask(Task task) {
        System.out.println("Желаете изменить название задачи?");
        if (sc.nextBoolean()) {
            System.out.println("Введите новое название задачи");
            task.description = sc.next();
        }
        System.out.println("Желаете изменить дату выполнения задачи?");
        if (sc.nextBoolean()) {
            System.out.println("Введите новую дату выполнения задачи");
            Calendar date = new GregorianCalendar();
            date.set(Calendar.YEAR, sc.nextInt());
            date.set(Calendar.MONTH, sc.nextInt());
            date.set(Calendar.DAY_OF_MONTH, sc.nextInt());
            date.set(Calendar.HOUR, sc.nextInt());
            date.set(Calendar.MINUTE, sc.nextInt());
        }
        return task;
    }

    //десериализация
    private static List<Task> readFromSerializeFile() {
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
    private static void write(Serializable tasks) {
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

    //parse from JSON
    private static List<Task> parsefromJson(String tasksGson, Gson gson) {
        List<Task> parsedTasks = gson.fromJson(tasksGson, new TypeToken<List<Task>>() {
        }.getType());
        return parsedTasks;
    }

    // save to JSON
    private static String saveToJson(Gson gson, List<Task> taskList) {
        String tasksGson = gson.toJson(taskList);

        return tasksGson;
    }

    // проверка, есть ли совпадения с двух списках
    private static List<Task> checkTasksAndPrintResultList(List<Task> taskList1, List<Task> taskList2) {
        int N = taskList1.size();
        for (int i = 0; i < N; i++) {
            taskList2.add(taskList1.get(i));
        }
        for (int i = 0; i < taskList2.size() - 1; i++) {
            for (int j = i + 1; j < taskList2.size(); j++) {
                if (taskList2.get(i).description.equals(taskList2.get(j).description)) {
                    System.out.println("Обнаружено две задачи с названием: " + taskList2.get(i).description +
                            "но с разными датами выполнения");
                    System.out.println("Выберите нужную дату для данной задачи :" +
                            " 0 - оставляем первую дату , любое число кроме 0 - оставляем вторую дату");
                    if (sc.nextInt() == 0) taskList2.remove(i);
                    else taskList2.remove(j);
                }
            }
        }
        System.out.println("Задач всего: " + taskList2.size());
        for (int i = 0; i < taskList2.size(); i++) {
            System.out.println("Задача " + i + ": " + taskList2.get(i).description + " дата выполнения: "
                    + format1.format(taskList2.get(i).date.getTime()));
        }
        return taskList2;
    }
}

