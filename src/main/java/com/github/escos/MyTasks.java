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
    private static final String SERIAL_NAME =
            "C:\\Users\\Роман\\Desktop\\levelup-java-18.11.16\\src\\main\\files\\serialTasks.txt";
    private static final String JSON_NAME =
            "C:\\Users\\Роман\\Desktop\\levelup-java-18.11.16\\src\\main\\files\\tasksJSON.txt";
    private static final String RESULT_NAME =
            "C:\\Users\\Роман\\Desktop\\levelup-java-18.11.16\\src\\main\\files\\result.txt";
    static Scanner sc = new Scanner(System.in);
    static SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    //перечисления команд управления списком
    private enum Commands {
        ADD,   // добавить задачу
        LIST,  // вывести полный список задач
        EDITS, // редактировать список задач в файле сериализации
        EDITJ, // редактировать список задач в файле JSON
        DELJ,  // удалить задачу в файле JSON
        DELS,  // удалить задачу в файле сериализации
        SAVE,  // сохранить добавленную задачу
        QUIT,  //выход
    }

    public static void main(String[] args) throws IOException, ParseException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = readFileGsonToString(JSON_NAME);
        List<Task> jsonTasks = parsefromJson(s, gson);
        System.out.println("Содержимое файла tasksJSON.txt:");
        printTaskList(jsonTasks);
        List<Task> serialTasks = readFromSerializeFile();
        System.out.println("Содержимое файла serialTasks.txt:");
        printTaskList(serialTasks);
        if (readFileToList(RESULT_NAME).get(0).equals("11")) System.out.println("Последней выполнялась сериализация");
        else System.out.println("Последним выполнялось сохранение в JSON");
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
                        checkTasksAndPrintResultList(jsonTasks, serialTasks);
                        break;
                    case EDITS:
                        System.out.println("Укажите номер задачи из файла serialize, которую нужно корректировать:");
                        int n = sc.nextByte();
                        if ((n > serialTasks.size()) || (n < 1)) {
                            System.out.println("Задачи с таким номером не существует!");
                        } else {
                            changeTask(serialTasks.get(n - 1));
                            serializeList((Serializable) serialTasks);
                        }
                        break;
                    case EDITJ:
                        System.out.println("Укажите номер задачи из файла JSON, которую нужно корректировать:");
                        n = sc.nextByte();
                        if ((n > jsonTasks.size()) || (n < 1)) {
                            System.out.println("Задачи с таким номером не существует!");
                        } else {
                            changeTask(jsonTasks.get(n - 1));
                            String str = saveToJson(gson, jsonTasks);
                            writeToFileGson(str, JSON_NAME);
                        }
                        break;
                    case DELJ:
                        System.out.println("Введите какой элемент файла tasksJSON.txt необходимо удалить");
                        int N = sc.nextByte();
                        if ((N > jsonTasks.size()) || (N < 1)) {
                            System.out.println("Задачи с таким номером не существует!");
                        } else {
                            jsonTasks.remove(N - 1);
                            String str = saveToJson(gson, jsonTasks);
                            writeToFileGson(str, JSON_NAME);
                        }
                        break;
                    case DELS:
                        System.out.println("Введите какой элемент файла serialTasks.txt необходимо удалить");
                        N = sc.nextByte();
                        if ((N > serialTasks.size()) || (N < 1)) {
                            System.out.println("Задачи с таким номером не существует!");
                        } else {
                            serialTasks.remove(N - 1);
                            serializeList((Serializable) serialTasks);
                        }
                        break;
                    case SAVE:
                        System.out.println("Выберите действие, выполняемое перед выходом из программы:" +
                                " 0 - сериализация, отличное от 0 число - сохранение в фомате JSON");
                        int j = sc.nextInt();
                        if (j == 0) {
                            serializeList((Serializable) serialTasks);
                            writeFlag("11");
                        } else {
                            String str = saveToJson(gson, jsonTasks);
                            writeToFileGson(str, JSON_NAME);
                            writeFlag("22");
                        }
                        flag = -1;
                        break;
                    case QUIT:
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
    private static void writeToFileGson(String tasks, String file) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file), StandardCharsets.UTF_8)) {
            writer.write(tasks);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write flag
    private static void writeFlag(String st) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(RESULT_NAME), StandardCharsets.UTF_8)) {
            writer.write(st);
            //writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // распечатка списка задач
    private static void printTaskList(List<Task> taskList) {
        System.out.println("Задач: " + taskList.size());
        for (int i = 0; i < taskList.size(); i++) {
            System.out.printf("Задача %2d: %-30s | дата выполнения: %s \n",
                    (i + 1), taskList.get(i).description, format1.format(taskList.get(i).date.getTime()));
        }
    }

    // чтение из файла gson в строку
    private static String readFileGsonToString(String FILENAME) {
        String s = "";
        try {
            List<String> str = Files.readAllLines(Paths.get(FILENAME), StandardCharsets.UTF_8);
            for (int i = 0; i < str.size(); i++) {
                s += str.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Collections.emptyList();
        }
        return s;
    }

    // читаем из файла в список строк
    private static List<String> readFileToList(String FILENAME) {
        try {
            return Files.readAllLines(Paths.get(FILENAME), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    //parsing задач из строки
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
    private static void serializeList(Serializable tasks) {
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

    //parsing из JSON
    private static List<Task> parsefromJson(String tasksGson, Gson gson) {
        List<Task> parsedTasks = gson.fromJson(tasksGson, new TypeToken<List<Task>>() {
        }.getType());
        return parsedTasks;
    }

    // сохранение в JSON
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
                    if (taskList2.get(i).date.equals(taskList2.get(j).date)) {
                        taskList2.remove(j);
                        break;
                    }
                    System.out.println("Обнаружено две задачи с названием: " + taskList2.get(i).description +
                            ", но с разными датами выполнения");
                    System.out.println("Выберите необходимую дату для данной задачи: " +
                            "при вводе 0 - дата: " + format1.format(taskList2.get(j).date.getTime()) + " , " +
                            "при вводе отличного от 0 числа- дата: " + format1.format(taskList2.get(i).date.getTime()));
                    if (sc.nextInt() == 0) taskList2.remove(i);
                    else taskList2.remove(j);
                }
            }
        }
        System.out.println("Задач всего: " + taskList2.size());
        for (int i = 0; i < taskList2.size(); i++) {
            System.out.printf("Задача %2d: %-30s | дата выполнения: %s \n",
                    (i + 1), taskList2.get(i).description, format1.format(taskList2.get(i).date.getTime()));
        }
        return taskList2;
    }

}

