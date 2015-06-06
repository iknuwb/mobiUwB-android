package pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.versionController;

import android.content.Context;

import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;

/**
 * Created by Tunczyk on 2015-06-03.
 */
public class VersionControllerTaskInput extends TaskInput
{
    private String configurationFileName;
    public String getConfigurationFileName()
    {
        return configurationFileName;
    }

    private Context baseContext;
    public Context getBaseContext()
    {
        return baseContext;
    }

    public VersionControllerTaskInput(String propertiesFileName,
                                      Context baseContext)
    {
        this.configurationFileName = propertiesFileName;
        this.baseContext = baseContext;
    }
}
