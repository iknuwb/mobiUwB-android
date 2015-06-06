package pl.edu.uwb.mobiuwb.view.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.configuration.ConfigureEvents;
import pl.edu.uwb.mobiuwb.configuration.StartupConfig;
import pl.edu.uwb.mobiuwb.view.mainactivity.MainActivity;

public class SplashScreen extends Activity implements ConfigureEvents
{
    private Thread splashIntervalThread;
    private boolean doubleClickStop = false;
    private static long backPressedTime;
    private SharedPreferences prefs = null;
    private boolean configurationFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashIntervalThread = new Thread()
        {
            public void run()
            {
                try
                {
                    long current = System.currentTimeMillis();
                    while (true)
                    {
                        if (System.currentTimeMillis() >= current + 1500)
                        {
                            if (configurationFinished)
                            {
                                break;
                            }
                        }
                        else
                        {
                            if (doubleClickStop == true)
                            {
                                return;
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                final Intent intent = new Intent(SplashScreen.this,
                                                 MainActivity.class);
                startActivity(intent);
            }
        };
        splashIntervalThread.start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        StartupConfig startupConfig = new StartupConfig();
        startupConfig.executeStartupAsyncTask();
        startupConfig.addOnConfigurationEventListener(this);
    }


    @Override public void onConfigurationFinished()
    {
        configurationFinished = true;
    }

    @Override
    public void onBackPressed()
    {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
            {
                if (backPressedTime + 2000 > System.currentTimeMillis())
                {
                    if (splashIntervalThread != null &&
                            splashIntervalThread.isAlive())
                    {
                        doubleClickStop = true;
                        try
                        {
                            splashIntervalThread.join();
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(
                            this,
                            getString(
                                    R.string.toast_press_again_to_close),
                            Toast.LENGTH_SHORT)
                            .show();
                }
                backPressedTime = System.currentTimeMillis();
                break;
            }
            default:
            {
                break;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}