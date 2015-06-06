package pl.edu.uwb.mobiuwb.VersionControl;

import java.io.IOException;

import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningRequest;
import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningResult;
import pl.edu.uwb.mobiuwb.io.IoManager;

/**
 * Created by sennajavie on 2015-06-01.
 */
public class VersionController
{
    public VersioningResult getNewestFile(VersioningRequest versioningRequest)
    {
        VersioningResult result = new VersioningResult();
        try
        {
            Boolean deviceFileExists =
                    IoManager.checkIfFileExists(
                            versioningRequest.getOverwriteDestinationPath());

            if(deviceFileExists)
            {
                Boolean isDeviceFileCurrent = checkIfIsCurrent(versioningRequest);
                if(!isDeviceFileCurrent)
                {
                    IoManager.downloadFile(
                        versioningRequest.getInternetFile(),
                        versioningRequest.getOverwriteDestinationPath());
                }
            }
            else
            {
                IoManager.downloadFile(
                    versioningRequest.getInternetFile(),
                    versioningRequest.getOverwriteDestinationPath());
            }
            FillResultWithLocalFile(
                versioningRequest,
                result);
        }
        catch (Exception e)
        {
            result.addErrorMessage(e.getMessage());
            result.setSucceeded(false);
        }
        return result;
    }

    private Boolean checkIfIsCurrent(VersioningRequest versioningRequest)
    {
        return false; //TODO XD
    }

    private void FillResultWithLocalFile(
            VersioningRequest versioningRequest,
            VersioningResult result)
            throws IOException
    {
        result.setFileContent(
                IoManager.getFileContent(
                        versioningRequest.getOverwriteDestinationPath()));

        result.setFileInputStream(
                IoManager.getFileInputStream(
                        versioningRequest.getOverwriteDestinationPath()));
    }
}
