package pl.edu.uwb.mobiuwb.parsers.xml.model;

/**
 * Jest to reprezentacja strony WWW w pliku XML Właściwości.
 */
public class Website
{
    /**
     * Nazwa strony WWW.
     */
    private String name;

    /**
     * Pobiera nazwę strony WWW.
     * @return Nazwa strony WWW.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Nadaje nazwę strony WWW.
     * @param name Nazwa strony WWW.
     */
    void setName(String name)
    {
        this.name = name;
    }

    /**
     * Jest to URL do strony WWW.
     */
    private String url;

    /**
     * Pobiera URL do strony WWW.
     * @return URL do strony WWW.
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Nadaje URL do strony WWW.
     * @param url URL do strony WWW.
     */
    void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * Jest to URL ping do strony WWW.
     */
    private String ping;

    /**
     * Pobiera URL ping do strony WWW.
     * @return URL ping do strony WWW.
     */
    public String getPing()
    {
        return ping;
    }

    /**
     * Nadaje URL ping do strony WWW.
     * @param ping URL ping do strony WWW.
     */
    void setPing(String ping)
    {
        this.ping = ping;
    }

    /**
     * Inicjalizuje pola w klasie.
     * @param name Nazwa.
     * @param url URL ping.
     * @param ping URL.
     */
    public Website(String name, String url, String ping)
    {
        this.setName(name);
        this.setUrl(url);
        this.setPing(ping);
    }

    /**
     * Pobiera haszkod. Jeśli dwie instancje mają różny haszkod,
     * to uważane są za różne.
     * @return Haszkod instancji.
     */
    @Override
    public int hashCode()
    {
        return 0;
    }

    /**
     * Porównuje dwa obiekty tego typu. Bazuje na nazwie.
     * @param o Inny obiekt tego typu.
     * @return True, jeśli są równe, false, jeśli nie.
     */
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Website)
        {
            Website that = (Website) o;
            return name.equals(that.name);
        }
        else
        {
            return false;
        }
    }
}
