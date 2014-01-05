package pl.edu.uwb.ii.mobiuwb.notyfications;

import java.util.ArrayList;
import java.util.Date;
import pl.edu.uwb.ii.mobiuwb.GlobalVariables;
import pl.edu.uwb.ii.mobiuwb.models.JSONNotificationModel;
import pl.edu.uwb.ii.mobiuwb.parsers.JSONParser;
import pl.edu.uwb.ii.mobiuwb.parsers.NotyficationParser;


public class PrepareNotificationData
{
	public static Date lastVisitDate;
	public static Date currentVisitDate;
	public static String jsonURL = "http://ii.uwb.edu.pl/serwis/?/json/sz"; // TODO
	private JSONParser jp;
	public ArrayList<String> notificationsMessages;
	
	
	public PrepareNotificationData()
	{
		lastVisitDate = GlobalVariables.LAST_VISIT_DATE;
		currentVisitDate = new Date();
		notificationsMessages = new ArrayList<String>();
		jp = new JSONParser(jsonURL);
		jp.parseJSON();
		
		chooseNewData();
	}
	
	
	private void chooseNewData()
	{
		String cleanMessage;
		for(JSONNotificationModel item : jp.JSONModelList)
		{
			if(item.getDate().compareTo(lastVisitDate) > 0)
			{
				cleanMessage = NotyficationParser
						.prepareTextToNotification(item.getContent());
				notificationsMessages.add(cleanMessage);
			}
		}
	}
	
}
