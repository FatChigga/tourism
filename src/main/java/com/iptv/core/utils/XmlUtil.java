package com.iptv.core.utils;

import org.dom4j.*;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class XmlUtil {
    public static Map xml2map(String xmlStr, boolean needRootKey)
            throws DocumentException {
        Document doc = DocumentHelper.parseText(xmlStr);
        Element root = doc.getRootElement();
        Map<String, Object> map = xml2map(root);
        if ((root.elements().size() == 0) && (root.attributes().size() == 0)) {
            return map;
        }
        if (needRootKey) {
            Map<String, Object> rootMap = new HashMap();
            rootMap.put(root.getName(), map);
            return rootMap;
        }
        return map;
    }


    public static Map xml2mapWithAttr(String xmlStr, boolean needRootKey)
            throws DocumentException {
        Document doc = DocumentHelper.parseText(xmlStr);
        Element root = doc.getRootElement();
        Map<String, Object> map = xml2mapWithAttr(root);
        if ((root.elements().size() == 0) && (root.attributes().size() == 0)) {
            return map;
        }
        if (needRootKey) {
            Map<String, Object> rootMap = new HashMap();
            rootMap.put(root.getName(), map);
            return rootMap;
        }
        return map;
    }


    private static Map xml2map(Element e) {
        Map map = new LinkedHashMap();
        List list = e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List mapList = new ArrayList();

                if (iter.elements().size() > 0) {
                    Map m = xml2map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if ((obj instanceof List)) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else if (map.get(iter.getName()) != null) {
                    Object obj = map.get(iter.getName());
                    if (!(obj instanceof List)) {
                        mapList = new ArrayList();
                        mapList.add(obj);
                        mapList.add(iter.getText());
                    }
                    if ((obj instanceof List)) {
                        mapList = (List) obj;
                        mapList.add(iter.getText());
                    }
                    map.put(iter.getName(), mapList);
                } else {
                    map.put(iter.getName(), iter.getText());
                }
            }
        } else
            map.put(e.getName(), e.getText());
        return map;
    }


    private static Map xml2mapWithAttr(Element element) {
        Map<String, Object> map = new LinkedHashMap();

        List<Element> list = element.elements();
        List<Attribute> listAttr0 = element.attributes();
        for (Attribute attr : listAttr0) {
            map.put("@" + attr.getName(), attr.getValue());
        }
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List mapList = new ArrayList();

                if (iter.elements().size() > 0) {
                    Map m = xml2mapWithAttr(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if ((obj instanceof List)) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    List<Attribute> listAttr = iter.attributes();
                    Map<String, Object> attrMap = null;
                    boolean hasAttributes = false;
                    if (listAttr.size() > 0) {
                        hasAttributes = true;
                        attrMap = new LinkedHashMap();
                        for (Attribute attr : listAttr) {
                            attrMap.put("@" + attr.getName(), attr.getValue());
                        }
                    }

                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!(obj instanceof List)) {
                            mapList = new ArrayList();
                            mapList.add(obj);

                            if (hasAttributes) {
                                attrMap.put("#text", iter.getText());
                                mapList.add(attrMap);
                            } else {
                                mapList.add(iter.getText());
                            }
                        }
                        if ((obj instanceof List)) {
                            mapList = (List) obj;

                            if (hasAttributes) {
                                attrMap.put("#text", iter.getText());
                                mapList.add(attrMap);
                            } else {
                                mapList.add(iter.getText());
                            }
                        }
                        map.put(iter.getName(), mapList);

                    } else if (hasAttributes) {
                        attrMap.put("#text", iter.getText());
                        map.put(iter.getName(), attrMap);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }

                }

            }

        } else if (listAttr0.size() > 0) {
            map.put("#text", element.getText());
        } else {
            map.put(element.getName(), element.getText());
        }

        return map;
    }


    public static Document map2xml(Map<String, Object> map, String rootName)
            throws DocumentException, IOException {
        Document doc = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement(rootName);
        doc.add(root);
        map2xml(map, root);


        return doc;
    }


    public static Document map2xml(Map<String, Object> map) {
        Document doc = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement("Data");
        doc.add(root);

        map2xml(map, root);

        return doc;
    }

    private static Document map2xml(Entry<String, Object> entry, Document doc) {
        String key = (String) entry.getKey();
        Object value = entry.getValue();

        Element body = DocumentHelper.createElement((String) entry.getKey());
        doc.add(body);

        if (key.startsWith("@")) {
            body.addAttribute(key.substring(1, key.length()), value.toString());
        } else if (key.equals("#text")) {
            body.setText(value.toString());
        } else if ((value instanceof List)) {
            List list = (List) value;

            for (int i = 0; i < list.size(); i++) {
                Object obj = list.get(i);

                if ((obj instanceof Map)) {
                    Element subElement = body.addElement(key);
                    map2xml((Map) list.get(i), subElement);
                } else {
                    body.addElement(key).setText((String) list.get(i));
                }
            }
        } else if ((value instanceof Map)) {
            Element subElement = body.addElement(key);
            map2xml((Map) value, subElement);
        } else {
            body.addElement(key).setText(value.toString());
        }


        return doc;
    }


    private static Element map2xml(Map<String, Object> map, Element body) {
        Iterator<Entry<String, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Entry<String, Object> entry = (Entry) entries.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (key.startsWith("@")) {
                body.addAttribute(key.substring(1, key.length()), value.toString());
            } else if (key.equals("#text")) {
                body.setText(value.toString());
            } else if ((value instanceof List)) {
                List list = (List) value;

                Element subElement = body.addElement(key);
                for (int i = 0; i < list.size(); i++) {
                    Object obj = list.get(i);

                    if ((obj instanceof Map)) {
                        map2xml((Map) list.get(i), subElement);
                    } else {
                        body.addElement(key).setText((String) list.get(i));
                    }
                }
            } else if ((value instanceof Map)) {
                Element subElement = body.addElement(key);
                map2xml((Map) value, subElement);
            } else {
                body.addElement(key).setText(value.toString());
            }
        }


        return body;
    }

    public static Document list2xml(List<Map> data) {
        Document doc = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement("Data");
        doc.add(root);

        Element rows = root.addElement("Rows");
        for (Map item : data) {
            item2xml(item, rows);
        }

        return doc;
    }

    private static Element item2xml(Map<String, Object> map, Element body) {
        Iterator<Entry<String, Object>> entries = map.entrySet().iterator();
        body = body.addElement("Row");

        while (entries.hasNext()) {
            Entry<String, Object> entry = (Entry) entries.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (key.startsWith("@")) {
                body.addAttribute(key.substring(1, key.length()), value.toString());
            } else if (key.equals("#text")) {
                body.setText(value.toString());
            } else if ((value instanceof List)) {
                List list = (List) value;

                Element subElement = body.addElement(key);
                subElement = subElement.addElement("Rows");

                for (int i = 0; i < list.size(); i++) {
                    Object obj = list.get(i);

                    if ((obj instanceof Map)) {
                        item2xml((Map) list.get(i), subElement);
                    } else {
                        body.addElement(key).setText((String) list.get(i));
                    }
                }
            } else if ((value instanceof Map)) {
                Element subElement = body.addElement(key);
                map2xml((Map) value, subElement);
            } else {
                body.addElement(key).setText(value.toString());
            }
        }


        return body;
    }


    public static String formatXml(String xmlStr) {
        try {
            Document document = DocumentHelper.parseText(xmlStr);
            return formatXml(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatXml(Document document) {
        org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();

        StringWriter writer = new StringWriter();

        XMLWriter xmlWriter = new XMLWriter(writer, format);
        try {
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }
}
