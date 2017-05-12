package ru.epavlov.xmlParser.logic.xml;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.epavlov.xmlParser.logic.Parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
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
            Document document = docBuilder.parse(new File(Parser.config));
            Node setNode = document.getElementsByTagName("Права").item(0);
            xpathGeneral = setNode.getAttributes().getNamedItem("xpath").getTextContent();
            for (int i = 0; i < setNode.getChildNodes().getLength(); i++) {
                Node node = setNode.getChildNodes().item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    String xpath = node.getAttributes().getNamedItem("xpath").getTextContent();
                 //   System.out.println(node.getNodeName()+ " "+xpath);
                    nameXpathMap.put(node.getNodeName(),xpath);
                }
            }
            System.out.println(">>>>>>>>>>>");
            System.out.println();
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
                    if(!name.equals("ФИО")) {
                        String path = xpathGeneral + "[" + (finalI + 1) + "]" + subPath;
                        String value = "";
                        try {
                            value = xpath.evaluate(path, document, XPathConstants.STRING).toString();
                        } catch (XPathExpressionException e) {
                            log.error(TAG + "parse()" + e.toString());
                            e.printStackTrace();
                        }
                       // System.out.println(name + " " + value);
                        map.put(name, value);
                    }
                });
                ArrayList<String> ownersList= Person.getPerson(document,xpathGeneral+"["+(i+1)+"]", nameXpathMap.get("ФИО"));
                ownersList.forEach(s -> {
                    HashMap<String,String> fullMap = new HashMap<>();
                    fullMap.putAll(map);
                    fullMap.put("ФИО",s);
                    mapArrayList.add(fullMap);
                });
                //mapArrayList.add(map);
            }

        } catch (XPathExpressionException e) {
            log.error(TAG+"parse()"+e.toString());
            e.printStackTrace();
        }
        return mapArrayList;
    }


}