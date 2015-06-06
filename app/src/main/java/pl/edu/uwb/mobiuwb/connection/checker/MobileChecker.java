package pl.edu.uwb.mobiuwb.connection.checker;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import pl.edu.uwb.mobiuwb.connection.InternetChecker;

import java.util.ArrayList;

/**
 * Created by Tunczyk on 2015-05-05.
 */
public class MobileChecker extends InternetChecker
{
    @Override protected void fillNetworkInfo(ConnectivityManager connectivityManager,
                                             ArrayList<NetworkInfo> networkInfos)
    {
        networkInfos.add(
                connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE));
    }

    @Override public boolean makeCheck()
    {
        for (NetworkInfo networkInfo : networkInfos)
        {
            if (networkInfo.isConnected())
            {
                return true;
            }
        }
        return false;
    }
}
