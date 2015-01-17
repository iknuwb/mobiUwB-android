package com.example.mobiuwb.utillities;

import android.widget.Toast;
import com.example.mobiuwb.MobiUwbApp;
import com.example.mobiuwb.R;
import com.example.mobiuwb.view.interfaces.GuiAccess;

public class MenuMethods
{
	public static boolean menuOptions(int id, GuiAccess activityAccess)
	{
		switch(id)
		{
			case R.id.action_bar_refresh:
			{
				refreshMenuItemClickHandler(activityAccess);
				break;
			}
			case R.id.action_settings:
			{
				Toast.makeText(MobiUwbApp.getContext(), "action_settings", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.action_contact:
			{
				Toast.makeText(MobiUwbApp.getContext(), "action_contact", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.action_about:
			{
				Toast.makeText(MobiUwbApp.getContext(), "action_about", Toast.LENGTH_SHORT).show();
				break;
			}
			case R.id.action_exit:
			{
				activityAccess.finishActivity();
				break;
			}
			default:
			{
				break;
			}
		}
		return true;
	}
	
	private static boolean refreshMenuItemClickHandler(GuiAccess activityAccess)
	{
		activityAccess.refreshWebView();
		return true;
	}
}
