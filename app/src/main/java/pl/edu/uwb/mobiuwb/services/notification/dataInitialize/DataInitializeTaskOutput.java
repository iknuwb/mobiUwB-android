package pl.edu.uwb.mobiuwb.services.notification.dataInitialize;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningResult;
import pl.edu.uwb.mobiuwb.tasks.models.TaskOutput;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.ConfigXmlResult;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.result.model.PropertiesXmlResult;

/**
 * Created by Tunczyk on 2015-06-03.
 */
public class DataInitializeTaskOutput extends TaskOutput
{
    public File propertiesFile;
    public File configurationFile;
    public PropertiesXmlResult propertiesXmlResult;
    public VersioningResult versioningResult;
    public ConfigXmlResult configXmlResult;

    public boolean isNotificationActive;
    public boolean isTimeRangeActive;

    public Date timeRangeFrom;
    public Date timeRangeTo;

    public int intervalIndex;
    public long interval;

    public HashMap<String, Boolean> categories;

    public DataInitializeTaskOutput()
    {
        categories = new HashMap<String, Boolean>();
    }
}
