package pl.edu.uwb.mobiuwb.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import pl.edu.uwb.mobiuwb.MobiUwbApp;

import java.util.ArrayList;

/**
 * Created by Tunczyk on 2015-05-05.
 */
public abstract class InternetChecker
{
    private ConnectivityManager connectivityManager;
    protected ArrayList<NetworkInfo> networkInfos;

    public InternetChecker()
    {
        networkInfos = new ArrayList<NetworkInfo>();
        connectivityManager = (ConnectivityManager) MobiUwbApp.getContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        fillNetworkInfo(connectivityManager, networkInfos);
    }

    protected abstract void fillNetworkInfo(ConnectivityManager connectivityManager,
                                            ArrayList<NetworkInfo> networkInfos);

    public boolean check()
    {
        networkInfos.clear();
        fillNetworkInfo(connectivityManager, networkInfos);
        return makeCheck();
    }

    public boolean isAvailable()
    {
        for (NetworkInfo networkInfo : networkInfos)
        {
            if(networkInfo == null)
            {
                return false;
            }
        }
        return true;
    }


    protected abstract boolean makeCheck();
}
