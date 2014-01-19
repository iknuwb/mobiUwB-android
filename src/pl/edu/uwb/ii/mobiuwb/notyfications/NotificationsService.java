package pl.edu.uwb.ii.mobiuwb.notyfications;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import pl.edu.uwb.ii.mobiuwb.GlobalVariables;
import pl.edu.uwb.ii.mobiuwb.LocalData;
import pl.edu.uwb.ii.mobiuwb.R;
import pl.edu.uwb.ii.mobiuwb.activities.MainActivity;
import pl.edu.uwb.ii.mobiuwb.activities.NotificationsSettingsActivity;
import pl.edu.uwb.ii.mobiuwb.models.JSONNotificationModel;
import pl.za.sennajavie.ConnectionTypeChecker;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.support.v4.app.NotificationCompat;


public class NotificationsService extends IntentService
{
	public LocalData localData;
	public static boolean MAKE_CLOSE = false;
	
	ConnectionTypeChecker ctc = new ConnectionTypeChecker(this);
	
	
	public NotificationsService()
	{
		super("Notifications Service");
	}
	
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		if(GlobalVariables.LAST_VISIT_DATE == null)
		{
			try
			{
				GlobalVariables.LAST_VISIT_DATE = Calendar.getInstance().getTime();
			}
			catch(ParseException e)
			{
				e.printStackTrace();
			}
		}
		
		switch (NotificationsSettingsActivity.nsm.getTypeConnectionChech())
		{
			case BOTH:
			{
				checkBoth();
				break;
			}
			case GSM:
			{
				checkGsm();
				break;
			}
			case WIFI:
			{
				checkWifi();
				break;
			}
		}
		
		stopSelf();
	}
	
	
	private void checkBoth()
	{
		while(MAKE_CLOSE == false)
		{
			synchronized(this)
			{
				if(ctc.freeInternetCheck() == true)
				{
					setNotifications();
					try
					{
						wait(NotificationsSettingsActivity.nsm.getNotificationTimeInterwal());
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
	
	
	private void checkWifi()
	{
		while(MAKE_CLOSE == false)
		{
			synchronized(this)
			{
				if(ctc.freeInternetCheckByWiFi() == true)
				{
					setNotifications();
					try
					{
						wait(NotificationsSettingsActivity.nsm.getNotificationTimeInterwal());
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
	
	
	private void checkGsm()
	{
		while(MAKE_CLOSE == false)
		{
			synchronized(this)
			{
				if(ctc.freeInternetCheckByGsm() == true)
				{
					setNotifications();
					try
					{
						wait(NotificationsSettingsActivity.nsm.getNotificationTimeInterwal());
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
		if(!pnd.models.isEmpty())
		{
			for(JSONNotificationModel item : pnd.models)
			{
				
				Notification noti = new NotificationCompat.Builder(getApplicationContext())
						.setContentTitle("Nowy wpis na stronie UwB.")
						.setContentText(item.getContent())
						.setSmallIcon(R.drawable.ikona_bw)
						.setAutoCancel(true)
						.setContentIntent(resultPendingIntent)
						.setStyle(new NotificationCompat.BigTextStyle()
								.bigText(item.getContent()))
						.build();
				
				/*
				 * NotificationCompat.Builder mBuilder = new
				 * NotificationCompat.Builder(getApplicationContext())
				 * .setSmallIcon(R.drawable.ic_launcher).setContentText(item)
				 * .setContentTitle("Nowy wpis na stronie UwB.")
				 * .setContentInfo(item) .setContentIntent(resultPendingIntent);
				 * Notification n = mBuilder.getNotification();
				 */
				
				manager.notify((int)item.getDate().getTime(), noti);
			}
			
			SimpleDateFormat sdf = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
			
			GlobalVariables.LAST_VISIT_DATE = Calendar.getInstance().getTime();
			localData = new LocalData(this.getSharedPreferences(GlobalVariables.LOCAL_STORAGE, Context.MODE_PRIVATE));
			localData.zapiszDanaLokalna(GlobalVariables.LOCAL_STORE_LAST_VISIT_DATE, sdf.format(GlobalVariables.LAST_VISIT_DATE));
		}
	}
}
