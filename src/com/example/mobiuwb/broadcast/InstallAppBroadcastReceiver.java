package com.example.mobiuwb.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InstallAppBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) 
	{
		Log.d("MOBIUWB","broadcast MOBIUWB");
    }
}
