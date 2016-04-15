package pl.edu.uwb.mobiuwb.parsers.xml.parser;

/**
 * Reprezentuje obiekt określający wysokość i szerokość geograficzną.
 */
public class Coordinates
{
    /**
     * Szerokość geograficzna.
     */
    private String lattitude;

    /**
     * Pobiera szerokość geograficzną.
     * @return Szerokość geograficzna.
     */
    public String getLattitude()
    {
        return lattitude;
    }

    /**
     * Nadaje szerokość geograficzną.
     * @param lattitude Szerokość geograficzna.
     */
    public void setLattitude(String lattitude)
    {
        this.lattitude = lattitude;
    }

    /**
     * Wysokość geograficzna.
     */
    private String longtitude;

    /**
     * Pobiera wysokość geograficzną.
     * @return Wysokość geograficzna.
     */
    public String getLongtitude()
    {
        return longtitude;
    }

    /**
     * Nadaje wysokość geograficzną.
     * @param longtitude Wysokość geograficzna.
     */
    public void setLongtitude(String longtitude)
    {
        this.longtitude = longtitude;
    }
}
