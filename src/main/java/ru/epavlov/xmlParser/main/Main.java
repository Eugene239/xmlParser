package ru.epavlov.xmlParser.main;

import org.xml.sax.SAXException;

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
        Parser.getInstance().parseFile(new File("C:\\Users\\epavlov\\Projects\\xmlParser\\src\\main\\resources\\kp_b39c766e-1458-4658-a5c0-5b2dcb4ea495.xml"));
        Parser.getInstance().save(new File("C:\\Users\\epavlov\\Projects\\xmlParser\\src\\main\\resources\\out.xls"));
    }
}
