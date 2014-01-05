package pl.edu.uwb.ii.mobiuwb.activities;

import java.util.ArrayList;
import pl.edu.uwb.ii.mobiuwb.R;
import pl.edu.uwb.ii.mobiuwb.R.id;
import pl.edu.uwb.ii.mobiuwb.R.layout;
import pl.edu.uwb.ii.mobiuwb.adapters.NotificationsIntervalAdapter;
import pl.edu.uwb.ii.mobiuwb.models.NotificationIntervalModel;
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
	Spinner sp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications_settings);
		sp = (Spinner)this.findViewById(R.id.notifications_interval_chooser);
		sp.setAdapter(new NotificationsIntervalAdapter(getApplicationContext(), fillIntervalList()));
		sp.setSelection(0);
		this.setTitle("Ustawienia powiadomień");
		setEvents();
	}
	
	
	private ArrayList<NotificationIntervalModel> fillIntervalList()
	{
		// TODO do strings.xml
		ArrayList<NotificationIntervalModel> temp = new ArrayList<NotificationIntervalModel>();
		temp.add(new NotificationIntervalModel("15 min.", 900000));
		temp.add(new NotificationIntervalModel("1 godz.", 3600000));
		temp.add(new NotificationIntervalModel("5 godz.", 18000000));
		return temp;
	}
	
	
	private void setEvents()
	{
		setIntervalSpinnerEvents();
		setNotificationsCheckBoxEvents();
	}
	
	
	private void setIntervalSpinnerEvents()
	{
		Spinner spinner = (Spinner)this.findViewById(R.id.notifications_interval_chooser);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				// TODO Auto-generated method stub
				
			}
			
			
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	private void setNotificationsCheckBoxEvents()
	{
		CheckBox checkBox = (CheckBox)this.findViewById(R.id.notifications_on_off_check_box);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
}
