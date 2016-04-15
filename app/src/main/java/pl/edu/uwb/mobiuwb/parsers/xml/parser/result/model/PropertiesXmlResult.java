package pl.edu.uwb.mobiuwb.parsers.xml.parser.result.model;

import pl.edu.uwb.mobiuwb.parsers.xml.model.XmlPropertiesRootElement;

import java.util.List;

/**
 * Jest to to, co zostanie zwrócone po przeanalizowaniu pliku Właściwości
 * przez analizator do XML.
 */
public class PropertiesXmlResult
{
    /**
     * Czy jest poprawny.
     */
    private boolean valid;

    /**
     * Czy jest poprawny.
     */
    public boolean isValid()
    {
        return valid;
    }

    /**
     * Nadaje, czy jest poprawny.
     */
    public void setValid(boolean valid)
    {
        this.valid = valid;
    }

    /**
     * Element bazowy struktury XML.
     */
    private XmlPropertiesRootElement xmlPropertiesRootElement;

    /**
     * Pobiera element bazowy struktury.
     * @return Element bazowy struktury XML.
     */
    public XmlPropertiesRootElement getXmlPropertiesRootElement()
    {
        return xmlPropertiesRootElement;
    }

    /**
     * Nadaje element bazowy struktury XML.
     * @param xmlPropertiesRootElement Element bazowy struktury XML.
     */
    public void setXmlPropertiesRootElement(
            XmlPropertiesRootElement xmlPropertiesRootElement)
    {
        this.xmlPropertiesRootElement = xmlPropertiesRootElement;
    }

    /**
     * Lista ewentualnych komunikatów o błędach.
     */
    public List<String> errors;

    /**
     * Nadaje wartości zmiennym.
     * Zakłada, że mamy do czynienia z poprawnym obiektem.
     * @param xmlPropertiesRootElement Element bazowy struktury XML.
     */
    public PropertiesXmlResult(
            XmlPropertiesRootElement xmlPropertiesRootElement)
    {
        this.valid = true;
        this.xmlPropertiesRootElement = xmlPropertiesRootElement;
    }

    /**
     * Zwraca zrozumiałą reprezentację tego obiektu w formie ciągu znaków.
     * @return Reprezentacja tego obiektu w formie ciągu znaków.
     */
    @Override
    public String toString()
    {
        return "PropertiesXmlResult [valid=" + valid
                + ", xmlPropertiesRootElement=" + xmlPropertiesRootElement
                + ", errors=" + errors + "]";
    }
}
