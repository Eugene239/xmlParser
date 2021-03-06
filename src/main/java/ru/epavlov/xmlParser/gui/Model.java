package ru.epavlov.xmlParser.gui;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import ru.epavlov.xmlParser.logic.Parser;
import rx.subjects.BehaviorSubject;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 21.04.2017, 14:04
 */
public class Model {
    private static final String TAG = "["+ Model.class.getSimpleName()+"]: ";
    private static final Logger log = Logger.getLogger(Model.class);
    private BehaviorSubject<ArrayList<File>> rxFiles = BehaviorSubject.create();
    private float area;
    private static Model instance = new Model();
    private HashMap<String,ItemModel> itemMap = new HashMap<>();


    public static Model getInstance(){
        return instance;
    }

    public BehaviorSubject<ArrayList<File>> getRxFiles() {
        return rxFiles;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public void parseFiles() throws ParserConfigurationException, SAXException, IOException {
        ArrayList<File> list= rxFiles.getValue();
        Parser.getInstance().clear();
        for (int i = 0; i <list.size() ; i++) {
            Parser.getInstance().parseFile(list.get(i),area);
        }
    }
    public void saveFiles(File f){
        try {
            Parser.getInstance().save(f);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void addFiles(File f){
        ArrayList<File> list = new ArrayList<>();

        for (File f_:f.listFiles()) {
            if (f_.getName().contains(".xml")){
                list.add(f_);
            }
        }
        log.info("List size "+list.size());
        rxFiles.onNext(list);
    }

    public void addFiles(List<File> list){
        if (rxFiles.getValue()!=null){
            ArrayList<File> files= rxFiles.getValue();
            list.forEach(f->{
                if (!files.contains(f) && f.getName().contains(".xml")) files.add(f);
            });
            log.info("List size "+list.size());
            rxFiles.onNext(files);
        }else {
            ArrayList<File> files = new ArrayList<>();
            files.addAll(list);
            log.info("List size "+list.size());
            rxFiles.onNext(files);

        }
    }
    public void clear(){
        rxFiles.onNext(new ArrayList<>());
    }
    public void remove(String s){
        ArrayList<File> list = rxFiles.getValue();
        if (list!=null){
            for (int i = list.size()-1; i >=0 ; i--) {
                if (list.get(i).getName().equals(s)) list.remove(i);
            }

        }
        rxFiles.onNext(list);
    }
    public ItemModel get(String name){
        itemMap.computeIfAbsent(name, ItemModel::new);
        return  itemMap.get(name);
    }

    public float getArea() {
        return area;
    }
}