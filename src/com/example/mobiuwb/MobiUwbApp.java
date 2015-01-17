package com.example.mobiuwb;

import com.example.mobiuwb.configuration.StartupConfig;
import com.example.mobiuwb.utillities.SharedPreferencesKeyring;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class MobiUwbApp extends Application
{
	
	private static MobiUwbApp instance;
    
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		instance = this;
	}
	
	public static MobiUwbApp getContext()
	{
		return instance;
	}
	
	
}