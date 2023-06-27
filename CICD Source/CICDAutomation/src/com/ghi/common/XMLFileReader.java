package com.ghi.common;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLFileReader {

	public static void main(String argv[]) { 
	}
	 
	public static ArrayList<String > getMemebersForGivenName_TBD(String memberName) { 
		ArrayList <String> memberList = new ArrayList <String>();
		try {
			File file = new File("E:\\Gabilan\\may2020release\\FT\\package.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			
			NodeList typeList = doc.getElementsByTagName("types");
			for (int itr = 0; itr < typeList.getLength(); itr++) {
				
				
				Node typeNode = typeList.item(itr);
				if (typeNode.getNodeType() == Node.ELEMENT_NODE) {
					Element typeElement = (Element) typeNode;
					
					NodeList nameNodes = typeElement.getElementsByTagName("name");
					
					for (int it = 0; it < nameNodes.getLength(); it++) {
						Node nameNode = nameNodes.item(it);
						String name = nameNode.getTextContent();
						if (name != null && name.trim().equalsIgnoreCase(memberName)) {


							NodeList memberNodes = typeElement.getElementsByTagName("members");
							for (int i = 0; i < memberNodes.getLength(); i++) {
								Node memberNode = memberNodes.item(i);
								String member = memberNode.getTextContent();

								//System.out.println("member name: " + member);
								memberList.add(member);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Exception: "+ex.getMessage());
			return null;
		}
		return memberList;
	}
}
