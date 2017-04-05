package ru.epavlov.xmlParser.main;

import org.apache.commons.lang3.CharEncoding;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 05.04.2017, 12:15
 */
public class XMLHelper {
    private static final Logger log = Logger.getLogger(XMLHelper.class);
    public static final String dir ="C:\\Users\\epavlov\\Desktop\\xmlEugene";
    public ArrayList<File> getFileList(){
        ArrayList<File> list = new ArrayList<File>();
        for(File f_ : new File(dir).listFiles()){
            list.add(f_);
            //System.out.println(f_.getName());
        }
        return list;
    }
    /**
     * Приводим файл в красивй вид
     * @param src текст
     * @return красивый текст
     */
    public  String indent(String src) {
        //System.out.println(src);
        if (src == null || src.trim().equals("")) return "";
        String s;
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(CharEncoding.UTF_8);
        try {
            Document document = DocumentHelper.parseText(src);
            StringWriter sw = new StringWriter();
            XMLWriter writer = new XMLWriter(sw, format);
            writer.write(document);
            s = sw.toString();
            s = new String(s.getBytes(), CharEncoding.UTF_8);
            sw.flush();
            sw.close();
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName().concat("::  " + e.getMessage()));
            return "";
        }
        return s;
    }


}
