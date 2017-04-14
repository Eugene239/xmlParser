package ru.epavlov.xmlParser.logic.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.epavlov.xmlParser.logic.Parser;

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
 * @version 1.0 Created 12.04.2017, 10:08
 */
public class Right {
    private Document document;
    private String xpathGeneral="";
    private HashMap<String,String> hashMapXpath = new HashMap<>(); //мапа отображений
    private ArrayList<HashMap<String,String>> list =new ArrayList<>(); // лист всех значений
    public Right(Node setNode){
        xpathGeneral = setNode.getAttributes().getNamedItem("xpath").getTextContent();
        for (int i = 0; i <setNode.getChildNodes().getLength() ; i++) {
            Node node= setNode.getChildNodes().item(i);
            if (node.getNodeType()==Node.ELEMENT_NODE){
                Parser.xmlFields.add(node.getNodeName());
                hashMapXpath.put(node.getNodeName(),node.getAttributes().getNamedItem("xpath").getTextContent());
            }
        }
//        hashMapXpath.forEach((s, s2) -> {
//            System.out.println(s+" "+s2);
//        });
    }

    public ArrayList<HashMap<String,String>> getValue(Document d){
        this.document = d;
      //  list.clear();
        try {
            XPath path = XPathFactory.newInstance().newXPath();
            NodeList  list= (NodeList) path.evaluate(xpathGeneral,document, XPathConstants.NODESET);
            for (int i = 0; i <list.getLength() ; i++) {
                getIndexValue(i);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

//        list.forEach(stringStringHashMap -> {
//            stringStringHashMap.forEach((s, s2) -> {
//                System.out.println(s+" "+s2);
//            });
//            System.out.println(">>>>>>>>>>>>>>>>>");
//        });
       return list;
    }

    private void getIndexValue(int i){
        HashMap<String,String> xmlName_Value = new HashMap<>();
            XPath path = XPathFactory.newInstance().newXPath();
            hashMapXpath.forEach((xmlName, p) -> {
                String xpath= xpathGeneral+"["+(i+1)+"]"+p;
                try {
                    xmlName_Value.put(xmlName,path.evaluate(xpath,document, XPathConstants.STRING).toString());
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }
            });
        list.add(xmlName_Value);
    }
    public void clear(){
        list.clear();
    }


}
