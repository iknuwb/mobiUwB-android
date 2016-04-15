package pl.edu.uwb.mobiuwb.services.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningRequest;
import pl.edu.uwb.mobiuwb.VersionControl.Models.VersioningResult;
import pl.edu.uwb.mobiuwb.VersionControl.VersionController;
import pl.edu.uwb.mobiuwb.parsers.json.model.Feed;
import pl.edu.uwb.mobiuwb.parsers.json.parser.JsonParser;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.Section;
import pl.edu.uwb.mobiuwb.parsers.xml.parser.UniversityUnit;
import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.DataInitializeTaskOutput;
import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.configurationXml.ConfigurationXmlTask;
import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.propertiesXml.PropertiesXmlTask;
import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.propertiesXml.PropertiesXmlTaskInput;
import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.sharedPreferences.SettingsPreferenceManagerTask;
import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.sharedPreferences.SettingsPreferenceManagerTaskInput;
import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.versionController.VersionControllerTask;
import pl.edu.uwb.mobiuwb.services.notification.dataInitialize.tasks.versionController.VersionControllerTaskInput;
import pl.edu.uwb.mobiuwb.tasks.TasksQueue;
import pl.edu.uwb.mobiuwb.view.splash.SplashScreenActivity;

/**
 * Jest to usługa obsługująca Powiadomienia w aplikacji.
 * Jako oddzielny proces, działa ona w tle wtedy, kiedy aplikacja jest
 * wyłączona. Dane są pozyskiwane poprzez deserializację
 * wcześniej zserializowanych danych w samej aplikacji.
 */
public class NotificationService extends Service
{
    /**
     * Flaga oznaczająca "ładuj ponownie konfigurację".
     */
    public static final int RELOAD_CONFIGURATION = 0;

    /**
     * Flaga oznaczająca "restartuj usługę".
     */
    public static final int RESTART = 1;

    /**
     * Flaga oznaczająca "stop usłudze".
     */
    public static final int STOP = 2;

    /**
     * Flaga oznaczająca "wystartuj usługę".
     */
    public static final int START = 3;

    /**
     * Wątek wewnętrzny usługi.
     */
    private ServiceTask serviceTask;

    /**
     * Wydarza się gdy wydajemy jakąś komendę usłudze.
     * Konfigurujemy co usługa ma zrobić za pomocą flag.
     * @param intent Intencja wchodząca w tą usługę.
     * @param flags Flagi do kontroli usługi.
     * @param startId Startowe ID, nieużywany parametr.
     * @return Zawsze zwraca flagę, która informuje system operacyjny,
     * że ta usługa powinna zostać ponownie uruchamiana w przyszłości.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        int flag;
        if(intent == null)
        {
            flag = RESTART;
        }
        else
        {
            flag = intent.getIntExtra(ServiceManager.REQUEST_FLAG, -1);
        }
        parseRequest(flag);
        return Service.START_STICKY;
    }

    /**
     * Przetwarza żądanie bazując na flagach.
     * @param flag Wejściowa flaga.
     */
    private void parseRequest(int flag)
    {
        switch(flag)
        {
            case RELOAD_CONFIGURATION:
            case RESTART:
            {
                if(serviceTask != null)
                {
                    serviceTask.stopBackgroundProcess();
                    serviceTask.cancel(true);
                }
                serviceTask = new ServiceTask();
                serviceTask.execute();
                break;
            }
            case STOP:
            {
                if(serviceTask != null)
                {
                    serviceTask.stopBackgroundProcess();
                    serviceTask.cancel(true);
                }
                break;
            }
            case START:
            {
                serviceTask = new ServiceTask();
                serviceTask.execute();
                break;
            }
            default:
            {
                break;
            }
        }
    }


    /**
     * Wewnętrzna metoda systemu Android, nieużywana.
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    /**
     * Wydarza się, gdy usługa kończy swoje działanie.
     * Stopowany jest wtedy wątek wewnętrzny usługi.
     */
    @Override public void onDestroy()
    {
        super.onDestroy();
        serviceTask.cancel(true);
    }

