package pl.edu.uwb.mobiuwb.connection;

import android.os.AsyncTask;
import android.util.Log;

import pl.edu.uwb.mobiuwb.connection.interfaces.OnConnectionChangedListener;
import pl.edu.uwb.mobiuwb.utillities.Globals;

/**
 * Created by Tunczyk on 2015-05-05.
 */
public class InternetCheckingService extends AsyncTask<Void, InternetCheckingService.ProgressUpdateData, Void>
{
    private static final long INTERVAL_MILISECONDS = 500;


    private OnConnectionChangedListener onConnectionLostListener;
    private InternetChecker wifiInternetChecker;
    public void setWifiInternetChecker(
            InternetChecker wifiInternetChecker)
    {
        this.wifiInternetChecker = wifiInternetChecker;
    }

    private InternetChecker mobileInternetChecker;
    public void setMobileInternetChecker(
            InternetChecker mobileInternetChecker)
    {
        this.mobileInternetChecker = mobileInternetChecker;
    }

    public void setOnConnectionLostListener(
            OnConnectionChangedListener onConnectionLostListener)
    {
        this.onConnectionLostListener = onConnectionLostListener;
    }

    private OnConnectionChangedListener onConnectionRecivedListener;

    public void setOnConnectionRecivedListener(
            OnConnectionChangedListener onConnectionRecivedListener)
    {
        this.onConnectionRecivedListener = onConnectionRecivedListener;
    }

    private boolean running;

    public void stopService()
    {
        running = false;
    }

    @Override protected void onPreExecute()
    {
        super.onPreExecute();
        //Log.d(Globals.MOBIUWB_TAG, "onPreExecute");
        running = true;
    }

    @Override protected Void doInBackground(Void... params)
    {
        boolean wifiCheck = false;
        boolean mobileCheck = false;

        boolean previousInternetState = true;
        boolean currentInternetState = false;

        CheckingType checkingType = CheckingType.WIFI;

        while (running)
        {
            try
            {
                if(wifiInternetChecker != null)
                {
                    wifiCheck = wifiInternetChecker.check();
                    currentInternetState = wifiCheck;
                }
                if(wifiCheck == true)
                {
                    if(checkingType != CheckingType.WIFI)
                    {
                        checkingType = CheckingType.WIFI;
                        publishProgress(new ProgressUpdateData(checkingType, currentInternetState));
                        previousInternetState = true;
                    }
                }
                else
                {
                    if(mobileInternetChecker != null)
                    {
                        mobileCheck = mobileInternetChecker.check();
                        currentInternetState = mobileCheck;
                    }
                    if(mobileCheck == true)
                    {
                        if(checkingType != CheckingType.MOBILE)
                        {
                            checkingType = CheckingType.MOBILE;
                            publishProgress(new ProgressUpdateData(checkingType, currentInternetState));
                            previousInternetState = true;
                        }
                    }
                    else
                    {
                        Log.d(Globals.MOBIUWB_TAG,"else previousInternetState: " + previousInternetState);
                        Log.d(Globals.MOBIUWB_TAG,"currentInternetState: " + currentInternetState);
                        if(previousInternetState != currentInternetState)
                        {
                            Log.d(Globals.MOBIUWB_TAG,"if previousInternetState: " + previousInternetState);
                            Log.d(Globals.MOBIUWB_TAG,"currentInternetState: " + currentInternetState);
                            checkingType = CheckingType.NONE;
                            previousInternetState = currentInternetState;
                            publishProgress(new ProgressUpdateData(checkingType, currentInternetState));
                        }
                    }
                }
                Thread.sleep(INTERVAL_MILISECONDS);

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override protected void onProgressUpdate(ProgressUpdateData... values)
    {
        //Log.d(Globals.MOBIUWB_TAG, "onProgressUpdate: " + values[0]);
        super.onProgressUpdate(values);
        ProgressUpdateData data = values[0];

        if (data.isActive())
        {
            onConnectionRecivedListener.connectionChanged(data.getCheckingType());
        }
        else
        {
            onConnectionLostListener.connectionChanged(data.getCheckingType());
        }
    }

    public class ProgressUpdateData
    {
        private CheckingType checkingType;
        public CheckingType getCheckingType()
        {
            return checkingType;
        }
        public void setCheckingType(CheckingType checkingType)
        {
            this.checkingType = checkingType;
        }

        private boolean active;
        public boolean isActive()
        {
            return active;
        }
        public void setActive(boolean active)
        {
            this.active = active;
        }

        public ProgressUpdateData(CheckingType checkingType, boolean active)
        {
            this.checkingType = checkingType;
            this.active = active;
        }

        @Override public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            ProgressUpdateData data = (ProgressUpdateData) o;

            if (isActive() != data.isActive())
            {
                return false;
            }
            return getCheckingType() == data.getCheckingType();

        }

        @Override
        public int hashCode()
        {
            return 0;
        }
    }
}
