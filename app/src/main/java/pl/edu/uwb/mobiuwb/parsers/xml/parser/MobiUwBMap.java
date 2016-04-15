package pl.edu.uwb.mobiuwb.parsers.xml.parser;

/**
 * Reprezentuje obiekt mapy w aplikacji.
 */
public class MobiUwBMap
{
    /**
     * Tytuł.
     */
    private String title;

    /**
     * Pobiera tytuł.
     * @return Tytuł
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Nadaje tytuł.
     * @param title Tytuł.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Koordynaty na mapie.
     */
    private Coordinates coordinates;

    /**
     * Pobiera koordynaty na mapie.
     * @return Koordynaty na mapie.
     */
    public Coordinates getCoordinates()
    {
        return coordinates;
    }

    /**
     * Nadaje koordynaty na mapie.
     * @param coordinates Koordynaty na mapie.
     */
    public void setCoordinates(Coordinates coordinates)
    {
        this.coordinates = coordinates;
    }
}
