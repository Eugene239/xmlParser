package ru.epavlov.xmlParser.logic.xml;

import org.w3c.dom.Document;
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
 * @version 1.0 Created 12.04.2017, 11:10
 */
public class Common {
    private String xmlName;
    private String xpath;
    private ArrayList<String> params;
    private String value="";

    public Common(Node node){
        xmlName=  node.getNodeName();
        xpath = node.getAttributes().getNamedItem("xpath").getTextContent();
        if (!node.getTextContent().equals("")) params = new ArrayList<>(Arrays.asList(node.getTextContent().split(",")));
    }

    public String getValue(Document document){
        XPath path = XPathFactory.newInstance().newXPath();
        if (params==null) try {
            value = (String) path.evaluate(xpath,document, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }else{
            value="";
            params.forEach(s -> {
                try {
                    value+= path.evaluate(xpath+"/@"+s,document, XPathConstants.STRING) +" ";
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }
            });
        }

        return value;
    }

    public String getXmlName() {
        return xmlName;
    }

    public String getValue() {
        return value;
    }
}
