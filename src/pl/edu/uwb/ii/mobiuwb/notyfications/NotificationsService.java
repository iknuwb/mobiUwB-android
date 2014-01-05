package pl.edu.uwb.ii.mobiuwb.notyfications;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import pl.edu.uwb.ii.mobiuwb.GlobalVariables;
import pl.edu.uwb.ii.mobiuwb.LocalData;
import pl.edu.uwb.ii.mobiuwb.R;
import pl.edu.uwb.ii.mobiuwb.R.drawable;
import pl.edu.uwb.ii.mobiuwb.activities.MainActivity;
import pl.za.sennajavie.ConnectionTypeChecker;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


public class NotificationsService extends IntentService
{
	public LocalData localData;
	
	
	public NotificationsService()
	{
		super("Notifications Service");
	}
	
	ConnectionTypeChecker ctc = new ConnectionTypeChecker(this);
	
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		
		localData = new LocalData(this.getSharedPreferences(GlobalVariables.LOCAL_STORAGE, Context.MODE_PRIVATE));
		
		SimpleDateFormat sdf = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
		
		try
		{
			GlobalVariables.LAST_VISIT_DATE = sdf.parse(localData.pobierzDanaLokalna(GlobalVariables.LOCAL_STORE_LAST_VISIT_DATE, sdf.format(Calendar.getInstance().getTime())));
		}
		catch(ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true)
		{
			synchronized(this)
			{
				if(ctc.freeInternetCheck() == true)
				{
					setNotifications();
					try
					{
						wait(5000);// co godzine
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					try
					{
						wait(5000); // jak nie trafisz neta w danej godzinie to
									// probuj ponownie co x czasu
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				
			}
		}
	}
	
	
	private void setNotifications()
	{
		PrepareNotificationData pnd = new PrepareNotificationData();
		
		Intent resultIntent = new Intent(this, MainActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
				0x34958, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		if(!pnd.notificationsMessages.isEmpty())
		{
			for(String item : pnd.notificationsMessages)
			{
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
						.setSmallIcon(R.drawable.ic_launcher).setContentText(item)
						.setContentTitle("Nowy wpis na stronie UwB.")
						.setContentInfo(item)
						.setContentIntent(resultPendingIntent);
				Notification n = mBuilder.getNotification();
				
				manager.notify(0x34958, n);
			}
			SimpleDateFormat sdf = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
			
			localData.zapiszDanaLokalna(GlobalVariables.LOCAL_STORE_LAST_VISIT_DATE, sdf.format(Calendar.getInstance().getTime()));
		}
	}
}
