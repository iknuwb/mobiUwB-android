package pl.edu.uwb.mobiuwb.connection;

/**
 * Created by Tunczyk on 2015-05-05.
 */
public enum CheckingType
{
    WIFI,
    MOBILE,
    NONE

    /*
    private final int value;
    public int getValue()
    {
        return value;
    }

    private boolean isAvailable;
    public boolean isAvailable()
    {
        return isAvailable;
    }
    public void setIsAvailable(boolean isAvailable)
    {
        this.isAvailable = isAvailable;
    }

    private InternetChecker internetChecker;
    public InternetChecker getInternetChecker()
    {
        return internetChecker;
    }
    private void setInternetChecker(InternetChecker internetChecker)
    {
        this.internetChecker = internetChecker;
    }

    CheckingType(int value)
    {
        this.value = value;
        init(value);
    }

    public void init(int value)
    {
        //TODO switch
        if(value == 0)
        {
            setInternetChecker(new WiFiChecker());
            isAvailable = getInternetChecker().isAvailable();
        }
        else if(value == 1)
        {
            setInternetChecker(new MobileChecker());
            isAvailable = getInternetChecker().isAvailable();
        }
        else if(value == 2)
        {
            setInternetChecker(new WifiMobileChecker());
            isAvailable = getInternetChecker().isAvailable();
        }
        else
        {
            throw new AssertionError("Not implemented CheckingType.");
        }
    }
    */
}
