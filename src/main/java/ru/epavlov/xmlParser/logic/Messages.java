package ru.epavlov.xmlParser.logic;

;import javafx.scene.paint.Paint;

import java.util.HashMap;

public class Messages {
    public static final String AREA_ERROR = "Неверная площадь дома";
    public static final String FILE_CONFIG_ERROR = "Не найден файл конфигурации";
    public static final String START = "Запуск разбора файлов";
    public static final String PARSE_FILE = "Разбор файла ";
    public static final String PARSE_DONE = "Разбор файлов завершен";
    public static final String SAVE_ERROR = "При сохранении реестра возникла ошибка";
    public static final String START_MACROS = "Запущена подготовка реестра";
    public static final String MACROS_START_ERROR = "Во время запуска подготовки реестра возникла ошибка";
    public static final String MACROS_ERROR = "Во время подготовки реестра возникла ошибка";
    public static final String DONE = "Реестр и бюллетени успешно сохранены";

    public static HashMap<String,Paint> mesMap= new HashMap<>();
    static {
        mesMap.put(AREA_ERROR,Paint.valueOf("red"));
        mesMap.put(PARSE_DONE,Paint.valueOf("green"));
        mesMap.put(FILE_CONFIG_ERROR,Paint.valueOf("red"));
        mesMap.put(START,Paint.valueOf("green"));
        mesMap.put(PARSE_FILE,Paint.valueOf("green"));
        mesMap.put(SAVE_ERROR,Paint.valueOf("red"));
       // mesMap.put(START_PARSER,Paint.valueOf("green"));
        mesMap.put(START_MACROS,Paint.valueOf("green"));
        mesMap.put(MACROS_START_ERROR,Paint.valueOf("red"));
        mesMap.put(MACROS_ERROR,Paint.valueOf("red"));
        mesMap.put(DONE,Paint.valueOf("green"));
    }
}
