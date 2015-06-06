package pl.edu.uwb.mobiuwb.VersionControl.Models;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sennajavie on 2015-06-01.
 */
public class VersioningResult
{
    private VersioningRequest versioningRequest;
    public VersioningRequest getVersioningRequest()
    {
        return versioningRequest;
    }
    public void setVersioningRequest(
            VersioningRequest versioningRequest)
    {
        this.versioningRequest = versioningRequest;
    }

    private InputStream fileInputStream;
    public InputStream getFileInputStream()
    {
        return fileInputStream;
    }
    public void setFileInputStream(InputStream fileInputStream)
    {
        this.fileInputStream = fileInputStream;
    }

    private String fileContent;
    public String getFileContent()
    {
        return fileContent;
    }
    public void setFileContent(String fileContent)
    {
        this.fileContent = fileContent;
    }


    private List<String> errorMessages ;
    public List<String> getErrorMessages()
    {
        return errorMessages;
    }
    public void addErrorMessage(String errorMessage)
    {
        errorMessages.add(errorMessage);
    }

    private Boolean succeeded;
    public Boolean getSucceeded()
    {
        return succeeded;
    }
    public void setSucceeded(Boolean succeeded)
    {
        this.succeeded = succeeded;
    }

    public VersioningResult()
    {
        succeeded = true;
        errorMessages = new ArrayList<String>();
    }
}
