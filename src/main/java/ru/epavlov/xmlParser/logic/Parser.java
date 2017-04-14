package ru.epavlov.xmlParser.logic;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ru.epavlov.xmlParser.logic.xml.Common;
import ru.epavlov.xmlParser.logic.xml.Right;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private ArrayList<Common> commons = new ArrayList<>();//общие данные для квартиры
    private Right right;// = new ArrayList<>();
    private ArrayList<HashMap<String,String>> list;
    private Document document;
    private Float area;
    public static ArrayList<String> xmlFields= new ArrayList<>();
    private static Parser instance;

    private Parser() throws ParserConfigurationException, IOException, SAXException {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(getClass().getClassLoader().getResourceAsStream("fields.xml"));

            //инициализация общих данных
            Node common= document.getElementsByTagName("Общее").item(0);
            for (int i = 0; i <common.getChildNodes().getLength() ; i++) {
                Node item = common.getChildNodes().item(i);
                if (item.getNodeType()==Node.ELEMENT_NODE){
                    commons.add(new Common(item));
                    xmlFields.add(item.getNodeName());
                }
            }
            //инициализация прав
            right = new Right(document.getElementsByTagName("Права").item(0));
    }
    public static Parser getInstance() throws IOException, SAXException, ParserConfigurationException {
        if (instance==null) instance =new Parser();
        return instance;
    }

    public void parseFile(File f, float area) throws IOException, SAXException, ParserConfigurationException {
        this.area = area;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document d = docBuilder.parse(f);
        document = d;
        list= right.getValue(d);
    //    System.out.println(list.size());
    }

    public void save(File f) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(getClass().getClassLoader().getResourceAsStream("CommonMacroses.xls"));
        HSSFSheet sheet = workbook.getSheet("Лист2");
        if  (sheet==null) sheet= workbook.createSheet("Лист2");

//         workbook.cloneSheet()
        HSSFRow rowhead = sheet.createRow((short) 0);

        for (int i = 0; i <xmlFields.size() ; i++) {
            rowhead.createCell(i).setCellValue(xmlFields.get(i));
        }
        rowhead.createCell(xmlFields.size()).setCellValue("Площадь дома");

        for (int i = 0; i <list.size() ; i++) {
            for (int j = 0; j <xmlFields.size() ; j++) {
                if (sheet.getRow(i+1)==null) sheet.createRow(i+1);
                String cellValue =list.get(i).get(xmlFields.get(j));
                String rowName = xmlFields.get(j);
                if(cellValue==null)  cellValue =commons.get(j).getValue(document);
                if (rowName.equals("Доля_собственника")){
                    if (cellValue.equals("")|| cellValue.trim().equals("Весь объем")) {
                        cellValue="1" ;
                    }
                }
                sheet.getRow(i+1).createCell(j).setCellValue(cellValue);
            }
            sheet.getRow(i+1).createCell(xmlFields.size()).setCellValue(area);
        }

        FileOutputStream fileOut = new FileOutputStream(f);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println(f.getName()+" DONE!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        right.clear();
    }



}
