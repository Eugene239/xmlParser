package ru.epavlov.xmlParser.logic;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        Path path = Paths.get("");
        System.out.println(path.toFile().getAbsoluteFile());
       // checkList();
     //  checkFile(new File("C:\\Users\\epavlov\\Desktop\\xmlEugene\\kp_b39c766e-1458-4658-a5c0-5b2dcb4ea495.xml"));
    }
    public static void checkList() throws IOException, SAXException {
        File f =new File("F:\\xml\\");
        try {
            for (File f_ : f.listFiles()) {
                if (f_.getName().contains(".xml"))
                Parser.getInstance().parseFile(f_,500.4f);
            }
        Parser.getInstance().save(new File(Parser.OUTPUT_FILE));
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void checkFile(File f) throws IOException, SAXException, ParserConfigurationException {
        Parser.getInstance().parseFile(f,44.f);
        Parser.getInstance().save(new File("C:\\Users\\epavlov\\Projects\\xmlParser\\src\\main\\resources\\"+f.getName()+".xls"));
    }

}
