package pl.edu.uwb.mobiuwb.utillities;

import android.util.DisplayMetrics;

import pl.edu.uwb.mobiuwb.MobiUwbApp;

/**
 * Created by Tunczyk on 2015-04-21.
 */
public class UnitConverter
{
    public static DisplayMetrics displayMetrics;

    static
    {
        displayMetrics = MobiUwbApp.getContext().getResources().getDisplayMetrics();
    }

    public static int toDpi(int px)
    {
        return (int) ((px * displayMetrics.density) + 0.5);
    }

    public static int toPx(int dp)
    {
        return (int) ((dp / displayMetrics.density) + 0.5);
    }
}
