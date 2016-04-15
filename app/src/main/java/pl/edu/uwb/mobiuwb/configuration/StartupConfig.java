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

/**
 * Opisuje proces konfiguracji aplikacji w trakcie jej startu.
 */
public class StartupConfig
{

    /**
     * Jest oddzielnym wątkiem w którym dzieje się konfiguracja w trakcie startu aplikacji.
     */
    private StartupAsyncTask startupAsyncTask;

    /**
     * Reprezentuje wydarzenia związane z pierwszą konfiguracją aplikacji.
     */
    private ConfigureEvents configureEvents;

    /**
     * Jest to globalny egzemplarz analizatora języka XML.
     */
    public static XMLParser xmlParser;

    /**
     * Jest to to, co zostanie zwrócone przez proces konfiguracji startowej.
     */
    public static ConfigXmlResult configXmlResult;

    /**
     * Jest to to, co zostanie zwrócone po przeanalizowaniu pliku Właściwości.
     */
    public static PropertiesXmlResult propertiesXmlResult;

    /**
     * Preferencje Androida. Wykorzystywane do serializacji
     * najróżniejszych danych w programie.
     * Ten egzemplarz preferencji posiada prywatny dostęp.
     */
    private static SharedPreferences prefs;

    /**
     * Inicjalizuje preferencje Androida poprzez
     * domyślną nazwę z prywatnym dostępem.
     *
     */
    static
    {
        prefs = MobiUwbApp.getContext().getSharedPreferences(
                SharedPreferencesKeyring.SHARED_PREFERENCES_NAME,
                MobiUwbApp.MODE_PRIVATE);
    }

    /**
     * Determinuje, czy mobilne połączenie z Internetem jest dostępne.
     */
    private static boolean mobileAvailable;

    /**
     * Determinuje, czy połączenie WiFi z Internetem jest dostępne.
     */
    public static boolean isWiFiAvailable()
    {
        return wiFiAvailable;
    }

    /**
     * Determinuje, czy połączenie WiFi z Internetem jest dostępne.
     */
    private static boolean wiFiAvailable;

    /**
     * Determinuje, czy mobilne połączenie z Internetem jest dostępne.
     */
    public static boolean isMobileAvailable()
    {
        return mobileAvailable;
    }

    /**
     * Sprawdza, czy to jest pierwsze uruchomienie aplikacji na tym urządzeniu.
     * @return True, jeżeli to jest pierwsze uruchomienie, false w przeciwnym wypadku.
     */
    public boolean isFirstAppStart()
    {
        return prefs.getBoolean(SharedPreferencesKeyring.APP_FIRST_RUN, true);
    }

    /**
     * Uruchamia proces odpowiedzialny za pierwszą konfigurację aplikacji.
     */
    public void executeStartupAsyncTask()
    {
        startupAsyncTask = new StartupAsyncTask(isFirstAppStart());
        startupAsyncTask.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * Dodaje nasłuchiwacz, którego wydarzenie wydarzy się gdy pierwsza konfiguracja
     * zostanie zakończona.
     * @param configureEvents Nasłuchiwacz, który zostanie nadany.
     */
    public void addOnConfigurationEventListener(ConfigureEvents configureEvents)
    {
        this.configureEvents = configureEvents;
    }

    /**
     * Jest to wątek, którego zadaniem jest uruchomienie procesu pierwszej konfiguracji.
     */
    private class StartupAsyncTask extends AsyncTask<Void, Void, Boolean>
    {
        /**
         * Opisuje, czy to jest pierwsze uruchomienie tej aplikacji na urządzeniu.
         */
        private boolean firstAppStart;

        /**
         * Inicjalizuje pola.
         * @param firstAppStart Opisuje, czy to jest pierwsze uruchomienie
         *                      tej aplikacji na urządzeniu.
         */
        public StartupAsyncTask(boolean firstAppStart)
        {
            this.firstAppStart = firstAppStart;
        }

        /**
         * Wydarza się tuż przed uruchomieniem niniejszego wątku.
         */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        /**
         * Metoda ta dzieje się w oddzielnym wątku,
         * czyli w wątku wykonywującym pierwszą konfigurację.
         * Do jej zadań należy uruchomienie metod konfigurujących.
         * Sprawdza, czy to jest pierwsze uruchomienie.
         * Kopiuje plik Właściwości do pamięci wewnętrznej aplikacji.
         * Ustala zmienną globalną w preferencjach, która oznacza,
         * że pierwsze uruchomienie się odbyło.
         * Sprawdza dostępne połączenia z Internetem.
         * Uruchamia analizator pliku XML Właściwości.
         * Pobiera na urządzenie plik Konfiguracji oraz uruchamia na nim analizator.
         * @param params Te parametry są nieużywane.
         * @return True, jeżeli wszystko się powiodło, false, jeżeli nie.
         */
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

        /**
         * Wydarza się, gdy wątek ten się zakończy.
         * Uruchamia wydarzenie ukończenia konfiguracji.
         * @param result
         */
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

    /**
     * Metoda ta sprawdza, które połączenia z Internetem są możliwe do zrealizowania
     * na tym urządzeniu.
     */
    private static void determineNetworks()
    {
        MobileChecker mobileChecker = new MobileChecker();
        WiFiChecker wifiChecker = new WiFiChecker();

        mobileAvailable = mobileChecker.isAvailable();
        wiFiAvailable =wifiChecker.isAvailable();
    }

    /**
     * Metoda ta pobiera zawsze najnowszy plik Konfiguracji.
     * Jeżeli nowszy jest w serwisie WWW, to pobierze jego zawartość,
     * jeżeli nie, to pobierze lokalny plik Konfiguracji.
     * @return Zawartość najnowszego pliku konfiguracji.
     */
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
            //TODO nie ma tu pliku konfiguracyjnego a jesteśmy w startupie,
            //czyli aplikacja nie ma na czym działać
            //chodzi o przypadek - wyczyscilem dane, ale nie ma neta aby pobrac nowy plik.

            return null;
        }
    }

    /**
     * Metoda ta kopiuje plik Właściwości na pamięć urządzenia.
     * Umożliwia to jego edycję.
     */
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

    /**
     * Uruchamia analizator plików XML na pliku Właściwości.
     * Wypełnia zmienne odpowiedzialne za ten plik.
     */
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

    /**
     * Uruchamia analizator plików XML na pliku Konfiguracji.
     * Wypełnia zmienne odpowiedzialne za ten plik.
     */
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
