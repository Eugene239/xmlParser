package ru.epavlov.xmlParser.logic.xml;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 12.05.2017, 16:14
 */
public class Person {
    private static final String TAG = "["+ Person.class.getSimpleName()+"]: ";
    private static final Logger log = Logger.getLogger(Person.class);
    private static final String VALUE_PATH= "/Person/Content/text()";
    public static ArrayList<String>  getPerson(Document document, String xpathMain, String xpathAdd) {
        System.out.println(xpathMain+" "+xpathAdd);
        ArrayList<String> ownerList =new ArrayList<>();
        String expr;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
//            NodeList rightList = (NodeList) xpath.evaluate(xpathMain, document, XPathConstants.NODESET); //узнаю сколько всего прав
//            System.out.println(rightList.getLength());
//            for (int i = 0; i <rightList.getLength() ; i++) {
 //               System.out.println(i+" "+rightList.item(i).getNodeName());
                expr = xpathMain+xpathAdd;
                NodeList owners = (NodeList) xpath.evaluate(expr, document, XPathConstants.NODESET); //узнаю сколько всего владельцев
                for (int j = 0; j <owners.getLength() ; j++) {
                    expr = xpathMain+xpathAdd+"["+(j+1)+"]"+VALUE_PATH; // expr = xpathMain+"["+(i+1)+"]"+xpathAdd+"["+(j+1)+"]"+VALUE_PATH;
                    String s = (String) xpath.evaluate(expr, document, XPathConstants.STRING); //узнаю сколько всего владельцев
                    ownerList.add(s);
                    System.out.println(s);
                }
    //        }
       }catch (Exception e){
           e.printStackTrace();
           log.error(TAG+e);
       }
       return ownerList;
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document d = docBuilder.parse(new File("C:\\Users\\epavlov\\Downloads\\Telegram Desktop\\124.xml"));
        ArrayList<String> s= Person.getPerson(d,"//ExtractObject/ObjectRight/Right","/Owner");
        System.out.println(s.size());
        s.forEach(System.out::println);

    }
}
