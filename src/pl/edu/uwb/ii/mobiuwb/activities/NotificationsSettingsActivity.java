package pl.edu.uwb.ii.mobiuwb.activities;

import java.util.ArrayList;
import pl.edu.uwb.ii.mobiuwb.R;
import pl.edu.uwb.ii.mobiuwb.adapters.NotificationsIntervalAdapter;
import pl.edu.uwb.ii.mobiuwb.models.NotificationIntervalModel;
import pl.edu.uwb.ii.mobiuwb.models.NotyficationsSettingsModel;
import pl.edu.uwb.ii.mobiuwb.notyfications.NotificationsService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;


public class NotificationsSettingsActivity extends ActionBarActivity
{
	
	public static NotyficationsSettingsModel nsm;
	private CheckBox gsmCheckBox;
	private CheckBox wifiCheckBox;
	private CheckBox onOffCheckBox;
	private Spinner timeIntervalSpinner;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications_settings);
		setEvents();
		timeIntervalSpinner.setAdapter(new NotificationsIntervalAdapter(getApplicationContext(), fillIntervalList()));
		timeIntervalSpinner.setSelection(0);
		this.setTitle("Ustawienia powiadomień");
	}
	
	
	@Override
	protected void onStart()
	{
		setSelectedSpinnerItem();
		setCheckedOnOfCheckBox();
		setCheckedConnectionCeckBoxes();
		NotificationsService.MAKE_CLOSE = true;
		super.onStart();
	}
	
	
	@Override
	protected void onStop()
	{
		NotificationsService.MAKE_CLOSE = false;
		Context ctx = getApplicationContext();
		Intent i = new Intent(ctx, NotificationsService.class);
		ctx.startService(i);
		super.onStop();
	}
	
	
	private void setSelectedSpinnerItem()
	{
		int position = 0;
		NotificationIntervalModel item;
		for(int i = 0; i < timeIntervalSpinner.getAdapter().getCount(); i++)
		{
			item = (NotificationIntervalModel)timeIntervalSpinner.getAdapter().getItem(i);
			if(item.getMiliseconds() == nsm.getNotificationTimeInterwal())
			{
				position = i;
				timeIntervalSpinner.setSelection(position);
				return;
			}
		}
		timeIntervalSpinner.setSelection(position);
	}
	
	
	private void setCheckedOnOfCheckBox()
	{
		onOffCheckBox.setChecked(nsm.isNotificationTurnedOn());
	}
	
	
	private void setCheckedConnectionCeckBoxes()
	{
		if(nsm.getTypeConnectionChech() == TypeConnectionCheck.BOTH)
		{
			gsmCheckBox.setChecked(true);
			wifiCheckBox.setChecked(true);
		}
		else if(nsm.getTypeConnectionChech() == TypeConnectionCheck.WIFI)
		{
			gsmCheckBox.setChecked(false);
			wifiCheckBox.setChecked(true);
		}
		else if(nsm.getTypeConnectionChech() == TypeConnectionCheck.GSM)
		{
			gsmCheckBox.setChecked(true);
			wifiCheckBox.setChecked(false);
		}
	}
	
	
	private ArrayList<NotificationIntervalModel> fillIntervalList()
	{
		ArrayList<NotificationIntervalModel> temp = new ArrayList<NotificationIntervalModel>();
		temp.add(new NotificationIntervalModel(getResources().getString(R.string.notification_interval_item1), 900000));
		temp.add(new NotificationIntervalModel(getResources().getString(R.string.notification_interval_item2), 3600000));
		temp.add(new NotificationIntervalModel(getResources().getString(R.string.notification_interval_item3), 18000000));
		return temp;
	}
	
	
	private void setEvents()
	{
		setIntervalSpinnerEvents();
		setNotificationsCheckBoxEvents();
		setConnectionCheckBoxEvents();
	}
	
	
	private void setConnectionCheckBoxEvents()
	{
		gsmCheckBox = (CheckBox)this.findViewById(R.id.notifications_connection_type_gsm_check_box);
		gsmCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				setConnectionCeckBoxSettings();
				
			}
		});
		
		wifiCheckBox = (CheckBox)this.findViewById(R.id.notifications_connection_type_wifi_check_box);
		wifiCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				setConnectionCeckBoxSettings();
				
			}
		});
	}
	
	
	private void setIntervalSpinnerEvents()
	{
		timeIntervalSpinner = (Spinner)this.findViewById(R.id.notifications_interval_chooser);
		timeIntervalSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				setNotyficationsTimeIntervalSpinnerSettings();
				
			}
			
			
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	private void setNotificationsCheckBoxEvents()
	{
		onOffCheckBox = (CheckBox)this.findViewById(R.id.notifications_on_off_check_box);
		onOffCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				setNotyficationsCeckBoxSettings();
				
			}
		});
	}
	
	
	private void setNotyficationsTimeIntervalSpinnerSettings()
	{
		Long value = ((NotificationIntervalModel)timeIntervalSpinner.getSelectedItem()).getMiliseconds();
		nsm.setNotificationTimeInterwal(value);
	}
	
	
	private void setNotyficationsCeckBoxSettings()
	{
		boolean turnedOn = onOffCheckBox.isChecked();
		nsm.setNotificationTurnedOn(turnedOn);
	}
	
	
	private void setConnectionCeckBoxSettings()
	{
		boolean GSM = gsmCheckBox.isChecked();
		boolean WiFi = wifiCheckBox.isChecked();
		if(GSM == true && WiFi == true)
		{
			nsm.setTypeConnectionChech(TypeConnectionCheck.BOTH);
		}
		else if(GSM == false && WiFi == true)
		{
			nsm.setTypeConnectionChech(TypeConnectionCheck.WIFI);
		}
		else if(GSM == true && WiFi == false)
		{
			nsm.setTypeConnectionChech(TypeConnectionCheck.GSM);
		}
	}
}
