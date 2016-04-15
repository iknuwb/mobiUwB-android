package pl.edu.uwb.mobiuwb.view.model;

import android.graphics.drawable.Drawable;

import pl.edu.uwb.mobiuwb.parsers.xml.model.Website;

/**
 * Model dla elementu z adaptera z bocznego wysuwanego menu.
 */
public class DrawerListAdapterModel
{

    /**
     * Ikonka.
     */
    Drawable icon;

    /**
     * Pobiera ikonkę.
     * @return Ikona.
     */
    public Drawable getIcon()
    {
        return icon;
    }

    /**
     * Nadaje ikonę.
     * @param icon Ikona.
     */
    public void setIcon(Drawable icon)
    {
        this.icon = icon;
    }

    /**
     * Wyświetlany tekst.
     */
    String text;

    /**
     * Pobiera wyświetlany tekst.
     * @return Wyświetlany tekst.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Nadaje wyświetlany tekst.
     * @param text Wyświetlany tekst.
     */
    public void setText(String text)
    {
        this.text = text;
    }

    /**
     * Identyfikator.
     */
    private long id;

    /**
     * Pobiera identyfikator.
     * @return Identyfikator.
     */
    public long getId()
    {
        return id;
    }

    /**
     * Strona WWW.
     */
    private Website website;

    /**
     * Pobiera stronę WWW.
     * @return Strona WWW.
     */
    public Website getWebsite()
    {
        return website;
    }

    /**
     * Nadaje zmienne.
     * @param icon Ikona.
     * @param website Strona WWW.
     * @param id Identyfikator.
     */
    public DrawerListAdapterModel(Drawable icon, Website website, long id)
    {
        this.icon = icon;
        this.text = website.getName();
        this.website = website;
        this.id = id;
    }
}
