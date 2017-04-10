package ru.epavlov.xmlParser.main;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 05.04.2017, 12:14
 */
public class Main {
    public static void main(String[] args) throws IOException, SAXException {
        checkList();
       //checkFile(new File("C:\\Users\\epavlov\\Desktop\\xmlEugene\\kp_1831b7d7-38fd-4bd2-8e95-e527ddf58318.xml"));
    }
    public static void checkList() throws IOException, SAXException {
        File f =new File("C:\\Users\\epavlov\\Desktop\\xmlEugene");

            for (File f_ : f.listFiles()) {
                try {
                Parser.getInstance().parseFile(f_);
                Parser.getInstance().save(f_);
                } catch (Exception e){
                    System.out.println(f_.getName());
                    e.printStackTrace();
                }
            }

    }
    public static void checkFile(File f) throws IOException, SAXException, ParserConfigurationException {
        Parser.getInstance().parseFile(f);
        Parser.getInstance().save(f);
    }

}
