package com.merlin.utils;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.aventstack.extentreports.Status;
import com.merlin.common.Util;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Xml_Parsor {

	public static String Get_Value_Of_Attribute_In_Xml(String file_Path, String xpath_Expression, String attribute_Name) throws Exception  
    {
//		attribute_Value= "source";
        String fileName = file_Path ;
        Document document =     getDocument(new File(fileName));
//        String xpathExpression = "/document/spreads/spread/page/pageitem[@id='1037']";
        String attribute_Value = evaluateXPath(document, xpath_Expression, attribute_Name) ;
        Assert.assertTrue(!(attribute_Value.isEmpty() || attribute_Value == null) ,"Unable to parse xml file for attribute - "+ attribute_Name);
        return attribute_Value;
    }
	
	private static String evaluateXPath(Document document, String xpathExpression , String attribute_Value) throws Exception 
    {
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        String value = "";
        try
        {
            XPathExpression expr = xpath.compile(xpathExpression);
            Node node = (Node) expr.evaluate(document, XPathConstants.NODE);
            value = node.getAttributes().getNamedItem(attribute_Value).getNodeValue();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return value;
    }
	
	public static String Get_Value_Of_Tag_From_Xml(String file_Path, String xpath_Expression) throws Exception {
		String fileName = file_Path;
		Document document = getDocument(new File(fileName));
		String tag_Value = evaluateXPath(document, xpath_Expression+"/text()");
		Assert.assertTrue(!(tag_Value.isEmpty() || tag_Value == null),
				"Unable to parse xml file for tag - " + tag_Value);
		return tag_Value;
	}

	private static String evaluateXPath(Document document, String xpathExpression) throws Exception {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		String value = "";
		try {
			XPathExpression expr = xpath.compile(xpathExpression);
			value = (String) expr.evaluate(document, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return value;
	}
 
    private static Document getDocument(File fileName) throws Exception 
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fileName);
        return doc;
    }

    public static ArrayList<String> Get_list_Of_Attribute_tag_In_Xml(String file_Path, String tag_name, String attribute) throws Exception
    {
        ArrayList<String> varlinks= new ArrayList<>();
        String fileName = file_Path ;
        Document document =     getDocument(new File(fileName));
        int length = document.getElementsByTagName(tag_name).getLength();
        for(int  i=0; i<length; i++){
            varlinks.add(document.getElementsByTagName(tag_name).item(i).getAttributes().getNamedItem(attribute).getNodeValue());}
        Assert.assertTrue(!varlinks.isEmpty(), "No list fetched from xml");
      return varlinks;
    }

    public static ArrayList<String> Get_list_Of_Attribute_tag_In_Xml(String file_Path, String frame_Type) throws Exception
    {
        ArrayList<String> varlinks= new ArrayList<>();
        ArrayList<String> varlinks_2= new ArrayList<>();
        String fileName = file_Path ;
        Document document =     getDocument(new File(fileName));
        int length = document.getElementsByTagName("pageitem").getLength();
        for(int  j=0; j<length; j++) {
            switch (frame_Type) {
                case "Shape":
                    if (document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("Shape")) {
                        varlinks.add(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());
                    }
                break;
                case "Image":
                    if (document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("Image")) {
                        varlinks.add(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());
                    }
                break;
                case "Table_with_Anchored":
                   try {
                       if (document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("anchored").getNodeValue()==null)
                           System.out.println(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());;
                   }catch (Exception e) {
                   continue;
                   }
                   if(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("anchored").getNodeValue().equalsIgnoreCase("true")) {
                           if(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("Table")) {
                               varlinks.add(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());
                           }
                       }
                break;
                case "TextFrame_With_Anchored":
                    try {
                        if (document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("anchored").getNodeValue()==null)
                            System.out.println(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());;
                    }catch (Exception e) {
                        continue;
                    }
                    if(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("anchored").getNodeValue().equalsIgnoreCase("true")) {
                        if(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("TextFrame")) {
                            varlinks.add(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());
                        }
                    }
                break;
                case "TextFrame":
                    if (document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("TextFrame")) {
                        varlinks.add(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());
                    }
                    try {
                        if (document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("anchored").getNodeValue()==null)
                            System.out.println(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());;
                    }catch (Exception e) {
                        continue;
                    }
                    if(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("anchored").getNodeValue().equalsIgnoreCase("true")) {
                        if(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("TextFrame")) {
                            varlinks_2.add(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());
                        }
                    }
                    break;
                case "Table_Without_Anchored":
                    if (document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("Table")) {
                        varlinks.add(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());
                    }
                    try {
                        if (document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("anchored").getNodeValue()==null)
                            System.out.println(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());;
                    }catch (Exception e) {
                        continue;
                    }
                    if(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("anchored").getNodeValue().equalsIgnoreCase("true")) {
                        if(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("Table")) {
                            varlinks_2.add(document.getElementsByTagName("pageitem").item(j).getAttributes().getNamedItem("id").getNodeValue());
                        }
                    }
                    break;
            }
        }
        varlinks.removeAll(varlinks_2);
        Assert.assertTrue(!varlinks.isEmpty(), "No list fetched from xml");
        return varlinks;


    }
}
