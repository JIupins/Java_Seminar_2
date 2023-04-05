package hw1.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class App {

    public static void main(String[] args) throws Exception {

        String resultText = "ResultText.txt";
        String mainText = "MainText.json";
        String text = "{{\"фамилия\":\"Иванов\",\"оценка\":\"5\",\"предмет\":\"Математика\"}," +
                "{\"фамилия\":\"Петрова\",\"оценка\":\"4\",\"предмет\":\"Информатика\"}," +
                "{\"фамилия\":\"Краснов\",\"оценка\":\"5\",\"предмет\":\"Физика\"}}";

        Logger loger = Logger.getLogger(App.class.getName());
        loggerPropertis(loger);

        String mainFile = textParsing(text, loger);
        fileCreater(mainFile, resultText, loger);

        String resultFile = fileConverter(loger, resultText);
        fileCreater(resultFile, mainText, loger);
    }

    public static void loggerPropertis(Logger loger) {
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("log.txt");
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            loger.addHandler(fileHandler);
            loger.log(Level.INFO, "Файл log.txt успешно создан.");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Парсинг исходной json строки
    static String textParsing(String text, Logger log) {
        text = text.replaceAll("[{\"\"]", "");

        String[] students = new String[0];
        String newData = "";
        try {
            students = text.split("},");
            log.log(Level.INFO, "Строка успешно разделена.");
            System.out.println();
            for (String note : students) {
                String[] temp = note.split(",");
                String[] name = temp[0].split(":");
                String[] ball = temp[1].split(":");
                String[] lesson = temp[2].split(":");
                lesson[1] = lesson[1].replace("}", "");

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Студент ");
                stringBuilder.append(name[1]);
                stringBuilder.append(" получил ");
                stringBuilder.append(ball[1]);
                stringBuilder.append(" по предмету ");
                stringBuilder.append(lesson[1]);
                System.out.println(stringBuilder.toString());

                newData += String.format("%s\n", stringBuilder.toString());
            }
            System.out.println();
            log.log(Level.INFO, "Данные из строки успешно получены.");
            return newData;
        } catch (Exception e) {
            e.printStackTrace();
            log.log(Level.WARNING, e.getMessage(), "Даные из строки не получены.");
            return newData;
        }
    }

    // Создание текстовых файдов (*.txt, *.json)
    public static void fileCreater(String txt, String name, Logger log) {
        try (FileWriter fileWr = new FileWriter(new File(name))) {
            fileWr.write(txt);
            log.log(Level.INFO, String.format("Файл %s успешно создан.", name));
        } catch (Exception e) {
            log.log(Level.WARNING, "Ошибка создания файла!");
            System.out.println(e.getMessage());
        }
    }

    public static String fileConverter(Logger log, String name) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(name));
        String str;
        StringBuilder res = new StringBuilder();
        while ((str = br.readLine()) != null) {
            res.append(str + "\n");
        }

        // Удаление лишних символов
        String data = res.toString();
        data = data.replaceAll("Студент ", "").replace(" получил ", " ").replace(" по предмету ", " ");

        // Формирование массива для преобразование в исходную строку
        String[] arr1 = data.split("\n");
        String[][] arr2 = new String[arr1.length][];
        for (int i = 0; i < arr1.length; i++) {
            arr2[i] = arr1[i].split(" ");
        }

        // Формирование исходной строки
        String result = "";
        for (int i = 0; i < arr2.length; i++) {
            result += "{\"фамилия\":\"" + arr2[i][0] + "\",\"оценка\":\"" + arr2[i][1] + "\",\"предмет\":\""
                    + arr2[i][2] + "\"}";
            if (i != 2) {
                result += ",";
            } else {
                result = "{" + result + "}";
            }
        }

        br.close();
        log.log(Level.INFO, "Исходная строка успешно сформирована");
        return result;
    }
}