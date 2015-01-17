package com.example.mobiuwb.xml.model;


public class Website 
{
	private String name;
	public String getName() {
		return name;
	}
	void setName(String name) {
		this.name = name;
	}
	
	private String url;
	public String getUrl() {
		return url;
	}
	void setUrl(String url) {
		this.url = url;
	}
	
	private String ping;
	public String getPing() {
		return ping;
	}
	void setPing(String ping) {
		this.ping = ping;
	}
	
	public Website(String name, String url, String ping) 
	{
		this.name = name;
		this.url = url;
		this.ping = ping;
	}
	
	@Override
	public int hashCode() 
	{
		return 0;
	}
	
	@Override
	public boolean equals(Object o) 
	{
		if(o instanceof Website)
		{
			Website that = (Website)o;
			return name.equals(that.name);
		}
		else
		{
			return false;
		}
	}
}
