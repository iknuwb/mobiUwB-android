package pl.edu.uwb.ii.mobiuwb.models;

import pl.edu.uwb.ii.mobiuwb.activities.TypeConnectionCheck;


public class NotyficationsSettingsModel
{
	private Long notificationTimeInterwal;
	private TypeConnectionCheck typeConnectionChech;
	private boolean notificationTurnedOn;
	
	
	public NotyficationsSettingsModel()
	{
		
	}
	
	
	public Long getNotificationTimeInterwal()
	{
		return notificationTimeInterwal;
	}
	
	
	public void setNotificationTimeInterwal(Long notificationTimeInterwal)
	{
		this.notificationTimeInterwal = notificationTimeInterwal;
	}
	
	
	public TypeConnectionCheck getTypeConnectionChech()
	{
		return typeConnectionChech;
	}
	
	
	public void setTypeConnectionChech(TypeConnectionCheck typeConnectionChech)
	{
		this.typeConnectionChech = typeConnectionChech;
	}
	
	
	public boolean isNotificationTurnedOn()
	{
		return notificationTurnedOn;
	}
	
	
	public void setNotificationTurnedOn(boolean notificationTurnedOn)
	{
		this.notificationTurnedOn = notificationTurnedOn;
	}
}
