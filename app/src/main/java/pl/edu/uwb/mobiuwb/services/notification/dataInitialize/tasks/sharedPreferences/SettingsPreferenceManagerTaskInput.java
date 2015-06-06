package pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.sharedPreferences;

import android.content.Context;

import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;

/**
 * Created by Tunczyk on 2015-06-03.
 */
public class SettingsPreferenceManagerTaskInput extends TaskInput
{
    private Context baseContext;
    public Context getBaseContext()
    {
        return baseContext;
    }

    public SettingsPreferenceManagerTaskInput(Context baseContext)
    {
        this.baseContext = baseContext;
    }
}
