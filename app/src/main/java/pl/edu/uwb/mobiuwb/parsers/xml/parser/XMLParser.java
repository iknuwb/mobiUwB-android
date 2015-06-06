package pl.edu.uwb.mobiuwb.parsers.xml.parser;

import android.util.Log;

import pl.edu.uwb.mobiuwb.MobiUwbApp;
import pl.edu.uwb.mobiuwb.io.IoManager;
import pl.edu.uwb.mobiuwb.utillities.Globals;
import pl.edu.uwb.mobiuwb.parsers.xml.model.Website;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.result.model.PropertiesXmlResult;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.result.model.UnitAddress;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.result.validator.PropertiesXmlResultValidator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static javax.xml.transform.OutputKeys.*;


/**
 * Jest to klasa odpowiedzialna za parsowanie XML'a.
 */
public class XMLParser
{

    private Document deserializeXml(File xmlFileName)
            throws
            IOException,
            ParserConfigurationException,
            SAXException
    {
        String xmlFileContent =
                readXmlFromInternalStorage(xmlFileName.getAbsolutePath());
        return deserializeXml(xmlFileContent);
    }

    private Document deserializeXml(String xmlFileContent)
            throws
            IOException,
            ParserConfigurationException,
            SAXException
    {
        InputSource XMLFileSource = new InputSource(
                new StringReader(xmlFileContent));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(XMLFileSource);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private String readXmlFromInternalStorage(String xmlFileFullPath)
            throws IOException
    {
        String content = "";
        InputStream inputStream =
                new FileInputStream(
                        new File(xmlFileFullPath));
        BufferedReader bufferedReader =
                new BufferedReader(
                        new InputStreamReader(inputStream)
                );
        String currentLine = null;
        currentLine = bufferedReader.readLine();
        while (currentLine != null)
        {
            content += currentLine;
            currentLine = bufferedReader.readLine();
        }
        bufferedReader.close();
        return content;
    }

    /**
     * Jest to metoda zajmująca się parsowaniem XML'a ze stronami WWW.
     * Pobiera ona strony i zapisuje je do określonego modelu
     * przechowywania stron (WebsiteModel).
     *
     * @throws IOException                  //TODO
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public PropertiesXmlResult deserializePropertiesXml(String fullFilePath)
            throws
            IOException,
            ParserConfigurationException,
            SAXException
    {
        File file = new File(fullFilePath);
        return deserializePropertiesXml(file);
    }

    public PropertiesXmlResult deserializePropertiesXml()
            throws
            IOException,
            ParserConfigurationException,
            SAXException
    {
        File file = new File(MobiUwbApp.getContext().getFilesDir().getPath(),
                             IoManager.PROPERTIES_XML_FILE_NAME);
        return deserializePropertiesXml(file);
    }


    public PropertiesXmlResult deserializePropertiesXml(File file)
            throws
            IOException,
            ParserConfigurationException,
            SAXException
    {
        List<Website> defaultWebsites = null;
        String configurationFilePath = null;
        List<Website> websites = new ArrayList<Website>();

        Document doc = deserializeXml(file);

        websites = parseWebsitesTag(doc, "Website");
        defaultWebsites = parseWebsitesTag(doc, "DefaultWebsite");
        configurationFilePath = parseConfigurationTag(doc, "Configuration");
        return preparePropertiesXmlResult(defaultWebsites, websites, configurationFilePath);
    }


    public static void prettyPrint(Document xml) throws Exception
    {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(ENCODING, "UTF-8");
        tf.setOutputProperty(INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        Log.d(Globals.MOBIUWB_TAG, out.toString());
    }


    public ConfigXmlResult deserializeConfigurationXml()
            throws
            IOException,
            ParserConfigurationException,
            SAXException
    {
        File file = new File(MobiUwbApp.getContext().getFilesDir().getPath(),
                             IoManager.CONFIGURATION_XML_FILE_NAME);
        Document doc = deserializeXml(file);
        return deserializeConfigurationXml(doc);
    }

    public ConfigXmlResult deserializeConfigurationXml(String fileContent)
            throws
            IOException,
            ParserConfigurationException,
            SAXException
    {
        Document doc = deserializeXml(fileContent);
        return deserializeConfigurationXml(doc);
    }

    public ConfigXmlResult deserializeConfigurationXml(Document document)
            throws
            IOException,
            ParserConfigurationException,
            SAXException
    {
        ConfigXmlResult result = new ConfigXmlResult();

        parseUniversityUnits(document, result);
        parseAuthors(document, result);
        return result;
    }


    private void parseAuthors(Document doc, ConfigXmlResult result)
    {
        NodeList tomaszukNodes = doc.getElementsByTagName("opiekun");
        Node tomaszuk = tomaszukNodes.item(0);
        if (tomaszuk.getNodeType() == Node.ELEMENT_NODE)
        {
            Element tomaszukElement = (Element) tomaszuk;
            result.authors.add(tomaszukElement.getFirstChild().getNodeValue() + " (opiekun)");
        }

        NodeList authorsNodes = doc.getElementsByTagName("autorzy");
        Node authorsNode = authorsNodes.item(0);
        NodeList authorNodes = authorsNode.getChildNodes();
        for (int i = 0; i < authorNodes.getLength(); i++)
        {
            Node authorNode = authorNodes.item(i);
            if (authorNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element authorElement = (Element)authorNode;
                if (authorElement.getTagName().equals("autor"))
                {
                    result.authors.add(authorElement.getFirstChild().getNodeValue());
                }
            }
        }
    }

    private void parseUniversityUnits(Document doc, ConfigXmlResult result)
    {

        NodeList units = doc.getElementsByTagName("jednostka");
        for (int i = 0; i < units.getLength(); i++)
        {
            Node unitNode = units.item(i);
            if (unitNode.getNodeType() == Node.ELEMENT_NODE)
            {
                UniversityUnit unit = new UniversityUnit();
                NodeList unitNodeMembers = unitNode.getChildNodes();
                for (int j = 0; j < unitNodeMembers.getLength(); j++)
                {
                    Node unitNodeMember = unitNodeMembers.item(j);
                    if (unitNodeMember.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element unitElementMember = (Element) unitNodeMember;
                        if (unitElementMember.getTagName().equals("nazwa"))
                        {
                            unit.setName(unitElementMember.getFirstChild().getNodeValue());
                        }
                        else if (unitElementMember.getTagName().equals("pelny_tytul"))
                        {
                            unit.setFullName(unitElementMember.getFirstChild().getNodeValue());
                        }
                        else if (unitElementMember.getTagName().equals("apiUrl"))
                        {
                            unit.setApiUrl(unitElementMember.getFirstChild().getNodeValue());
                        }
                        else if (unitElementMember.getTagName().equals("sekcje"))
                        {
                            parseSectionsTag(unitElementMember,unit.getSections());
                        }
                        else if (unitElementMember.getTagName().equals("sekcje_statyczne"))
                        {
                            parseStaticSectionsTag(unitElementMember, unit.getStaticSections());
                        }
                        else if(unitElementMember.getTagName().equals("adres"))
                        {
                            unit.setAddress(new UnitAddress());
                            parseUnitAddress(unitElementMember,unit.getAddress());
                        }
                        else if(unitElementMember.getTagName().equals("email"))
                        {
                            unit.setEmail(unitElementMember.getFirstChild().getNodeValue());
                        }
                        else if(unitElementMember.getTagName().equals("fax"))
                        {
                            unit.setFax(unitElementMember.getFirstChild().getNodeValue());
                        }
                        else if(unitElementMember.getTagName().equals("tel1"))
                        {
                            unit.setTel1(unitElementMember.getFirstChild().getNodeValue());
                        }
                        else if(unitElementMember.getTagName().equals("tel2"))
                        {
                            unit.setTel2(unitElementMember.getFirstChild().getNodeValue());
                        }
                        else if(unitElementMember.getTagName().equals("mapa"))
                        {
                            unit.setMobiUwBMap(new MobiUwBMap());
                            parseMobiUwBMap(unitElementMember, unit.getMobiUwBMap());
                        }
                    }
                }
                result.universityUnits.add(unit);
            }
        }
    }

    private void parseMobiUwBMap(Element unitElementMember, MobiUwBMap mobiUwBMap)
    {
        NodeList nodeList = unitElementMember.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node unitNode = nodeList.item(i);
            if (unitNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element unitElement = (Element)unitNode;
                if (unitElement.getTagName().equals("tytul"))
                {
                    mobiUwBMap.setTitle(unitElement.getFirstChild().getNodeValue());
                }
                else if (unitElement.getTagName().equals("wspolrzedne"))
                {
                    mobiUwBMap.setCoordinates(new Coordinates());
                    parseCoordinates(unitElement, mobiUwBMap.getCoordinates());
                }
            }
        }
    }

    private void parseCoordinates(Element unitElementMember, Coordinates coordinates)
    {
        NodeList nodeList = unitElementMember.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node unitNode = nodeList.item(i);
            if (unitNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element unitElement = (Element)unitNode;
                if (unitElement.getTagName().equals("szerokosc"))
                {
                    coordinates.setLattitude(unitElement.getFirstChild().getNodeValue());
                }
                else if (unitElement.getTagName().equals("dlugosc"))
                {
                    coordinates.setLongtitude(unitElement.getFirstChild().getNodeValue());
                }
            }
        }
    }

    private void parseUnitAddress(Element unitElementMember, UnitAddress address)
    {
        NodeList nodeList = unitElementMember.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node unitAddressNode = nodeList.item(i);
            if (unitAddressNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element unitAddressElement = (Element)unitAddressNode;
                if (unitAddressElement.getTagName().equals("miasto"))
                {
                    address.setCity(unitAddressElement.getFirstChild().getNodeValue());
                }
                else if (unitAddressElement.getTagName().equals("kod"))
                {
                    address.setPostalCode(unitAddressElement.getFirstChild().getNodeValue());
                }
                else if (unitAddressElement.getTagName().equals("ulica"))
                {
                    address.setStreet(unitAddressElement.getFirstChild().getNodeValue());
                }
                else if (unitAddressElement.getTagName().equals("numer"))
                {
                    address.setNumber(unitAddressElement.getFirstChild().getNodeValue());
                }
            }
        }
    }

    private void parseStaticSectionsTag(Element sectionsElement, List<Section> staticSections)
    {
        NodeList sections = sectionsElement.getChildNodes();
        for (int i = 0; i < sections.getLength(); i++)
        {
            Node section = sections.item(i);
            if(section.getNodeType() == Node.ELEMENT_NODE)
            {
                Section sectionObject = new Section();
                NodeList sectionMembers = section.getChildNodes();
                Element sectionElement = (Element) section;
                for (int j = 0; j < sectionMembers.getLength(); j++)
                {
                    Node sectionMember = sectionMembers.item(j);
                    if (sectionMember.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element sectionInnerElement = (Element) sectionMember;
                        if (sectionInnerElement.getTagName().equals("tytul_sekcji"))
                        {
                            sectionObject.title = sectionInnerElement.getFirstChild().getNodeValue();
                        }
                        else if (sectionInnerElement.getTagName().equals("id_sekcji"))
                        {
                            sectionObject.id = sectionInnerElement.getFirstChild().getNodeValue();
                        }
                        else if (sectionInnerElement.hasAttribute("powiadomienia"))
                        {
                            String takLubNie = sectionInnerElement.getAttribute("powiadomienia");
                            sectionObject.notificate = Boolean.valueOf(takLubNie);
                        }
                        else if (sectionInnerElement.hasAttribute("licznik"))
                        {
                            String takLubNie = sectionInnerElement.getAttribute("licznik");
                            sectionObject.counter = Boolean.valueOf(takLubNie);
                        }
                    }
                }
                if (sectionElement.hasAttribute("powiadomienia"))
                {
                    String takLubNie = sectionElement.getAttribute("powiadomienia");
                    sectionObject.notificate = Boolean.valueOf(takLubNie);
                }
                else if (sectionElement.hasAttribute("licznik"))
                {
                    String takLubNie = sectionElement.getAttribute("licznik");
                    sectionObject.counter = Boolean.valueOf(takLubNie);
                }
                staticSections.add(sectionObject);
            }
        }
    }

    private void parseSectionsTag(Element sectionsElement, List<Section> normalSections)
    {
        NodeList sections = sectionsElement.getChildNodes();
        for (int i = 0; i < sections.getLength(); i++)
        {
            Node section = sections.item(i);
            if(section.getNodeType() == Node.ELEMENT_NODE)
            {
                Element sectionElement = (Element)section;
                Section sectionObject = new Section();
                NodeList sectionMembers = section.getChildNodes();
                for (int j = 0; j < sectionMembers.getLength(); j++)
                {
                    Node sectionMember = sectionMembers.item(j);
                    if(sectionMember.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element sectionInnerElement = (Element) sectionMember;
                        if (sectionInnerElement.getTagName().equals("tytul_sekcji"))
                        {
                            sectionObject.title = sectionInnerElement.getFirstChild().getNodeValue();
                        }
                        else if (sectionInnerElement.getTagName().equals("id_sekcji"))
                        {
                            sectionObject.id = sectionInnerElement.getFirstChild().getNodeValue();
                        }
                    }
                }
                if (sectionElement.hasAttribute("powiadomienia"))
                {
                    String takLubNie = sectionElement.getAttribute("powiadomienia");
                    sectionObject.notificate = Boolean.valueOf(takLubNie);
                }
                else if (sectionElement.hasAttribute("licznik"))
                {
                    String takLubNie = sectionElement.getAttribute("licznik");
                    sectionObject.counter = Boolean.valueOf(takLubNie);
                }
                normalSections.add(sectionObject);
            }
        }
    }

    private String parseConfigurationTag(Document doc, String tagName)
    {
        NodeList nodes = doc.getElementsByTagName(tagName);
        String configurationFilePath = null;
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                if (element.hasAttribute("File"))
                {
                    configurationFilePath = element.getAttribute("File");
                }
            }
        }
        return configurationFilePath;
    }

    private List<Website> parseWebsitesTag(
            Document doc,
            String tagName)
    {
        NodeList nodes;
        nodes = doc.getElementsByTagName(tagName);
        List<Website> websites = new ArrayList<Website>();
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            websites.add(parseWebsiteNode(node));
        }
        return websites;
    }

    private Website parseWebsiteNode(Node node)
    {
        Website website = null;
        String name = null;
        String url = null;
        String ping = null;
        if (node.getNodeType() == Node.ELEMENT_NODE)
        {
            Element element = (Element) node;
            if (element.hasAttribute("Name"))
            {
                name = element.getAttribute("Name");
            }
            if (element.hasAttribute("URL"))
            {
                url = element.getAttribute("URL");
            }
            if (element.hasAttribute("Ping"))
            {
                ping = element.getAttribute("Ping");
            }
            website = new Website(name, url, ping);
        }
        return website;
    }

    private PropertiesXmlResult preparePropertiesXmlResult(
            List<Website> defaultWebsites,
            List<Website> websites,
            String configurationFilePath)
    {
        PropertiesXmlResultValidator validator =
                new PropertiesXmlResultValidator();
        return validator.validate(defaultWebsites, websites, configurationFilePath);
    }

    /**
     * Jest to metoda odpowiadająca za ustawienie nowej domyślnej strony WWW w
     * XML'u.
     *
     * @param defaultWebsite nowy model do nadania jako domyślna strona WWW
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
                                IoManager.PROPERTIES_XML_FILE_NAME));
        InputSource XMLFileSource = new InputSource(inputStream);
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
        if (node.getNodeType() == Node.ELEMENT_NODE)
        {
            Element element = (Element) node;
            if (element.hasAttribute("Name"))
            {
                element.setAttribute("Name", defaultWebsite.getName());
            }
            if (element.hasAttribute("URL"))
            {
                element.setAttribute("URL", defaultWebsite.getUrl());
            }
            if (element.hasAttribute("Ping"))
            {
                element.setAttribute("Ping", defaultWebsite.getPing());
            }
        }
    }
}
