package pl.edu.uwb.mobiuwb.view.model;

import android.graphics.drawable.Drawable;

import pl.edu.uwb.mobiuwb.parsers.xml.model.Website;

public class DrawerListAdapterModel
{

    Drawable icon;

    public Drawable getIcon()
    {
        return icon;
    }

    public void setIcon(Drawable icon)
    {
        this.icon = icon;
    }

    String text;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    private long id;

    public long getId()
    {
        return id;
    }

    private Website website;

    public Website getWebsite()
    {
        return website;
    }

    public DrawerListAdapterModel(Drawable icon, Website website, long id)
    {
        this.icon = icon;
        this.text = website.getName();
        this.website = website;
        this.id = id;
    }
}
