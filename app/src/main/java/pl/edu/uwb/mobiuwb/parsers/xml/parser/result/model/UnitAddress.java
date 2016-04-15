package pl.edu.uwb.mobiuwb.parsers.xml.parser.result.model;

/**
 * Reprezentuje adres jednostki Uniwersytetu w Białymstoku.
 */
public class UnitAddress
{
    /**
     * Kod pocztowy.
     */
    String postalCode;

    /**
     * Pobiera kod pocztowy.
     * @return Kod pocztowy.
     */
    public String getPostalCode()
    {
        return postalCode;
    }

    /**
     * Nadaje kod pocztowy.
     * @param postalCode Kod pocztowy.
     */
    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    /**
     * Miasto.
     */
    String city;

    /**
     * Pobiera miasto.
     * @return Miasto.
     */
    public String getCity()
    {
        return city;
    }

    /**
     * Nadaje miasto.
     * @param city Miasto.
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * Ulica.
     */
    String street;
    public String getStreet()
    {
        return street;
    }
    public void setStreet(String street)
    {
        this.street = street;
    }

    /**
     * Numer budynku.
     */
    String number;

    /**
     * Pobiera numer budynku.
     * @return Numer budynku.
     */
    public String getNumber()
    {
        return number;
    }

    /**
     * Nadaje numer budynku.
     * @param number Numer budynku.
     */
    public void setNumber(String number)
    {
        this.number = number;
    }

}
