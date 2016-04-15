package pl.edu.uwb.mobiuwb.parsers.json.model;

import java.util.Date;

/**
 * Jest to pojedynczy kanał informacyjny pochodzący z JSON-a.
 */
public class Feed
{
    /**
     * Tytuł.
     */
    String title;

    /**
     * Pobiera tytuł.
     * @return Tytuł.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Nadaje tytuł.
     * @param title Tytuł do nadania.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Reprezentuje zawartość.
     */
    String content;

    /**
     * Pobiera zawartość.
     * @return Zawartość.
     */
    public String getContent()
    {
        return content;
    }

    /**
     * Nadaje zawartość.
     * @param content Zawartość.
     */
    public void setContent(String content)
    {
        this.content = content;
    }

    /**
     * Data.
     */
    Date date;

    /**
     * Pobiera datę.
     * @return Data.
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Nadaje datę.
     * @param date Data do nadania.
     */
    public void setDate(Date date)
    {
        this.date = date;
    }

    /**
     * Tworzy obiekt. Inicjuje zmienne.
     * @param title Tytuł.
     * @param content Zawartość.
     * @param date Data.
     */
    public Feed(String title, String content, Date date)
    {
        this.title = title;
        this.content = content;
        this.date = date;
    }
}
