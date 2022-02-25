package com.app.year2021.month12;

import lombok.Data;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class XsdApplication {

    private static final String TITLE_NODE = "xs:annotation/xs:documentation";
    private static final String RESTRICTION_NODE = "xs:simpleType/xs:restriction";
    private static final String ENUM_NODE = "xs:simpleType/xs:restriction/xs:enumeration";
    private static final String COMPLEX_NODE = "xs:complexType/xs:sequence/xs:element";
    private static final String CHOICE_NODE = "xs:choice/xs:element";
    private static final String APPINFO_NODE = "xs:annotation/xs:appinfo/xs:element";
    private static final String PROPERTIES_ATTR_NODE = "xs:complexType/xs:attribute";
    private static final String SELF_CHOICE_NODE = "xs:complexType/xs:sequence/xs:choice/xs:element";

    public static List<XsdElementBean> parseXsd(InputStream in) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(in);
            List<Element> elements = doc.getRootElement().elements();
            List<XsdElementBean> elementBeans = new ArrayList<>();
            for (Element element : elements) {
                parseElement(element, elementBeans, "", doc);
            }
            return elementBeans;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static List<XsdElementBean> parseXsd(byte[] bytes) {
        return parseXsd(new ByteArrayInputStream(bytes));
    }

    private static void parseElement(Element element, List<XsdElementBean> parentChildren, String parentTag, Document doc) {
        String ref = element.attributeValue("ref");
        String isFrequent = element.attributeValue("xml:frequent");
        if (StringUtils.isEmpty(isFrequent)) {
            if (StringUtils.isEmpty(ref)) {//非引用节点
                parseBaseElment(element, parentChildren, parentTag, doc);
            } else {
                Element refEle = (Element) doc.selectSingleNode(String.format("//xs:element[ @name= \"%s\" and @xml:reference=\"true\"]", ref));
                if (refEle != null) {
                    String isSegment = refEle.attributeValue("segment");
                    //忽略引用层
                    if ("true".equals(isSegment)) {
                        List<Node> complexNodes = refEle.selectNodes(COMPLEX_NODE);
                        for (Node complexNode : complexNodes) {
                            Element node = (Element) complexNode;
                            parseBaseElment(node, parentChildren, parentTag, doc);
                        }
                    } else {
                        parseBaseElment(refEle, parentChildren, parentTag, doc);
                    }
                }
            }
        }
    }

    private static XsdElementBean parseBaseElment(Element element, List<XsdElementBean> parentChildren, String parentTag, Document doc) {
        String reference = element.attributeValue("reference");
        String isSegment = element.attributeValue("segment");
        if ("true".equals(reference) && "true".equals(isSegment)) {//自引用节点直接忽略
            return null;
        }
        XsdElementBean xsdElementBean = new XsdElementBean();
        xsdElementBean.setName(element.attributeValue("name"));
        xsdElementBean.setDefaultValue(element.attributeValue("default"));
        xsdElementBean.setTitle(getElementTitle(element));
        if (StringUtils.isEmpty(parentTag)) {
            xsdElementBean.setTag(xsdElementBean.getName());
        } else {
            xsdElementBean.setTag(parentTag + "." + xsdElementBean.getName());
        }
        //设置基础属性
        String defaultV = element.attributeValue("default");
        String webEditable = element.attributeValue("webEditable");
        String webNullable = element.attributeValue("webNullable");
        String webValidate = element.attributeValue("webValidate");
        String hotFixable = element.attributeValue("hotFixable");
        String autoApply = element.attributeValue("autoApply");
        String array = element.attributeValue("array");
//        String reference = element.attributeValue("xml:reference");
        String desc = element.attributeValue("desc");
        String length = element.attributeValue("length");
        String frequent = element.attributeValue("frequent");
        String maxOccurs = element.attributeValue("maxOccurs");
        String minOccurs = element.attributeValue("minOccurs");

        if (!StringUtils.isEmpty(defaultV)) {
            xsdElementBean.setDefaultValue(defaultV);
        }
        if (!StringUtils.isEmpty(webEditable)) {
            xsdElementBean.setWebEditable(webEditable);
        }
        if (!StringUtils.isEmpty(webNullable)) {
            xsdElementBean.setWebNullable(webNullable);
        }
        if (!StringUtils.isEmpty(webValidate)) {
            xsdElementBean.setWebValidate(webValidate);
        }
        if (!StringUtils.isEmpty(hotFixable)) {
            xsdElementBean.setHotFixable(hotFixable);
        }
        if (!StringUtils.isEmpty(autoApply)) {
            xsdElementBean.setAutoApply(autoApply);
        }
        if (!StringUtils.isEmpty(array)) {
            xsdElementBean.setIsArray(array);
        }
        if (!StringUtils.isEmpty(desc)) {
            xsdElementBean.setDesc(desc);
        }
        if (!StringUtils.isEmpty(length)) {
            xsdElementBean.setLength(length);
        }
        if (!StringUtils.isEmpty(frequent)) {
            xsdElementBean.setIsFrequent(frequent);
        }
        if (!StringUtils.isEmpty(maxOccurs)) {
            xsdElementBean.setMaxOccurs(maxOccurs);
        }
        if (!StringUtils.isEmpty(minOccurs)) {
            xsdElementBean.setMinOccurs(minOccurs);
        }
        if (parentChildren != null) { //如为空，可能为options，不添加到子节点中
            parentChildren.add(xsdElementBean);
        }
        //枚举类型
        Node restrictionNode = element.selectSingleNode(RESTRICTION_NODE);
        if (restrictionNode != null) {
            List<Node> enumNodes = element.selectNodes(ENUM_NODE);
            //枚举类型,下拉框
            if (!CollectionUtils.isEmpty(enumNodes)) {
                xsdElementBean.setType("enumeration");
                ArrayList<EnumerationBean> enumerations = new ArrayList<>();
                for (Node enumNode : enumNodes) {
                    Element node = (Element) enumNode;
                    EnumerationBean enumeration = new EnumerationBean();
                    enumeration.setName(getElementTitle(node));
                    enumeration.setValue(node.attributeValue("value"));
                    enumeration.setOptions(node.attributeValue("options"));
                    //appinfo联动枚举
                    List<Node> appinfoNodes = enumNode.selectNodes(APPINFO_NODE);
                    if (!CollectionUtils.isEmpty(appinfoNodes)) {
                        enumeration.setAppInfo(getAppinfos(appinfoNodes, parentTag, doc));
                    }
                    enumerations.add(enumeration);
                }
                xsdElementBean.setEnumeration(enumerations);
                //如枚举类型无默认值，设置第一个为默认值
                if (StringUtils.isEmpty(xsdElementBean.getDefaultValue()) && enumerations.size() > 0) {
                    xsdElementBean.setDefaultValue(enumerations.get(0).getValue());
                }
                //枚举choice
                List<Node> choiceNodes = element.getParent().selectNodes(CHOICE_NODE);
                if (!CollectionUtils.isEmpty(choiceNodes)) {
                    List<XsdElementBean> options = new ArrayList<>();
                    for (Node choiceNode : choiceNodes) {
                        Element node = (Element) choiceNode;
                        //parentchildren传空，不添加到子节点中
                        options.add(parseBaseElment(node, null, parentTag, doc));
                    }
                    xsdElementBean.setOptions(options);
                }
            } else {//简单类型，maxlength，minlength
                Element restrictionEle = (Element) restrictionNode;
                String type = restrictionEle.attributeValue("base");
                //无已有类型且包含xs指定类型
                if (!StringUtils.isEmpty(xsdElementBean.getType()) && !StringUtils.isEmpty(type) && type.contains("xs:")) {
                    xsdElementBean.setType(type.split(":")[1]);
                }
                xsdElementBean.setMinExclusive(getValue(restrictionEle, "xs:minExclusive"));
                xsdElementBean.setMaxInclusive(getValue(restrictionEle, "xs:maxInclusive"));
                xsdElementBean.setMinLength(getValue(restrictionEle, "xs:minLength"));
                xsdElementBean.setMaxLength(getValue(restrictionEle, "xs:maxLength"));
            }
            return xsdElementBean;
        }


        //复杂元素，有子元素
        List<Node> complexNodes = element.selectNodes(COMPLEX_NODE);
        if (!CollectionUtils.isEmpty(complexNodes)) {
            xsdElementBean.setType("complex");
            for (Node childElement : complexNodes) {
                Element node = (Element) childElement;
                parseElement(node, xsdElementBean.getChildren(), xsdElementBean.getTag(), doc);
            }
            return xsdElementBean;
        }
        //自选择元素
        List<Node> selfChoiceNodes = element.selectNodes(SELF_CHOICE_NODE);
        if (!CollectionUtils.isEmpty(selfChoiceNodes)) {
            xsdElementBean.setType("selfChoice");
            for (Node childElement : selfChoiceNodes) {
                Element node = (Element) childElement;
                parseElement(node, xsdElementBean.getChildren(), xsdElementBean.getTag(), doc);
            }
            return xsdElementBean;
        }


        List<Node> propertiesAttrs = element.selectNodes(PROPERTIES_ATTR_NODE);
        //是否为properties
        if (!CollectionUtils.isEmpty(propertiesAttrs)) {
            xsdElementBean.setType("appinfo");
            List<Node> appinfoNodes = element.selectNodes(APPINFO_NODE);
            //添加固定appinfo，固定的key/value
            if (!CollectionUtils.isEmpty(appinfoNodes)) {
                xsdElementBean.setAppInfo(getAppinfos(appinfoNodes, parentTag, doc));
            }
            return xsdElementBean;
        }

        String segProperty = element.attributeValue("segProperty");
        //枚举类型appinfo
        if ("type".equals(segProperty)) {
            Element parent = element.getParent().getParent();
            Element relativeSegProperty = (Element) parent.selectSingleNode(String.format("//xs:attribute[ @name= \"%s\" and @xml:relativeSegProperty= \"%s\" ]", segProperty, xsdElementBean.getName()));
            xsdElementBean.setType("enumerationAppinfo");
            parseElement(relativeSegProperty, parentChildren, parentTag, doc);
            return xsdElementBean;
        }

        //其他简单元素
        String type = element.attributeValue("type");
        //无已有类型且包含xs指定类型
        if (!StringUtils.isEmpty(xsdElementBean.getType()) && !StringUtils.isEmpty(type) && type.contains("xs:")) {
            xsdElementBean.setType(type.split(":")[1]);
        }

        return xsdElementBean;
    }

    private static List<XsdElementBean> getAppinfos(List<Node> appinfoNodes, String parentTag, Document doc) {
        List<XsdElementBean> appinfos = new ArrayList<>();
        for (Node appinfoNode : appinfoNodes) {
            //parentchildren传空，不添加到子节点中
            Element node = (Element) appinfoNode;
            appinfos.add(parseBaseElment(node, null, parentTag + ".properties", doc));
        }
        return appinfos;
    }

    private static String getElementTitle(Element element) {
        Node titleNode = element.selectSingleNode(TITLE_NODE);
        if (titleNode != null) {
            return titleNode.getText();
        } else {//如果无描述，取name
            return element.attributeValue("name");
        }
    }

    private static String getValue(Element restrictionEle, String elementTag) {
        Node node = restrictionEle.selectSingleNode(elementTag);
        if (node != null) {
            Element element = (Element) node;
            return element.attributeValue("value");
        }
        return null;
    }


    @Data
    static class XsdElementBean {
        private String name;
        //标题，来自xs:annotation/xs:documentation的text
        private String title;
        //展示类型
        private String type;
        private List<XsdElementBean> children;
        //如为枚举等类型时，此处给出枚举值
        private List<EnumerationBean> enumeration;
        //枚举类型的联动options
        private List<XsdElementBean> options;
        //普通APPINFO类型节点，appinfo类型专有
        private List<XsdElementBean> appInfo;
        //默认值,default
        private String defaultValue;
        //是否可编辑，默认true，xml:webEditable
        private String webEditable;
        //是否能为空，默认true，xml:webNullable
        private String webNullable;
        //校验规则，IP/端口等，xml:webValidate
        private String webValidate;
        //是否热生效，默认true，如为false则在发生变更时提醒用户，xml:hotFixable
        private String hotFixable;
        //如为true，默认添加此模块，默认为true，xml:autoApply
        private String autoApply;
        //xml:array，此元素是否可以添加,默认false
        private String isArray;
        //    //xml:reference → 引用节点标识，标识该节点不是根节点，而是被引用的节点，默认false
//    private String isReference;
        //xml:desc → 复杂字段描述标识，对应的值标识该配置项键的具体描述帮助信息
        private String desc;
        //xml:length → 长度的标识，对应的值有1、2、3和4，分别表示小、中、大、超大，默认是1
        private String length;
        //xml:frequent → 常用配置标识，对应的值为true和false，分别表示常用配置和基础配置，默认是false
        private String isFrequent;
        //定位标识
        private String tag;
        //最大可添加数量
        private String maxOccurs;
        //最小可添加数量
        private String minOccurs;
        //最小值，数值型
        private String minExclusive;
        //最大值，数值型
        private String maxInclusive;
        //最小长度，字符串
        private String minLength;
        //最大长度，字符串
        private String maxLength;


        public XsdElementBean() {
            setWebEditable("true");
            setWebNullable("true");
            setHotFixable("true");
            setAutoApply("true");
            setLength("1");
            setIsFrequent("false");
            setType("string");
            setChildren(new LinkedList<>());
        }
    }


    @Data
    static class EnumerationBean {
        private String name;
        private String value;
        private String options;
        private List<XsdElementBean> appInfo;
    }
}
