package pl.edu.uwb.ii.mobiuwb.models;

public class NotificationIntervalModel
{
	private String text;
	private long miliseconds;
	
	
	public NotificationIntervalModel(String text, long miliseconds)
	{
		this.text = text;
		this.miliseconds = miliseconds;
	}
	
	
	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}
	
	
	/**
	 * @return the seconds
	 */
	public long getMiliseconds()
	{
		return miliseconds;
	}
	
	
	@Override
	public String toString()
	{
		return getText();
	}
}
