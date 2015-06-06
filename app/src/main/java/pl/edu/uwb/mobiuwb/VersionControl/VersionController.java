package pl.edu.uwb.mobiuwb.VersionControl;

import android.content.SharedPreferences;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningRequest;
import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningResult;
import pl.edu.uwb.mobiuwb.io.IoManager;

/**
 * Created by sennajavie on 2015-06-01.
 */
public class VersionController
{
    private SharedPreferences shareds;

    public VersionController(SharedPreferences shareds)
    {
        this.shareds = shareds;
    }

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
                    downloadFile(versioningRequest);
                }
            }
            else
            {
                downloadFile(versioningRequest);
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

    private void downloadFile(VersioningRequest versioningRequest)
            throws IOException, ParseException
    {
        IoManager.downloadFile(
                versioningRequest.getInternetFile(),
                versioningRequest.getOverwriteDestinationPath());

        Date internetFileModificationDate = getInternetFileModificationDate(versioningRequest);

        SharedPreferences.Editor editor = shareds.edit();
        editor.putLong(versioningRequest.getOverwriteDestinationPath(),
                       internetFileModificationDate.getTime());
        editor.commit();
    }

    private Boolean checkIfIsCurrent(VersioningRequest versioningRequest)
            throws IOException, ParseException
    {
        Date internetFileModificationDate =
                getInternetFileModificationDate(versioningRequest);

        long defaultLongDate = 0;

        Long lastDateLong =
                shareds.getLong(
                        versioningRequest.getOverwriteDestinationPath(),
                        defaultLongDate);
        Date lastDate = new Date(lastDateLong);

        if(lastDate.before(internetFileModificationDate))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private Date getInternetFileModificationDate(VersioningRequest versioningRequest)
            throws IOException, ParseException
    {
        URL obj = new URL(versioningRequest.getInternetFile());
        URLConnection conn = obj.openConnection();
        String internetFileModificationDateString =
                conn.getHeaderField("Last-Modified");
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.parse(
                internetFileModificationDateString);
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
