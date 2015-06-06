package pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.propertiesXml;

import android.content.Context;

import pl.edu.uwb.mobiuwb.tasks.models.TaskInput;

/**
 * Created by Tunczyk on 2015-06-03.
 */
public class PropertiesXmlTaskInput extends TaskInput
{
    private String propertiesFileName;
    public String getPropertiesFileName()
    {
        return propertiesFileName;
    }

    private Context baseContext;
    public Context getBaseContext()
    {
        return baseContext;
    }

    public PropertiesXmlTaskInput(String propertiesFileName, Context baseContext)
    {
        this.propertiesFileName = propertiesFileName;
        this.baseContext = baseContext;
    }
}
