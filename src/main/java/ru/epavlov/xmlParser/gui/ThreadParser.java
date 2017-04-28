package ru.epavlov.xmlParser.gui;

import javafx.application.Platform;
import org.xml.sax.SAXException;
import ru.epavlov.xmlParser.logic.MacroRunner;
import ru.epavlov.xmlParser.logic.Parser;
import rx.subjects.BehaviorSubject;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 21.04.2017, 15:57
 */
public class ThreadParser extends Thread implements MacroRunner.RunnerListner {
    private BehaviorSubject<String> status = BehaviorSubject.create();
    private Parser parser;
    @Override
    public void run() {
        super.run();
        status.onNext("Запуск");
        ArrayList<File> list= Model.getInstance().getRxFiles().getValue();
        try {
            parser = Parser.getInstance();
        }catch ( Exception e) {
            status.onNext("Не найден файл конфигурации");
        }
        for (File f: list){
            try {
                parser.parseFile(f,Model.getInstance().getArea());
                Platform.runLater(()->{Model.getInstance().get(f.getName()).getStatus().onNext("Готов");});
                status.onNext("Парсинг "+f.getName());
            } catch (IOException | SAXException | ParserConfigurationException e) {
                Platform.runLater(()->{Model.getInstance().get(f.getName()).getStatus().onNext("Ошибка");});
                // Model.getInstance().get(f.getName()).getStatus().onNext("Ошибка");
                e.printStackTrace();
            }
        }

        try{
            if (parser!=null)  parser.save(new File(Parser.OUTPUT_FILE));
        } catch (IOException e) {
            status.onNext("Ошибка сохранения");
            e.printStackTrace();
        }

        try {
            status.onNext("Запуск макроса");
            MacroRunner.start(new File(Parser.OUTPUT_FILE),this);
        } catch (IOException e) {
            status.onNext("Ошибка запуска макроса");
            System.err.println("MacroRunner:: "+e.toString());
            //e.printStackTrace();
        }
        //  this.stop();
    }

    public BehaviorSubject<String> getStatus() {
        return status;
    }

    @Override
    public void onDone(String s) {
        // System.out.println(s+" "+s.length());
        switch (s){
            case "Error": status.onNext("Ошибка макроса"); break;
            case "Done": status.onNext("Готово"); break;
        }
        // System.out.println("thread:"+s);
    }
}