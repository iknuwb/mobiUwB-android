package com.example.mobiuwb.view.splash;

import com.example.mobiuwb.R;
import com.example.mobiuwb.configuration.ConfigureEvents;
import com.example.mobiuwb.configuration.StartupConfig;
import com.example.mobiuwb.utillities.SharedPreferencesKeyring;
import com.example.mobiuwb.view.interfaces.GuiAccess;
import com.example.mobiuwb.view.mainactivity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class SplashScreen extends Activity
{
	private Thread splashIntervalThread;
	private boolean doubleClickStop = false;
	private static long backPressedTime;
    private SharedPreferences prefs = null;
	private boolean configurationFinished;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		splashIntervalThread = new Thread() 
		{
			public void run() 
			{
				try 
				{
					long current = System.currentTimeMillis();
					while (true) 
					{
						if(System.currentTimeMillis() >= current + 1500)
						{
							if(configurationFinished == true)
							{
								break;
							}
						}
						else
						{
							if(doubleClickStop == true)
							{
								return;
							}
						}
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				} 
				final Intent intent = new Intent(SplashScreen.this,
						MainActivity.class);
				startActivity(intent);
			}
		};
		splashIntervalThread.start();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) 
	{
		super.onPostCreate(savedInstanceState);
		
		prefs = getSharedPreferences(
				SharedPreferencesKeyring.SHARED_PREFERENCES_NAME, 
				MODE_PRIVATE);
		
		if (prefs.getBoolean(SharedPreferencesKeyring.APP_FIRST_RUN, true)) 
		{
            prefs.edit().putBoolean(SharedPreferencesKeyring.APP_FIRST_RUN, 
            		false).commit();
            configurationFinished = false;
            StartupConfig startupConfig = new StartupConfig();
            startupConfig.addOnConfigurationEventListener(new ConfigureEvents() 
            {
				@Override
				public void onConfigurationFinished() {
					configurationFinished = true;
				}
			});
            startupConfig.onAppFirstStart();
        }
		else
		{
            configurationFinished = true;
		}
	}
	
	@Override
	public void onBackPressed()
	{
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
			{
				if(backPressedTime + 2000 > System.currentTimeMillis())
				{
					if(splashIntervalThread != null && 
							splashIntervalThread.isAlive())
					{
						doubleClickStop = true;
						try
						{
							splashIntervalThread.join();
						}
						catch(InterruptedException e)
						{
							e.printStackTrace();
						}
						finish();
					}
				}
				else
				{
					Toast.makeText(
							this,
							getString(
									R.string.toast_press_again_to_close), 
							Toast.LENGTH_SHORT)
							.show();
				}
				backPressedTime = System.currentTimeMillis();
				break;
			}
			default:
			{
				break;
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}
}