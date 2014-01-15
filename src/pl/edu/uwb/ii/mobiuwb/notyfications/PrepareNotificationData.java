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
	public static String jsonURL = "http://tunczyk.keep.pl/pobrane"; // TODO
	private JSONParser jp;
	public ArrayList<JSONNotificationModel> models;
	
	
	public PrepareNotificationData()
	{
		models = new ArrayList<JSONNotificationModel>();
		jp = new JSONParser(jsonURL);
		jp.parseJSON();
		
		chooseNewData();
	}
	
	
	private void chooseNewData()
	{
		String cleanMessage;
		for(JSONNotificationModel item : jp.JSONModelList)
		{
			if(item.getDate().compareTo(GlobalVariables.LAST_VISIT_DATE) > 0)
			{
				cleanMessage = NotyficationParser.prepareTextToNotification(item.getContent());
				item.setContent(cleanMessage);
				models.add(item);
			}
		}
		jp.JSONModelList.clear();
	}
}