    /**
     * Jest to wątek wewnętrzny usługi, który zapewnia asynchroniczne działanie,
     * oraz możliwość łatwego włączenia lub wyłączenia operacji bez
     * zabijania całej usługi.
     * Do jego zadań należy wysyłanie notyfikacji.
     */
    private class ServiceTask extends AsyncTask<Void, Void, Void>
    {
        /**
         * Determinuje, czy jest aktywny.
         */
        private boolean active;

        /**
         * Uogólniony obiekt zawierający dane zwracane przez listę zadań.
         */
        private DataInitializeTaskOutput dataInitializeTaskOutput;

        /**
         * Kiedy ostatnio wysyłaliśmy notyfikacje.
         */
        private long lastCheckTime = 0;

        /**
         * Aktualny czas.
         */
        private long currentTime = 0;

        /**
         * Stopuje ten proces.
         */
        public void stopBackgroundProcess()
        {
            active = false;
        }

        /**
         * Wydarza się tuż przed startem tego procesu.
         */
        @Override protected void onPreExecute()
        {
            active = true;
            super.onPreExecute();
        }

        /**
         * Dzieje się w oddzielnym wątku.
         * Wywołuje wszelkie operacje związane z dostarczeniem
         * powiadomienia użytkownikowi.
         * Ponadto wywołuje inicjalizację danych.
         * @param params Nieużywane parametry.
         * @return Nieużywana wartość zwracana.
         */
        @Override protected Void doInBackground(Void... params)
        {
            boolean succeeded = InitializeData();
            processNotifications(succeeded);
            return null;
        }

        /**
         * Sprawdza, czy aplikacja może generować powiadomienia, czy też nie.
         * @param succeeded Czy generować powiadomienia, czy też nie.
         */
        private void processNotifications(boolean succeeded)
        {
            if(dataInitializeTaskOutput.isNotificationActive &&
                    succeeded)
            {
                notificationsLoop();
            }
        }

        /**
         * Enkapsuluje podstawową pętlę generującą powiadomienia.
         */
        private void notificationsLoop()
        {
            long interval = dataInitializeTaskOutput.interval;
            Date from = dataInitializeTaskOutput.timeRangeFrom;
            Date to = dataInitializeTaskOutput.timeRangeTo;
            while(active)
            {
                notificationsLoopTick(interval, from, to);
            }
        }

