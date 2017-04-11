package ru.epavlov.xmlParser.logic;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    protected static HashMap<String, Field> stringValueHashMap = new HashMap<>();
    private ArrayList<String> seq = new ArrayList<>();
    private RoomInfo roomInfo;
    private static Parser instance = new Parser();
    //  private DocumentBuilder docBuilder;
    private ArrayList<Field> nodeList;

    private Parser() {
        //address + objectRights
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(getClass().getClassLoader().getResourceAsStream("fields.xml"));
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Field f = new Field(node);
                    stringValueHashMap.put(f.getXmlName(), f);
                }

            }
            System.out.println("########################################");
            System.out.println();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Parser getInstance() {
        return instance;
    }

    public void parseFile(File f) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document d = docBuilder.parse(f);
        stringValueHashMap.forEach((s, field) -> {
          //  System.out.println(">>>" + s + " " + field.getEntity());
            if (field.getEntity() != null) {
                switch (field.getEntity()) {
                    case Node:
                    case String:
                        System.out.println(field.getEntity()+":: "+s + ": " + field.getValue(d));
                        break;
                    case NodeSet:
                        System.out.println(field.getEntity()+":: "+s+": "+ Arrays.toString(field.getListValue(d).toArray()));
                        break;
                }
            } else {
                System.out.println("NULL:: "+s + ": " + field.getValue(d));
            }
        });

    }

    public void save(File f) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("FirstSheet");

        HSSFRow rowhead = sheet.createRow((short) 0);
        roomInfo.init(); // проверяем целостность данных
        for (int i = 0; i < seq.size(); i++) { // стобцы
            String xmlName = stringValueHashMap.get(seq.get(i)).getXmlName();
            rowhead.createCell(i).setCellValue(xmlName);
            ArrayList<String> list = roomInfo.getValue(xmlName);
            for (int j = 0; j < list.size(); j++) {
                if (sheet.getRow(j + 1) == null) sheet.createRow(j + 1);
                sheet.getRow(j + 1).createCell(i).setCellValue(list.get(j));
            }
        }

        for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i++) {
            sheet.autoSizeColumn(i, true);
        }
        FileOutputStream fileOut = new FileOutputStream(f);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
        System.out.println("Your excel file has been generated!");
    }


}
