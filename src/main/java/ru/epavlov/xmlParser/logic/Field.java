package ru.epavlov.xmlParser.logic;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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


    private String name;
    private Type type;
    private ArrayList<String> params;
    private String xmlName;

    public Field(Node setNode){
        name = setNode.getAttributes().getNamedItem("name").getTextContent();
        type = Type.valueOf(setNode.getAttributes().getNamedItem("type").getTextContent());
        params = new ArrayList<>(Arrays.asList(setNode.getTextContent().split(",")));
        xmlName = setNode.getNodeName();

        System.out.println("["+setNode.getNodeName().toUpperCase()+"]:  "+ " name: "+name+", type: "+type+", params: "+ Arrays.toString(params.toArray()));

    }

    public String getName() {
        return name;
    }

    public String getXmlName() {
        return xmlName;
    }

    public String getValue(Node node){
        switch (type) {
            case text:
                return node.getTextContent();
            case field:
                return getFields(node);
            case attribute: return getAttribute(node);
        }
        return "";
    }
    private String getFields(Node node) {
        String out = "";
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            node = list.item(i);
            if (params.contains(node.getNodeName())) {
                out += node.getTextContent() + " ";
            }
        }
        return out;
    }
    private String getAttribute(Node node){
        final String[] out = {""};
        params.forEach(s -> {
            out[0] +=node.getAttributes().getNamedItem(s).getTextContent()+" ";
        });
        return out[0];
    }

}

