
package ru.epavlov.xmlParser.logic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 19.04.2017, 15:02
 */
public class MacroRunner {
    public interface RunnerListner{
        void onDone(String s);
    }
    public static final String PATH  = System.getProperty("user.dir")+"\\MacrosRunner.exe";
    private static RunnerListner listner;
    public static void start(File f, RunnerListner listner) throws IOException {
        MacroRunner.listner = listner;
        System.out.println(PATH+ " \""+ f.getAbsolutePath() +"\"");
        ProcessBuilder pb = new ProcessBuilder(PATH,"\""+ f.getAbsolutePath() +"\"");
        pb.redirectErrorStream(true);
        Process p = pb.start();
        readWithIS(p);
    }
    private static void readWithIS(Process p) throws IOException {
        InputStream is  = p.getInputStream();
        int val;
        String s="";
        while (p.isAlive()) {
            while ((val = is.read()) != -1) {
                if ((val >='a' && val<='z') || (val>='A' && val<='Z'))
                    s+=(char) val;
            }
        }
        listner.onDone(s);
    }

}