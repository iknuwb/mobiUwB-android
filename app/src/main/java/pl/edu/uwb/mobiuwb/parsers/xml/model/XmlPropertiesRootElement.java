package pl.edu.uwb.mobiuwb.parsers.xml.model;

import java.util.List;

public class XmlPropertiesRootElement
{
    private String configurationFilePath;
    public String getConfigurationFilePath()
    {
        return configurationFilePath;
    }
    public void setConfigurationFilePath(String configurationFilePath)
    {
        this.configurationFilePath = configurationFilePath;
    }

    private Website defaultWebsite;

    public Website getDefaultWebsite()
    {
        return defaultWebsite;
    }

    public void setDefaultWebsite(Website defaultWebsite)
    {
        this.defaultWebsite = defaultWebsite;
    }

    public List<Website> websites;

    public XmlPropertiesRootElement(Website defaultWebsite,
                                    List<Website> websites,
                                    String configurationFilePath)
    {
        this.defaultWebsite = defaultWebsite;
        this.websites = websites;
        this.configurationFilePath = configurationFilePath;
    }

    /**
     * Metoda wyszukująca indexu defaultowego obiektu w liście.
     *
     * @return wartość >= 0 jeżeli znaleziono, -1 w przeciwnym przypadku
     */
    public int getDefaultWebsiteIndex()
    {
        if (defaultWebsite != null && websites != null)
        {
            for (int i = 0; i < websites.size(); i++)
            {
                Website item = websites.get(i);

                if (item.equals(defaultWebsite))
                {
                    return i;
                }
            }
        }
        return -1;
    }
}
