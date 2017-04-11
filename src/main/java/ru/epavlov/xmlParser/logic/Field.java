package ru.epavlov.xmlParser.logic;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 10.04.2017, 15:26
 */
public class Field {

    public enum Type {
        attribute, field, text
    }
    public enum Entity{
        String, Node,NodeSet
    }
    private String xpath;
    private Type type;
    private ArrayList<String> params;
    private String xmlName;
    private Entity entity;
    private Document document;
    public Field(Node setNode){
        xmlName = setNode.getNodeName();
        NamedNodeMap nodeMap = setNode.getAttributes();
        if (nodeMap.getNamedItem("xpath")!=null) xpath = nodeMap.getNamedItem("xpath").getTextContent();
        if (nodeMap.getNamedItem("type")!=null) type = Type.valueOf(nodeMap.getNamedItem("type").getTextContent());
        if (nodeMap.getNamedItem("entity")!=null) entity= Entity.valueOf(nodeMap.getNamedItem("entity").getTextContent());
        params = new ArrayList<>(Arrays.asList(setNode.getTextContent().split(",")));

        System.out.println(xmlName+", "+xpath+", "+type+", "+entity+", params:"+ Arrays.toString(params.toArray()));
    }



    public String getXmlName() {
        return xmlName;
    }

    public String getValue(Document document){
        if (type==null) return "";
        this.document = document;
        switch (type){
            case attribute: return getAttribute();
            case text: return getText();
            case field: return getFields();
            default: break;
        }
       return "";
    }
    public ArrayList<String> getListValue(Document document){
        ArrayList<String> list =new ArrayList<>();
        if (type==null) return list;


        return  list;
    }
    private String getFields() { //получаем подполя ноды
        final String[] out = {""};
        XPath path = XPathFactory.newInstance().newXPath();
        try {
           Node  node= (Node) path.evaluate(xpath,document,XPathConstants.NODE);
            for (int i = 0; i <node.getChildNodes().getLength() ; i++) {
                Node child = node.getChildNodes().item(i);
                params.stream().filter(s -> child.getNodeName().equals(s)).forEach(s -> {
                    out[0] += child.getTextContent()+" ";
                });
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return out[0];
    }
    private String getAttribute(){ // получаем атрибуты у ноды, полученной через xpath
        final String[] out = {""};
        XPath path = XPathFactory.newInstance().newXPath();
        params.forEach(s -> {
            try {
                out[0] +=path.evaluate(xpath+"/@"+s, document, XPathConstants.STRING)+" ";
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        });
        return out[0];
    }
    private String getText(){
        String out="";
        XPath path = XPathFactory.newInstance().newXPath();
        try {
            out = (String) path.evaluate(xpath, document, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return out;
    }

    public Entity getEntity() {
        return entity;
    }
}

