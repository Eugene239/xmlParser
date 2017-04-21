package ru.epavlov.xmlParser.logic;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ru.epavlov.xmlParser.logic.xml.Right;
import ru.epavlov.xmlParser.logic.xml.Room;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 05.04.2017, 18:08
 */
//ObjectRight

public class Parser {
    public static  String OUTPUT_FILE = System.getProperty("user.dir") + "\\result.xls";

    String[] fields= {"Улица","Дом","Корпус","Литера","Квартира","Площадь_квартиры","ФИО","Номер_свидетельства","Дата_свидетельства","Доля_собственника","Площадь дома"};

    private Float area;
    //public static ArrayList<String> xmlFields = new ArrayList<>();
    private ArrayList<HashMap<String,String>> data= new ArrayList();
    private static Parser instance;

    private Parser() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(getClass().getClassLoader().getResourceAsStream("fields.xml"));
    }

    public static Parser getInstance() throws IOException, SAXException, ParserConfigurationException {
        if (instance == null) instance = new Parser();
        return instance;
    }

    public void parseFile(File f, float area) throws IOException, SAXException, ParserConfigurationException {
        this.area = area;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document d = docBuilder.parse(f);
      //  System.out.println("################ "+f.getName()+" ###############");
        HashMap<String,String> roomMap= Room.getInstanse().parse(d);
        ArrayList<HashMap<String,String>> rightList=  Right.getInstanse().parse(d);
        rightList.forEach(rightMap->{
            rightMap.putAll(roomMap);
            rightMap.forEach((s,s1)->{
            });
             data.add(rightMap);
        });
    }

    public void save(File f ) throws IOException{

        checkFile(f); // создаем новый файл
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(f));
        HSSFSheet sheet = workbook.getSheet("Лист1");
        if (sheet == null) sheet = workbook.createSheet("Лист1");
        //создаем шапку
        HSSFRow rowhead = sheet.createRow((short) 0);
        for (int i = 0; i <fields.length ; i++) {
            rowhead.createCell(i).setCellValue(fields[i]);
        }


        for (int i = 0; i <data.size() ; i++) { //rows
            HashMap<String,String> map = data.get(i);
           // System.out.println(i+":>>>>>>>>>>>>");
            if (sheet.getRow(i+1)==null) sheet.createRow(i+1);
            for (int j = 0; j <fields.length ; j++) {
                String field = fields[j];
                String value = map.get(fields[j]);
                if (field.equals("Площадь дома")) value= String.valueOf(area);
                if (field.equals("ФИО") && value.equals("")) value= "Санкт-Петербург";
                if (field.equals("Доля_собственника") && value.equals("")|| value.trim().equals("Весь объем")) value= "1";
                sheet.getRow(i+1).createCell(j).setCellValue(value);
               // System.out.println(field+" "+value);
            }
        }
        FileOutputStream fileOut = new FileOutputStream(f);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println(f.getName() + " DONE!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        data.clear();
    }
    private void checkFile(File f) throws IOException {
        if (f.exists()) f.delete();
        if (!f.exists()) {
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            InputStream is = getClass().getClassLoader().getResourceAsStream("empty.xls");
            while (is.available() > 0) {
                fos.write(is.read());
            }
            fos.flush();
            fos.close();
            is.close();
        }
    }



}
