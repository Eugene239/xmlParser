package ru.epavlov.xmlParser.logic;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ru.epavlov.xmlParser.logic.xml.Right;
import ru.epavlov.xmlParser.logic.xml.Room;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static final String config = Paths.get("").toFile().getAbsolutePath()+"\\fields.xml";
    private static final String TAG = "["+ Parser.class.getSimpleName()+"]: ";
    private static final Logger log = Logger.getLogger(Parser.class);
    //public static  String OUTPUT_FILE = System.getProperty("user.dir") + "\\result.xls";
    public static  String OUTPUT_FILE =   "C:\\Users\\epavlov\\Desktop\\Новая папка"+"\\result.xls";

    private ArrayList<String> headers = new ArrayList<>();
    private Float area;
    private ArrayList<HashMap<String,String>> data= new ArrayList();
    private static Parser instance;

    private Parser() throws ParserConfigurationException, IOException, SAXException {
        parseHeaders();

    }

    private void parseHeaders() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        Document document = docBuilder.parse(new File(config));
     //   Document document = docBuilder.parse(getClass().getClassLoader().getResourceAsStream("fields.xml"));
        Node node = document.getElementsByTagName("Последовательность").item(0);
        String[] arr= node.getTextContent().split(",");
        Arrays.stream(arr).forEach(s -> {
            String str =s.trim();
            headers.add(str);
        });
    }
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        Parser.getInstance();
    }
    public static Parser getInstance() throws IOException, SAXException, ParserConfigurationException {
        if (instance == null)
            instance = new Parser();
        return instance;
    }

    public void parseFile(File f, float area) throws IOException, SAXException, ParserConfigurationException {
        this.area = area;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document d = docBuilder.parse(f);
        HashMap<String,String> roomMap= Room.getInstanse().parse(d);
        ArrayList<HashMap<String,String>> rightList=  Right.getInstanse().parse(d);

        rightList.forEach(rightMap->{
            rightMap.putAll(roomMap);
            rightMap.put("file",f.getName());
//            rightMap.forEach((s,s1)->{
//            });
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
        for (int i = 0; i <headers.size() ; i++) {
            rowhead.createCell(i).setCellValue(headers.get(i));
        }

        //   System.out.println(data.size());
        for (int i = 0; i <data.size() ; i++) { //rows
            HashMap<String,String> map = data.get(i);
            if (sheet.getRow(i+1)==null) sheet.createRow(i+1);
            for (int j = 0; j <headers.size() ; j++) {
                String field = headers.get(j);
                String value = map.get(headers.get(j));
                value= value==null? "":value;
                if (field.equals("Площадь дома")) value= String.valueOf(area);
                if (field.equals("ФИО") && value.equals("")) value= "Санкт-Петербург";
                if (field.equals("Доля_собственника") && value.equals("")|| value.trim().equals("Весь объем")) value= "1";
                if (field.equals("Площадь_квартиры")) value= value.replace(".",",");
                if (field.equals("Площадь дома")) value= value.replace(".",",");
                sheet.getRow(i+1).createCell(j).setCellValue(value);
            }
        }
        FileOutputStream fileOut = new FileOutputStream(f);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
        workbook.close();
        log.info(f.getAbsolutePath() + " DONE!");
//        System.out.println(f.getName() + " DONE!");
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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

    public void clear(){
        data.clear();
    }

}