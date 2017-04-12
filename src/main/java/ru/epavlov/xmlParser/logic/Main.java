package ru.epavlov.xmlParser.logic;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

/**
 * Описание класса<br/>
 * <p> Copyright (c) </p>
 * <p> Company: <a href="http://estp-sro.ru/">ESTP-SRO</a>, Inc. Все права защищены.</p>
 * <p> Это программное обеспечение является собственностью ESTP-SRO.</p>
 *
 * @author <a href="http://estp.ru/">ESTP-SRO</a>
 * @version 1.0 Created 11.04.2017, 16:19
 */
public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document d= docBuilder.parse(new File("C:\\Users\\epavlov\\Desktop\\xmlEugene\\kp_b39c766e-1458-4658-a5c0-5b2dcb4ea495.xml"));
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "//ExtractObject/ObjectRight/Right[2]";
//        NodeList list= (NodeList) xpath.evaluate(expression, d, XPathConstants.NODESET);
//        for (int i = 0; i <list.getLength() ; i++) {
//            System.out.println(list.item(i).getTextContent());
//        }
        Node node = (Node) xpath.evaluate(expression, d, XPathConstants.NODE);
        System.out.println(node.getTextContent());
    }
}
