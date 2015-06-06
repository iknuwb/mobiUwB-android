package pl.edu.uwb.mobiuwb.services.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.edu.uwb.mobiuwb.parsers.json.parser.JsonParser;

/**
 * Created by sennajavie on 2015-06-04.
 */
public class ServicePreferencesManager
{
    private static String SHARED_PREFERENCES_NAME;

    private Context context;
    private SharedPreferences preferences;

    private static ServicePreferencesManager instance;
    public static ServicePreferencesManager getInstance(Context context,
                                                        String sharedPreferencesName)
    {
        if (instance == null)
        {
            instance = new ServicePreferencesManager();
        }
        instance.context = context;
        SHARED_PREFERENCES_NAME = sharedPreferencesName;
        instance.preferences = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return instance;
    }

    private ServicePreferencesManager()
    {
    }

    public Date getLastKnownDate(String categoryId)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JsonParser.DATE_FORMAT);
        Date defaultDate = new Date();
        String formattedDefaultDate = simpleDateFormat.format(defaultDate);
        String unformattedDate = preferences.getString(categoryId, formattedDefaultDate);
        Date date = null;
        try
        {
            date = simpleDateFormat.parse(unformattedDate);
        }
        catch (ParseException e)
        {
            date = defaultDate;
            Log.e("MobiUwB", e.getMessage());
        }
        return date;
    }

    public void setLastKnownDate(String universityUnitName, String categoryId, Date date)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(categoryId, date.toString());
        editor.commit();
    }
}
