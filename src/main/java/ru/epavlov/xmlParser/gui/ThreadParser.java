package ru.epavlov.xmlParser.gui;

import javafx.application.Platform;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import ru.epavlov.xmlParser.logic.Messages;
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
public class ThreadParser extends Thread  {
    private static final String TAG = "["+ ThreadParser.class.getSimpleName()+"]: ";
    private static final Logger log = Logger.getLogger(ThreadParser.class);
    private BehaviorSubject<String> status = BehaviorSubject.create();
    private Parser parser;
    private boolean done = false;
    public ThreadParser(){
        try {
            parser = Parser.getInstance();
        }catch ( Exception e) {
            status.onNext(Messages.FILE_CONFIG_ERROR);
            log.error(TAG+ e.toString());
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        super.run();
        ArrayList<File> list= Model.getInstance().getRxFiles().getValue();
        if (parser!=null && list!=null && list.size()>0) {
            status.onNext(Messages.START);
            for (File f : list) {
                try {
                    parser.parseFile(f, Model.getInstance().getArea());
                    Platform.runLater(() -> {
                        Model.getInstance().get(f.getName()).getStatus().onNext("Готов");
                    });
                    status.onNext(Messages.PARSE_FILE + f.getName());
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    Platform.runLater(() -> {
                        Model.getInstance().get(f.getName()).getStatus().onNext("Ошибка");
                    });
                    log.error(TAG+ e.toString());
                    // Model.getInstance().get(f.getName()).getStatus().onNext("Ошибка");
                    e.printStackTrace();
                }
            }
            status.onNext(Messages.PARSE_DONE);
        }

      done = true;
        //  this.stop();
    }

    public boolean isDone() {
        return done;
    }
//    private void complain(String text){
//
//    }
    public BehaviorSubject<String> getStatus() {
        return status;
    }


}