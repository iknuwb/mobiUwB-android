package pl.edu.uwb.mobiuwb.parsers.xml.parser.result.model;

/**
 * Created by sennajavie on 2015-05-15.
 */
public class UnitAddress
{
    String postalCode;
    public String getPostalCode()
    {
        return postalCode;
    }
    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    String city;
    public String getCity()
    {
        return city;
    }
    public void setCity(String city)
    {
        this.city = city;
    }

    String street;
    public String getStreet()
    {
        return street;
    }
    public void setStreet(String street)
    {
        this.street = street;
    }

    String number;
    public String getNumber()
    {
        return number;
    }
    public void setNumber(String number)
    {
        this.number = number;
    }

}
