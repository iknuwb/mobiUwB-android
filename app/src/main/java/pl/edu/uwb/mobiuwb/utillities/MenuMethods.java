package pl.edu.uwb.mobiuwb.utillities;

import android.content.Intent;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.about.AboutActivity;
import pl.edu.uwb.mobiuwb.view.contact.ContactActivity;
import pl.edu.uwb.mobiuwb.view.interfaces.GuiAccess;
import pl.edu.uwb.mobiuwb.view.mainactivity.MainActivity;
import pl.edu.uwb.mobiuwb.view.settings.SettingsActivity;

public class MenuMethods
{
    public static boolean menuOptions(int id, GuiAccess activityAccess)
    {
        switch (id)
        {
            case R.id.action_bar_refresh:
            {
                refreshMenuItemClickHandler(activityAccess);
                break;
            }
            case R.id.action_settings:
            {
                Intent intent = new Intent(activityAccess.getContextAccess(), SettingsActivity.class);
                activityAccess.startActivityForResultAccess(intent, MainActivity.SETTINGS_ACTIVITY_RESULT);
                break;
            }
            case R.id.action_contact:
            {
                Intent intent = new Intent(activityAccess.getContextAccess(), ContactActivity.class);
                activityAccess.startActivityAccess(intent);
                break;
            }
            case R.id.action_about:
            {
                Intent intent = new Intent(activityAccess.getContextAccess(), AboutActivity.class);
                activityAccess.startActivityAccess(intent);
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
