package pl.edu.uwb.ii.mobiuwb.models;

import android.webkit.URLUtil;


/**
 * Jest to klasa opisująca model strony WWW, w celu łatwiejszego przechowywania
 * ich w pogramie.
 */
public class WebsiteModel
{
	/**
	 * Jest to nazwa strony.
	 */
	private String name;
	/**
	 * Jest to URL strony.
	 */
	private String URL;
	/**
	 * Jest to adres strony głównej.
	 */
	private String ping;
	/**
	 * Jest to sprawdzenie, czy dany URL jest poprawny, czy też nie.
	 */
	private boolean isValid;
	
	
	/**
	 * Jest to główny konstruktor obiektu modelu strony WWW.
	 * 
	 * @param name
	 *            nazwa strony
	 * @param URL
	 *            URL strony
	 */
	public WebsiteModel(String name, String URL, String ping)
	{
		this.name = name;
		this.URL = URL;
		this.ping = ping;
		validate();
	}
	
	
	/**
	 * Metoda sprawdzająca, czy podany URL jest poprawny.
	 */
	private void validate()
	{
		isValid = URLUtil.isValidUrl(URL);
	}
	
	
	/**
	 * Metoda ta zwraca wartość, czy dany URL jest poprawny.
	 * 
	 * @return true, jeżeli poprawny, false jeżeli niepoprawny.
	 */
	public boolean isValid()
	{
		return isValid;
	}
	
	
	/**
	 * Getter ten służy do pobierania nazwy strony.
	 * 
	 * @return nazwa strony
	 */
	public String getName()
	{
		return name;
	}
	
	
	/**
	 * Getter ten służy do pobierania URL strony.
	 * 
	 * @return URL strony
	 */
	public String getURL()
	{
		return URL;
	}
	
	
	/**
	 * Getter ten służy do pobierania URL głównej strony.
	 * 
	 * @return URL głównej strony
	 */
	public String getPingURL()
	{
		return ping;
	}
	
	
	/**
	 * Wypisuje obiekt w formacie "nazwa".
	 * 
	 * @return nazwa strony
	 */
	@Override
	public String toString()
	{
		return getName();
	}
}
