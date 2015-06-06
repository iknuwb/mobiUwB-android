package pl.edu.uwb.mobiuwb;

import android.app.Application;

public class MobiUwbApp extends Application
{

    private static MobiUwbApp instance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    public static MobiUwbApp getContext()
    {
        return instance;
    }
}