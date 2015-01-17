package com.example.mobiuwb.xml.model;

import java.util.Iterator;
import java.util.List;

public class XmlPropertiesRootElement 
{
	
	private Website defaultWebsite;
	public Website getDefaultWebsite() 
	{
		return defaultWebsite;
	}
	public void setDefaultWebsite(Website defaultWebsite) 
	{
		this.defaultWebsite = defaultWebsite;
	}
	
	public List<Website> websites;
	
	public XmlPropertiesRootElement(Website defaultWebsite,
			List<Website> websites) 
	{
		this.defaultWebsite = defaultWebsite;
		this.websites = websites;
	}
	
	/**
	 * Metoda wyszukująca indexu defaultowego obiektu w liście.
	 * @return wartość >= 0 jeżeli znaleziono, -1 w przeciwnym przypadku
	 */
	public int getDefaultWebsiteIndex()
	{
		if(defaultWebsite != null && websites != null)
		{
			for(int i =0; i < websites.size(); i++)
			{
				Website item = websites.get(i);
				
				if(item.equals(defaultWebsite))
				{
					return i;
				}
			}
		}
		return -1;
	}
}
