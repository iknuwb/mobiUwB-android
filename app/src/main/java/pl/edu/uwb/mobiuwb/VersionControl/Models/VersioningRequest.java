package pl.edu.uwb.mobiuwb.VersionControl.Models;

import java.net.URI;

/**
 * Created by sennajavie on 2015-06-01.
 */
public class VersioningRequest
{
    private String internetFile;
    public String getInternetFile()
    {
        return internetFile;
    }

    private String overwriteDestinationPath;
    public String getOverwriteDestinationPath()
    {
        return overwriteDestinationPath;
    }

    public VersioningRequest(String internetFile, String overwriteDestinationPath)
    {
        this.internetFile = internetFile;
        this.overwriteDestinationPath = overwriteDestinationPath;
    }
}
