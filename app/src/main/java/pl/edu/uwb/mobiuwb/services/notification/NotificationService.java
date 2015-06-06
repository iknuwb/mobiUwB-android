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
import pl.edu.uwb.mobiuwb.view.splash.SplashScreen;

/**
 * Created by sennajavie on 2015-06-01.
 */
public class NotificationService extends Service
{
    public static final int RELOAD_CONFIGURATION = 0;
    public static final int RESTART = 1;
    public static final int STOP = 2;
    public static final int START = 3;

    private ServiceTask serviceTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        int flag = intent.getIntExtra(ServiceManager.REQUEST_FLAG, -1);
        parseRequest(flag);
        return Service.START_STICKY;
    }

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


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    @Override public void onDestroy()
    {
        super.onDestroy();
        serviceTask.cancel(true);
    }

    private class ServiceTask extends AsyncTask<Void, Void, Void>
    {
        private boolean active;

        private DataInitializeTaskOutput dataInitializeTaskOutput;
        private long lastCheckTime = 0;
        private long currentTime = 0;

        public void stopBackgroundProcess()
        {
            active = false;
        }

        @Override protected void onPreExecute()
        {
            active = true;
            super.onPreExecute();
        }

        @Override protected Void doInBackground(Void... params)
        {
            boolean succeeded = InitializeData();
            processNotifications(succeeded);
            return null;
        }

        private void processNotifications(boolean succeeded)
        {
            if(dataInitializeTaskOutput.isNotificationActive &&
                    succeeded)
            {
                notificationsLoop();
            }
        }

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

        private void notificationsExecute()
        {
            lastCheckTime = currentTime;
            runNotificationsPublisher();
        }

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

        private void notificationsTimeRangeCheck(int current, int from, int to)
        {
            if (from <= current &&
                    current <= to)
            {
                notificationsExecute();
            }
        }


        private void runNotificationsPublisher()
        {
            UniversityUnit unit = dataInitializeTaskOutput.configXmlResult.getCurrentUniversityUnit();
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



                Intent resultIntent = new Intent(baseContext, SplashScreen.class);

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
