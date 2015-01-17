package com.example.mobiuwb.xml.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import com.example.mobiuwb.MobiUwbApp;
import com.example.mobiuwb.io.IoManager;
import com.example.mobiuwb.xml.model.Website;
import com.example.mobiuwb.xml.model.XmlPropertiesRootElement;
import com.example.mobiuwb.xml.parser.result.model.PropertiesXmlResult;
import com.example.mobiuwb.xml.parser.result.validator.PropertiesXmlResultValidator;


/**
 * Jest to klasa odpowiedzialna za parsowanie XML'a.
 */
public class XMLParser
{
	
	private String readXmlFromInternalStorage(String xmlFileName)
			throws IOException
	{
		Log.d("MOBIUWB", "Reader: " + xmlFileName);
		
		String content = "";
		InputStream inputStream = 
				new FileInputStream(
						new File(
								MobiUwbApp.getContext().getFilesDir(),
								IoManager.PROPERTIES_XML_FILE_NAME));
		BufferedReader bufferedReader = 
				new BufferedReader(
						new InputStreamReader(inputStream)
				);
		String currentLine = null;
		currentLine = bufferedReader.readLine();
		while(currentLine != null)
		{
			content += currentLine;
			Log.d("MOBIUWB", currentLine);
			currentLine = bufferedReader.readLine();
		}
		bufferedReader.close();
		return content;
	}
	
	/**
	 * Jest to metoda zajmująca się parsowaniem XML'a ze stronami WWW. 
	 * Pobiera ona strony i zapisuje je do określonego modelu 
	 * przechowywania stron (WebsiteModel).
	 * @throws IOException //TODO
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 */
	public PropertiesXmlResult deserializePropertiesXml() 
					throws 
					IOException, 
					ParserConfigurationException, 
					SAXException
	{
		String xmlFileContent = 
				readXmlFromInternalStorage(
						IoManager.PROPERTIES_XML_FILE_NAME);
		List<Website> defaultWebsites = null;
		List<Website> websites = new ArrayList<Website>();
		InputSource XMLFileSource = new InputSource(
				new StringReader(xmlFileContent));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = dBuilder.parse(XMLFileSource);
		doc.getDocumentElement().normalize();
		websites = parseTag(doc,"Website");
		defaultWebsites = parseTag(doc,"DefaultWebsite");
		
		return preparePropertiesXmlResult(defaultWebsites, websites);
	}

	private List<Website> parseTag(
			Document doc, 
			String tagName) 
	{
		NodeList nodes;
		nodes = doc.getElementsByTagName(tagName);
		List<Website> websites = new ArrayList<Website>();
		for(int i = 0; i < nodes.getLength(); i++)
		{
			Node node = nodes.item(i);
			websites.add(parseNode(node));
		}
		return websites;
	}

	private Website parseNode(Node node) {
		Website website = null;
		String name = null;
		String url = null;
		String ping = null;
		if(node.getNodeType() == Node.ELEMENT_NODE)
		{
			Element element = (Element)node;
			if(element.hasAttribute("Name"))
			{
				name = element.getAttribute("Name");
			}
			if(element.hasAttribute("URL"))
			{
				url = element.getAttribute("URL");
			}
			if(element.hasAttribute("Ping"))
			{
				ping = element.getAttribute("Ping");
			}
			website = new Website(name,url,ping);
		}
		return website;
	}
	
	private PropertiesXmlResult preparePropertiesXmlResult(
			List<Website> defaultWebsites,
			List<Website> websites)
	{
		PropertiesXmlResultValidator validator = 
			new PropertiesXmlResultValidator();
		return validator.validate(defaultWebsites, websites);
	}
	
	/**
	 * Jest to metoda odpowiadająca za ustawienie nowej domyślnej strony WWW w
	 * XML'u.
	 * 
	 * @param newModel
	 *            nowy model do nadania jako domyślna strona WWW
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws TransformerException 
	 */
	public void serializeDefaultWebsite(Website defaultWebsite)
			throws IOException, 
				ParserConfigurationException, 
				SAXException, 
				TransformerException
	{
		InputStream inputStream = 
				new FileInputStream(
						new File(
								MobiUwbApp.getContext().getFilesDir(),
								IoManager.PROPERTIES_XML_FILE_NAME));InputSource XMLFileSource = new InputSource(inputStream);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(XMLFileSource);
		doc.getDocumentElement().normalize();
		NodeList nodes;
		nodes = doc.getElementsByTagName("DefaultWebsite");
		Node node = nodes.item(0);
		overwriteDefaultWebsite(defaultWebsite, node);
		
		TransformerFactory transformerFactory = 
				TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		
		StreamResult result = new StreamResult(
				new File(
						MobiUwbApp.getContext().getFilesDir(),
						IoManager.PROPERTIES_XML_FILE_NAME));
		transformer.transform(source, result);
	}

	private void overwriteDefaultWebsite(Website defaultWebsite, 
			Node node) 
	{
		if(node.getNodeType() == Node.ELEMENT_NODE)
		{
			Element element = (Element)node;
			if(element.hasAttribute("Name"))
			{
				element.setAttribute("Name", defaultWebsite.getName());
			}
			if(element.hasAttribute("URL"))
			{
				element.setAttribute("URL", defaultWebsite.getUrl());
			}
			if(element.hasAttribute("Ping"))
			{
				element.setAttribute("Ping", defaultWebsite.getPing());
			}
		}
	}
}
