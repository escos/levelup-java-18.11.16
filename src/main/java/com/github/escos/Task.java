package com.github.escos;

import java.io.Serializable;
import java.util.*;
import java.util.Random;

public class Task implements Serializable {
    final static long serialVersionUID = 2670384911550037760L;
    String description;
    Calendar date;
    static Random gen = new Random();
    static Scanner sc = new Scanner(System.in);

    public Task(String description, Calendar date) {
        this.description = description;
        this.date = date;
    }

    public static Task createTask() {
        System.out.println("Введите название задачи: ");
        String description = sc.nextLine();
        Calendar date = new GregorianCalendar();
        while (true) {
            //System.out.println("Введите дату выполнения задачи в формате(dd.mm.yyyy hh:mm:ss): ");
            Calendar current = Calendar.getInstance();
            //для проверки работоспособности дата вводится автоматически
            date.set(Calendar.YEAR, 2016);
            date.set(Calendar.MONTH, 11);
            date.set(Calendar.DAY_OF_MONTH, gen.nextInt(23)+9);
            date.set(Calendar.HOUR, gen.nextInt(24));
            date.set(Calendar.MINUTE, gen.nextInt(59));
            if (date.after(current))
                break;
            else System.out.println("Ошибка, необходимо ввести дату выполнения задачи позже текущей");
        }
        return new Task(description, date);
    }

    //изменение параметров задачи
    public static Task changeTask(Task task) {
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
}