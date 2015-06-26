package pl.edu.uwb.mobiuwb.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import pl.edu.uwb.mobiuwb.MobiUwbApp;
import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningRequest;
import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningResult;
import pl.edu.uwb.mobiuwb.VersionControl.VersionController;
import pl.edu.uwb.mobiuwb.connection.checker.MobileChecker;
import pl.edu.uwb.mobiuwb.connection.checker.WiFiChecker;
import pl.edu.uwb.mobiuwb.io.IoManager;
import pl.edu.uwb.mobiuwb.utillities.Globals;
import pl.edu.uwb.mobiuwb.utillities.SharedPreferencesKeyring;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.ConfigXmlResult;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.XMLParser;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.result.model.PropertiesXmlResult;
import pl.edu.uwb.mobiuwb.view.settings.SettingsPreferencesManager;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

public class StartupConfig
{

    private StartupAsyncTask startupAsyncTask;
    private ConfigureEvents configureEvents;

    public static XMLParser xmlParser;
    public static ConfigXmlResult configXmlResult;
    public static PropertiesXmlResult propertiesXmlResult;

    private static SharedPreferences prefs;

    static
    {
        prefs = MobiUwbApp.getContext().getSharedPreferences(
                SharedPreferencesKeyring.SHARED_PREFERENCES_NAME,
                MobiUwbApp.MODE_PRIVATE);
    }

    private static boolean mobileAvailable;
    private static boolean wiFiAvailable;

    public static boolean isMobileAvailable()
    {
        return mobileAvailable;
    }

    public static boolean isWiFiAvailable()
    {
        return wiFiAvailable;
    }

    public boolean isFirstAppStart()
    {
        return prefs.getBoolean(SharedPreferencesKeyring.APP_FIRST_RUN, true);
    }

    public void executeStartupAsyncTask()
    {
        startupAsyncTask = new StartupAsyncTask(isFirstAppStart());
        startupAsyncTask.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void addOnConfigurationEventListener(ConfigureEvents configureEvents)
    {
        this.configureEvents = configureEvents;
    }

    private class StartupAsyncTask extends AsyncTask<Void, Void, Boolean>
    {
        private boolean firstAppStart;
        public StartupAsyncTask(boolean firstAppStart)
        {
            this.firstAppStart = firstAppStart;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                if (firstAppStart)
                {
                    copyPropertiesFileIntoInternalStorage();
                    prefs.edit().putBoolean(
                            SharedPreferencesKeyring.APP_FIRST_RUN,
                            false)
                    .apply();
                }
                determineNetworks();
                executePropertiesParser();
                String downloadedFileContent = downloadConfigurationFileIntoInternalStorage();
                executeConfigurationParser(downloadedFileContent);
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            if (configureEvents != null)
            {
                configureEvents.onConfigurationFinished(result);
            }
            super.onPostExecute(result);


        }
    }

    private static void determineNetworks()
    {
        MobileChecker mobileChecker = new MobileChecker();
        WiFiChecker wifiChecker = new WiFiChecker();

        mobileAvailable = mobileChecker.isAvailable();
        wiFiAvailable =wifiChecker.isAvailable();
    }

    public static String downloadConfigurationFileIntoInternalStorage()
    {
        Context appContext = MobiUwbApp.getContext();


        VersionController versionController = new VersionController(
                appContext.getSharedPreferences(
                        SettingsPreferencesManager.SHARED_PREFERENCES_NAME,
                        Context.MODE_PRIVATE));

        File file = new File(
                appContext.getFilesDir().getPath(),
                IoManager.CONFIGURATION_XML_FILE_NAME);

        VersioningRequest versioningRequest = new VersioningRequest(
                propertiesXmlResult.getXmlPropertiesRootElement().getConfigurationFilePath(),
                file.getAbsolutePath());

        VersioningResult versioningResult =
                versionController.getNewestFile(
                        versioningRequest);

        if(versioningResult.getSucceeded())
        {
            return versioningResult.getFileContent();
        }
        else
        {
            //TODO nie ma tu pliku konfiguracyjnego a jesteśmy w startupie, czyli aplikacja nie ma na czym działać
            //chodzi o przypadek - wyczyscilem dane, ale nie ma neta aby pobrac nowy plik.

            return null;
        }
    }

    private static void copyPropertiesFileIntoInternalStorage()
    {
        Context appContext = MobiUwbApp.getContext();

        InputStream inputStream;
        try
        {
            inputStream = appContext.getAssets().open(
                    IoManager.PROPERTIES_XML_FILE_NAME);

            IoManager.copyAssetsFile(
                    inputStream,
                    IoManager.PROPERTIES_XML_FILE_NAME,
                    appContext.getFilesDir().getPath());
        }
        catch (IOException e)
        {
            Log.d(Globals.MOBIUWB_TAG, e.toString());
        }
    }

    private void executePropertiesParser()
    {
        xmlParser = new XMLParser();
        try
        {
            propertiesXmlResult = xmlParser.deserializePropertiesXml();
        }
        catch (IOException e)
        {
            Log.d(Globals.MOBIUWB_TAG, e.toString());
        }
        catch (ParserConfigurationException e)
        {
            Log.d(Globals.MOBIUWB_TAG, e.toString());
        }
        catch (SAXException e)
        {
            Log.d(Globals.MOBIUWB_TAG, e.toString());
        }
    }

    private void executeConfigurationParser(String fileContent)
    {
        xmlParser = new XMLParser();
        try
        {
            configXmlResult = xmlParser.deserializeConfigurationXml(fileContent);
        }
        catch (IOException e)
        {
            Log.d(Globals.MOBIUWB_TAG, e.toString());
        }
        catch (ParserConfigurationException e)
        {
            Log.d(Globals.MOBIUWB_TAG, e.toString());
        }
        catch (SAXException e)
        {
            Log.d(Globals.MOBIUWB_TAG, e.toString());
        }
    }
}
