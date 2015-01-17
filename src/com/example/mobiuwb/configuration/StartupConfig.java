package com.example.mobiuwb.configuration;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mobiuwb.MobiUwbApp;
import com.example.mobiuwb.io.IoManager;

public class StartupConfig 
{
	private FileReaderAsyncTask fileReaderAsyncTask;
    private ConfigureEvents configureEvents;
    
	public void onAppFirstStart()
	{
		fileReaderAsyncTask = new FileReaderAsyncTask();
		fileReaderAsyncTask.executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	public void addOnConfigurationEventListener(ConfigureEvents configureEvents)
	{
		this.configureEvents = configureEvents;
	}
	
	private static void copyPropertiesFileIntoInternalStorage()
	{
		Context appContext = MobiUwbApp.getContext();
		
		InputStream inputStream;
		try 
		{
			inputStream = appContext.getAssets().open(
					IoManager.PROPERTIES_XML_FILE_NAME);
			
			IoManager.copyAssetsFile(
					inputStream, 
					IoManager.PROPERTIES_XML_FILE_NAME,
					appContext.getFilesDir().getPath());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private class FileReaderAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute() {
            
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params) 
		{
			copyPropertiesFileIntoInternalStorage();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(configureEvents != null)
			{
				configureEvents.onConfigurationFinished();
			}
			super.onPostExecute(result);
		}
	}
}
