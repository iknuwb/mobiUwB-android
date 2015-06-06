package pl.edu.uwb.mobiuwb.parsers.json.model;

import java.util.Date;

/**
 * Created by Tunczyk on 2015-06-03.
 */
public class Feed
{
    String title;
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    String content;
    public String getContent()
    {
        return content;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    Date date;
    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Feed(String title, String content, Date date)
    {
        this.title = title;
        this.content = content;
        this.date = date;
    }
}
