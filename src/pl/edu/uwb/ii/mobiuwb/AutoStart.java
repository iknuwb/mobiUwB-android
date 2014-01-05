package pl.edu.uwb.ii.mobiuwb;

import pl.edu.uwb.ii.mobiuwb.notyfications.NotificationsService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AutoStart extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stub
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
			Intent i = new Intent(context, NotificationsService.class);
			context.startService(i);
		}
	}
}
