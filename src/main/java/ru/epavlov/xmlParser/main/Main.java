package ru.epavlov.xmlParser.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    public static void main(String[] args) throws IOException {
        XMLHelper xmlHelper = new XMLHelper();
        File f= xmlHelper.getFileList().get(0);
        String s="";
        for(String str: Files.readAllLines(f.toPath())){
            s+=str+"\n";
        }
        xmlHelper.indent(s);
    }
}

/*
         xmlHelper.getFileList().stream().forEach(file -> {
            final String[] s = {""};
            try {
                Files.readAllLines(file.toPath()).forEach(str->{
                    s[0] +=str+"\n";
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(file.getName());
        });
 */