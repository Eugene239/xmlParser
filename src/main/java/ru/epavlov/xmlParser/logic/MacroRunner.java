
package ru.epavlov.xmlParser.logic;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    public interface RunnerListner {
        void onDone(String s);
    }

    private static final String TAG = "[" + MacroRunner.class.getSimpleName() + "]: ";
    private static final Logger log = Logger.getLogger(MacroRunner.class);

    public static final String PATH = System.getProperty("user.dir") + "\\MacrosRunner.exe";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    private static RunnerListner listner;

    public static void start(File f, RunnerListner listner) throws IOException {
        MacroRunner.listner = listner;
        System.out.println(PATH + " \"" + f.getAbsolutePath() + "\"");
        ProcessBuilder pb = new ProcessBuilder(PATH, "\"" + f.getAbsolutePath() + "\"");
        pb.redirectErrorStream(true);
        Process p = pb.start();
        readWithIS(p);
    }

    private static void readWithIS(Process p) throws IOException {
        InputStream is = p.getInputStream();
        int val;
        String s = "";
        while (p.isAlive()) {
            while ((val = is.read()) != -1) {
                if ((val >= 'a' && val <= 'z') || (val >= 'A' && val <= 'Z'))
                    s += (char) val;
            }
        }
        listner.onDone(s);
    }

    private static boolean checkHex(String confirm) {
        DateTime start = new DateTime(DateTime.now().getYear(), 1, 1, 0, 0);
        DateTime now = DateTime.now();
        String hex1 = DigestUtils.md5Hex(String.valueOf(Minutes.minutesBetween(start, now).getMinutes()));
        String hex2 = DigestUtils.md5Hex(String.valueOf(Minutes.minutesBetween(start, now).getMinutes() - 1));
        hex1 = hex1.replaceAll("\\D", "");
        hex2 = hex2.replaceAll("\\D", "");
        if (hex1.equals(confirm)) return true;
        if (hex2.equals(confirm)) return true;
        return false;
    }

    public static boolean confirm() {
        try {
            ProcessBuilder pb = new ProcessBuilder(PATH, "confirm");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            String s = "";
            while (p.isAlive()) {
                s += (char) p.getInputStream().read();
            }
            s = s.replaceAll("\\D", "");
            return checkHex(s);
        } catch (Exception e) {
            log.error(TAG + e.toString());
            e.printStackTrace();
        }
        return false;
        // System.out.println(s);
        // String startTime = DateTime.now().getYear()+"";


        //  DateTime.now().getYear();

        //   System.out.println(start.getYear()+" "+start.getMonthOfYear()+" "+start.getDayOfMonth()+" "+start.getHourOfDay()+" "+start.getMinuteOfHour());
        //     DateTime now = DateTime.now();
        //    System.out.println(now.getYear()+" "+now.getMonthOfYear()+" "+now.getDayOfMonth()+" "+now.getHourOfDay()+" "+now.getMinuteOfHour());
        //  ;
        //   byte[] md5= DigestUtils.md5(String.valueOf(Minutes.minutesBetween(start,now).getMinutes()));
        //
//        String between = String.valueOf(Minutes.minutesBetween(start,now).getMinutes());
//          System.out.println("between:"+between);
//        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
//
//        byte[] md = new byte[md5.length];
//        for (int i = 0; i <md5.length ; i++) {
//            md[i]= (byte) Math.abs(md5[i]);
//        }
//        BigInteger bigInt1 = new BigInteger(1,DigestUtils.md5(md));
//        BigInteger bigInt2 = new BigInteger(1,messageDigest.digest(between.getBytes()));
        //  System.out.println("bytes: "+ Arrays.toString(messageDigest.digest(between.getBytes())));
        //  System.out.println("digest: "+ bigInt1.toString(10));
        //   System.out.println("apache:"+ DigestUtils.md5Hex(between.getBytes()));

    }

    public static void main(String[] args) throws IOException, ParseException, NoSuchAlgorithmException {
        System.out.println(confirm());
        //System.currentTimeMillis()-
        //  System.out.println(MessageDigest.getInstance("MD5").digest());
    }
}