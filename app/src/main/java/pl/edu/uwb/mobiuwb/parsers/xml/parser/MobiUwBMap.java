package pl.edu.uwb.mobiuwb.parsers.xml.parser;

/**
 * Created by sennajavie on 2015-05-16.
 */
public class MobiUwBMap
{
    private String title;
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    private Coordinates coordinates;
    public Coordinates getCoordinates()
    {
        return coordinates;
    }
    public void setCoordinates(Coordinates coordinates)
    {
        this.coordinates = coordinates;
    }
}
