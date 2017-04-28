package ru.epavlov.xmlParser.gui;

import org.apache.log4j.Logger;
import ru.epavlov.xmlParser.logic.MacroRunner;
import ru.epavlov.xmlParser.logic.Parser;
import rx.subjects.BehaviorSubject;

import java.io.File;
import java.io.IOException;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 28.04.2017, 15:35
 */
public class ThreadSaver extends Thread implements MacroRunner.RunnerListner{
    private static final String TAG = "["+ ThreadSaver.class.getSimpleName()+"]: ";
    private static final Logger log = Logger.getLogger(ThreadSaver.class);
    private BehaviorSubject<String> status = BehaviorSubject.create();
    private Parser parser;

    public ThreadSaver(){
        try {
            parser = Parser.getInstance();
        }catch ( Exception e) {
            status.onNext("Не найден файл конфигурации");
            log.error(TAG+ e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        try{
            if (parser!=null)  parser.save(new File(Parser.OUTPUT_FILE));
        } catch (IOException e) {
            status.onNext("Ошибка сохранения");
            log.error(TAG+ e.toString());
            e.printStackTrace();
        }
        try {
            status.onNext("Запуск макроса");
            MacroRunner.start(new File(Parser.OUTPUT_FILE),this);
        } catch (IOException e) {
            status.onNext("Ошибка запуска макроса");
            //System.err.println("MacroRunner:: "+e.toString());
            log.error(TAG+ e.toString());

        }
    }

    @Override
    public void onDone(String s) {
        switch (s){
            case "Error": status.onNext("Ошибка макроса"); break;
            case "Done": status.onNext("Готово"); break;
        }
    }
    public BehaviorSubject<String> getStatus() {
        return status;
    }
}
