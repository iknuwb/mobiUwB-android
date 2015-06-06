package pl.edu.uwb.mobiuwb.utillities;

import android.app.ActionBar;
import android.app.Activity;

public class ActionBarMethods
{
    /**
     * Metoda nadająca nowy tytuł dla action bar'a
     *
     * @param activity Activity dla którego nadajemy nowy tytuł.
     * @param title    Nowy tytuł.
     */
    public static void setActionBarTitle(Activity activity, String title)
    {
        ActionBar actionBar = activity.getActionBar();
        actionBar.setTitle(title);
    }
}