        /**
         * Pojedyncze wywołanie pętli generującej powiadomienia.
         * Sprawdza warunki odnośnie odstępu od wywołania kolejnych powiadomień.
         * @param interval Przerwa między powiadomieniami.
         * @param from Czas "od" w zakresie czasu.
         * @param to Czas "do" w zakresie czasu.
         */
        private void notificationsLoopTick(long interval, Date from, Date to)
        {
            currentTime = System.currentTimeMillis();
            if(currentTime > lastCheckTime + interval)
            {
                if(dataInitializeTaskOutput.isTimeRangeActive)
                {
                    timeRangeNotificationsExecute(from, to);
                }
                else
                {
                    notificationsExecute();
                }
            }
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        /**
         * Metoda ta uruchamia metodę od powiadomień.
         */
        private void notificationsExecute()
        {
            lastCheckTime = currentTime;
            runNotificationsPublisher();
        }

        /**
         * Metoda ta uruchamia metodę od powiadomień, are pod warunkami
         * zakresu czasu, w jakim użytkownik chciałby otrzymywać powiadomienia.
         * @param from Czas "od" w zakresie czasu.
         * @param to Czas "do" w zakresie czasu.
         */
        private void timeRangeNotificationsExecute(Date from, Date to)
        {
            Date currentDate = new Date(currentTime);
            int currentHours = currentDate.getHours();
            int currentMinutes = currentDate.getMinutes();

            int fromHours = from.getHours();
            int fromMinutes = from.getMinutes();

            int toHours = to.getHours();
            int toMinutes = to.getMinutes();

            if(fromHours != toHours)
            {
                notificationsTimeRangeCheck(currentHours,
                                            fromHours,
                                            toHours);
            }
            else
            {
                notificationsTimeRangeCheck(currentMinutes,
                                            fromMinutes,
                                            toMinutes);
            }
        }

        /**
         * Metoda ta wykonuje dalsze porównania warunków czasu, po czym
         * uruchamia wykonywacza notyfikacji.
         * @param current Aktualny czas.
         * @param from Czas "od" w zakresie czasu.
         * @param to Czas "do" w zakresie czasu.
         */

        private void notificationsTimeRangeCheck(int current, int from, int to)
        {
            if (from <= current &&
                    current <= to)
            {
                notificationsExecute();
            }
        }

        /**
         * Metoda ta publikuje notyfikacje.
         * Robi to dla konkretnych kategorii i sprawdza, czy użytkownich chciał,
         * aby dla danej kategorii otrzymać powiadomienie.
         * Następnie metoda pobiera aktualną sekcję i dla niej pobiera listę
         * kanałów informacyjnych, po czym uruchamia dla danego kanału powiadomienia.
         */
        private void runNotificationsPublisher()
        {
            UniversityUnit unit = dataInitializeTaskOutput.
                    configXmlResult.getCurrentUniversityUnit();
            int i = 0;
            for (Map.Entry<String, Boolean> category :
                    dataInitializeTaskOutput.categories.entrySet())
            {
                if(category.getValue())
                {
                    Section section = unit.getSectionById(category.getKey());
                    List<Feed> newestFeeds = getNewElements(section, unit);
                    publishNotifications(newestFeeds,section,i);

                    ServicePreferencesManager servicePreferencesManager =
                            ServicePreferencesManager.getInstance(
                                    getBaseContext());

                    servicePreferencesManager.setLastKnownDate(
                            unit.getName(),
                            category.getKey(),
                            new Date());
                }
                i++;
            }
        }

        /**
         * Metoda ta pobiera zawsze najnowszy plik JSON z kanałami informacyjnymi,
         * aby wydobyć z niego interesujące użytkownika kanały informacyjne.
         * Aby to zrealizować, operuje ona na preferencjeach aplikacji w celu pobrania plików
         * za pomocą kontrolera wersji.
         * Następnie ten plik JSON pobrany kontrolerem wersji jest analizowany.
         * Wydobywana jest z niego informacja o interesujących użytkownika kanałach i to one
         * są zwracane przez metode. Interesujący kanał jest determinowany przez jego datę wydania.
         * Jeżeli jest nowy, to zostaje zwrócony przez metodę.
         * @param section Sekcja, dla której należy pozyskać interesujące kanały.
         * @param unit Jednostka na Uniwersytecie w Białymstoku.
         * @return Kanały informacyjne, które jeszcze nie zostały pokazane przez aplikacje
         * i są one nowe.
         */
        private List<Feed> getNewElements(Section section, UniversityUnit unit)
        {
            List<Feed> notificationElements = new ArrayList<Feed>();

            SharedPreferences shareds = getSharedPreferences(
                    VersionControllerTask.SHARED_PREFERENCES_SERVICE_NAME,
                    Context.MODE_PRIVATE);

            VersionController versionController = new VersionController(shareds);
            String feedsFileUri = unit.getApiUrl() + section.id;

            Context baseContext = getBaseContext();

            String overwritePath = baseContext.getFilesDir().getPath();

            File overwriteFilePath = new File(overwritePath,section.id);


            VersioningRequest versioningRequest =
                    new VersioningRequest(feedsFileUri,
                                          overwriteFilePath.getAbsolutePath());

            VersioningResult versioningResult =
                    versionController.getNewestFile(versioningRequest);

            List<Feed> newestFeeds = new ArrayList<Feed>();
            if(versioningResult.getSucceeded())
            {
                String jsonContent = versioningResult.getFileContent();

                JsonParser jsonParser = new JsonParser();
                ServicePreferencesManager servicePreferencesManager =
                        ServicePreferencesManager.getInstance(getBaseContext());
                try
                {
                    notificationElements = jsonParser.parseFeedsJson(jsonContent);

                    Date lastKnownDate = servicePreferencesManager.getLastKnownDate(unit.getName(),
                                                                                    section.id);


                    for(Feed feed : notificationElements)
                    {
                        if(feed.getDate().after(lastKnownDate))
                        {
                            newestFeeds.add(feed);
                        }
                    }

                }
                catch (JSONException e)
                {
                    Log.e("MobiUwB",e.getMessage());
                }
            }
            return newestFeeds;
        }

        /**
         * Funkcja ta inicjalizuje wszelkie dane niezbędne dla procesu generowania powiadomień.
         * Pobiera ona pliki Konfiguracja, Właściwości, oraz ustawienia z aplikacji, jakie są
         * wymagane do działania niniejszego generatora powiadomień.
         * @return Czy inicjalizacja się powiodła.
         */
        private boolean InitializeData()
        {
            TasksQueue<DataInitializeTaskOutput> tasksQueue =
                    new TasksQueue<DataInitializeTaskOutput>();


            dataInitializeTaskOutput = new DataInitializeTaskOutput();


            PropertiesXmlTask propertiesXmlTask = new PropertiesXmlTask();
            PropertiesXmlTaskInput propertiesXmlTaskInput = new PropertiesXmlTaskInput(
                "properties.xml",
                getBaseContext());
            tasksQueue.add(propertiesXmlTask, propertiesXmlTaskInput);


            VersionControllerTask versionControllerTask = new VersionControllerTask();
            VersionControllerTaskInput versionControllerTaskInput =
                new VersionControllerTaskInput(
                    "config.xml",
                    getBaseContext());
            tasksQueue.add(versionControllerTask, versionControllerTaskInput);


            ConfigurationXmlTask configurationXmlTask = new ConfigurationXmlTask();
            tasksQueue.add(configurationXmlTask, null);
            SettingsPreferenceManagerTask settingsPreferenceManagerTask =
                new SettingsPreferenceManagerTask();
            SettingsPreferenceManagerTaskInput settingsPreferenceManagerTaskInput =
                new SettingsPreferenceManagerTaskInput(
                    getBaseContext());
            tasksQueue.add(settingsPreferenceManagerTask, settingsPreferenceManagerTaskInput);


            tasksQueue.performAll(dataInitializeTaskOutput);

            return dataInitializeTaskOutput.isValid();
        }

        /**
         * Metoda ta wysyła żądane powiadomienia bazując na liście kanałów informacyjnych Feed.
         * Buduje je oraz przygotowuje, nadając tytuł oraz ikonę.
         * Czyni to dla konkretnej sekcji, z konkretnym ID notyfikacji aby się one nie podwajały.
         * @param newestFeeds Kanały informacyjne, dla których należy wywołać powiadomienie.
         * @param section Sekcja, dla której wysyłane jest dane powiadomienie
         * @param notificationId ID notyfikacji, unikalne per sekcja, aby się nie podwajały tylko
         *                       zastępowały.
         */
        private void publishNotifications(List<Feed> newestFeeds,
                                          Section section,
                                          int notificationId)
        {
            int feedsAmount = newestFeeds.size();
            if(feedsAmount > 0)
            {
                Context baseContext = getBaseContext();
                Resources resources = baseContext.getResources();
                String contentText = resources.getString(
                        R.string.notification_content_text);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(baseContext)
                                .setSmallIcon(R.drawable.ikona_bw)
                                .setContentTitle(section.title)
                                .setContentText(contentText + feedsAmount);



                Intent resultIntent = new Intent(baseContext, SplashScreenActivity.class);

                PendingIntent notifyPendingIntent =
                        PendingIntent.getActivity(
                                baseContext,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                mBuilder.setContentIntent(notifyPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(notificationId, mBuilder.build());
            }
        }

    }
}
