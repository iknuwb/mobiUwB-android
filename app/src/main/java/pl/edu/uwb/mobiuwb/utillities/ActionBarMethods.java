package pl.edu.uwb.mobiuwb.utillities;

import android.app.ActionBar;
import android.app.Activity;

/**
 * Zawiera w sobie zbiór metod do zarządzania paskiem ActionBar.
 */
public class ActionBarMethods
{
    /**
     * Metoda nadająca nowy tytuł dla ActionBara.
     *
     * @param activity Activity dla którego nadajemy nowy tytuł.
     * @param title Nowy tytuł.
     */
    public static void setActionBarTitle(Activity activity, String title)
    {
        ActionBar actionBar = activity.getActionBar();
        actionBar.setTitle(title);
    }
}
