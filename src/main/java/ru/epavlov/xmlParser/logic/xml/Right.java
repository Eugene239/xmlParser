package ru.epavlov.xmlParser.logic.xml;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 20.04.2017, 11:18
 */
public class Right {
    private static final String TAG = "["+ Right.class.getSimpleName()+"]: ";
    private static final Logger log = Logger.getLogger(Right.class);
    private static Right instanse = new Right();
    private HashMap<String,String> nameXpathMap = new HashMap<>(); // по имени получаем путь
    private String xpathGeneral;
    private Right(){
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
           // Document document = docBuilder.parse(getClass().getClassLoader().getResourceAsStream("fields.xml"));
            Document document = docBuilder.parse(System.getProperty("user.dir")+"/fields.xml");
            Node setNode = document.getElementsByTagName("Права").item(0);
            xpathGeneral = setNode.getAttributes().getNamedItem("xpath").getTextContent();
            for (int i = 0; i < setNode.getChildNodes().getLength(); i++) {
                Node node = setNode.getChildNodes().item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String xpath = node.getAttributes().getNamedItem("xpath").getTextContent();
                    nameXpathMap.put(node.getNodeName(),xpath);
                }
            }
        }catch (Exception e){
            log.error(TAG+e.toString());
            e.printStackTrace();
        }
    }
    public static Right getInstanse(){
        return instanse;
    }



    public ArrayList<HashMap<String,String>> parse(Document document){
        ArrayList<HashMap<String,String>> mapArrayList = new ArrayList<>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            NodeList list= (NodeList) xpath.evaluate(xpathGeneral,document, XPathConstants.NODESET);
            for (int i = 0; i <list.getLength() ; i++) {
                HashMap<String,String> map = new HashMap<>();
                int finalI = i;
                nameXpathMap.forEach((name, subPath)-> {
                    String path = xpathGeneral+"["+(finalI +1)+"]"+subPath;
                    String value="";
                    try {
                        value = xpath.evaluate(path,document, XPathConstants.STRING).toString();
                    } catch (XPathExpressionException e) {
                        log.error(TAG+"parse()"+e.toString());
                        e.printStackTrace();
                    }
                    map.put(name,value);
                });
                mapArrayList.add(map);
            }
        } catch (XPathExpressionException e) {
            log.error(TAG+"parse()"+e.toString());
            e.printStackTrace();
        }
        return mapArrayList;
    }


}