package com.example.mobiuwb;

import android.webkit.URLUtil;

/**
 * Jest to klasa opisuj¹ca model strony WWW, w celu ³atwiejszego przechowywania ich w pogramie.
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
	 * Jest to sprawdzenie, czy dany URL jest poprawny, czy te¿ nie.
	 */
	private boolean isValid;
	
	/**
	 * Jest to g³ówny konstruktor obiektu modelu strony WWW.
	 * @param name nazwa strony
	 * @param URL URL strony
	 */
	public WebsiteModel(String name, String URL)
	{
		this.name = name;
		this.URL = URL;
		validate();
	}
	
	/**
	 * Metoda sprawdzaj¹ca, czy podany URL jest poprawny.
	 */
	private void validate()
	{
		isValid = URLUtil.isValidUrl(URL);
	}
	
	/**
	 * Metoda ta zwraca wartoœæ, czy dany URL jest poprawny.
	 * @return true, je¿eli poprawny, false je¿eli niepoprawny.
	 */
	public boolean isValid() 
	{
		return isValid;
	}

	/**
	 * Getter ten s³u¿y do pobierania nazwy strony.
	 * @return nazwa strony
	 */
	public String getName() 
	{
		return name;
	}
	
	/**
	 * Getter ten s³u¿y do pobierania URL strony.
	 * @return URL strony
	 */
	public String getURL() 
	{
		return URL;
	}

	/**
	 * Wypisuje obiekt w formacie "nazwa".
	 * @return nazwa strony
	 */
	@Override
	public String toString() 
	{
		return getName();
	}
}
