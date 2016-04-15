package pl.edu.uwb.mobiuwb.connection.checker;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import pl.edu.uwb.mobiuwb.connection.InternetChecker;

import java.util.ArrayList;

/**
 * {@inheritDoc}
 * Ten podtyp rozpoznaje mobilny internet.
 */
public class MobileChecker extends InternetChecker
{
    /**
     *  {@inheritDoc}
     */
    @Override protected void fillNetworkInfo(ConnectivityManager connectivityManager,
                                             ArrayList<NetworkInfo> networkInfos)
    {
        networkInfos.add(
                connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE));
    }

    /**
     *  {@inheritDoc}
     */
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
