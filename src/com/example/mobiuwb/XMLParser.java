package com.example.mobiuwb;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Jest to klasa odpowiedzialna za parsowanie XML'a.
 */
public class XMLParser 
{
	/**
	 * Jest to zmienna przechowuj¹ca aktualny plik XML do parsowania.
	 */
	public static String currentXML;
	
	/**
	 * Jest to metoda zajmuj¹ca siê parsowaniem XML'a ze stronami WWW.
	 * Pobiera ona strony i zapisuje je do okreœlonego modelu przechowywania stron (WebsiteModel).
	 */
    @SuppressWarnings("unused")
	public static void getWebsitesFromXML()
    {
        InputSource XMLFileSource= new InputSource(new StringReader(currentXML));
        try 
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(XMLFileSource);
            doc.getDocumentElement().normalize();
            NodeList nodes;
            nodes = doc.getElementsByTagName("Website");
            for (int i = 0; i < nodes.getLength(); i++) 
            {
                String name = null;
                String URL = null;
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) 
                {
                    if(node.getNodeName().equals("Website"))
                    {
                        Element element = (Element)node;
                        if(element.hasAttribute("Name"))
                        {
                            name = element.getAttribute("Name");
                        }
                        if(element.hasAttribute("URL"))
                        {
                            URL = element.getAttribute("URL");
                        }
                        MainActivity.webSites.add(new WebsiteModel(name, URL));
                    }
                }
            }
            nodes = doc.getElementsByTagName("DefaultWebsite");
            for (int i = 0; i < nodes.getLength(); i++) 
            {
                String name = null;
                String URL = null;
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) 
                {
                    if(node.getNodeName().equals("DefaultWebsite"))
                    {
                        Element element = (Element)node;
                        if(element.hasAttribute("Name"))
                        {
                            name = element.getAttribute("Name");
                        }
                        if(element.hasAttribute("URL"))
                        {
                            URL = element.getAttribute("URL");
                        }
                        MainActivity.webMod = new WebsiteModel(name, URL);
                    }
                }
                break;
            }
        } 
        catch (Exception ex) 
        {
        }
    }
    
    /**
     * Jest to metoda odpowiadaj¹ca za ustawienie nowej domyœlnej strony WWW w XML'u.
     * @param newModel nowy model do nadania jako domyœlna strona WWW
     */
    @SuppressWarnings("unused")
	public static void setNewDefaultWebsite(WebsiteModel newModel)
    {
        InputSource XMLFileSource= new InputSource(new StringReader(currentXML));
        try 
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(XMLFileSource);
            doc.getDocumentElement().normalize();
            NodeList nodes;
            nodes = doc.getElementsByTagName("DefaultWebsite");
            for (int i = 0; i < nodes.getLength(); i++) 
            {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) 
                {
                    if(node.getNodeName().equals("DefaultWebsite"))
                    {
                        Element element = (Element)node;
                        if(element.hasAttribute("Name"))
                        {
                            element.setAttribute("Name", newModel.getName());
                        }
                        if(element.hasAttribute("URL"))
                        {
                        	element.setAttribute("URL", newModel.getURL());
                        }
                    }
                }
                break;
            }
        } 
        catch (Exception ex) 
        {
        }
    }
    
    /**
     * Jest to konstruktor prywatny s³u¿¹cy do zapobiegania tworzeniu instancji tej klasy.
     */
    private XMLParser() 
    {
    }
}

