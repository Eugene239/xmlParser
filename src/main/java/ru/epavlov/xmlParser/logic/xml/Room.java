package ru.epavlov.xmlParser.logic.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
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
 * @version 1.0 Created 20.04.2017, 10:33
 */
public class Room {
    private static Room instanse = new Room();
    private HashMap<String,String> nameXpathMap = new HashMap<>(); // по имени получаем путь
    private HashMap<String,ArrayList<String>> parmsMap =  new HashMap();  //по имени получаем параметры



    private Room(){
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
           // Document document = docBuilder.parse(getClass().getClassLoader().getResourceAsStream("fields.xml"));
            Document document = docBuilder.parse(System.getProperty("user.dir")+"/fields.xml");
            //инициализация общих данных
            Node common = document.getElementsByTagName("Общее").item(0);
            for (int i = 0; i < common.getChildNodes().getLength(); i++) {
                Node item = common.getChildNodes().item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    String xmlName=  item.getNodeName();
                    String xpath = item.getAttributes().getNamedItem("xpath").getTextContent();
                    if (!item.getTextContent().equals("")) {
                        ArrayList<String> params = new ArrayList<>(Arrays.asList(item.getTextContent().split(",")));
                        parmsMap.put(xmlName,params);
                    }
                    nameXpathMap.put(xmlName,xpath);
                }
            }
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    public static Room getInstanse(){
        return instanse;
    }
    public HashMap<String,String> parse(Document d){
        HashMap<String,String> col = new HashMap<>();
        nameXpathMap.forEach((name,path)->{
            col.put(name,getXpathValue(path,d,parmsMap.get(name)));
            //    System.out.println(name+": "+ getXpathValue(path,d,parmsMap.get(name)));
        });
        return col;
    }

    private String getXpathValue(String xpath, Document document, ArrayList<String> params){
        String value = null;
        XPath path = XPathFactory.newInstance().newXPath();
        try {
            if (params==null) {
                value = (String) path.evaluate(xpath, document, XPathConstants.STRING);
            }else {
                final String[] val = {""};
                params.forEach(s -> {
                    try {
                        val[0] += path.evaluate(xpath+"/@"+s,document, XPathConstants.STRING) +" ";
                    } catch (XPathExpressionException e) {
                        e.printStackTrace();
                    }
                });
                value = val[0];
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return value;
    }


}