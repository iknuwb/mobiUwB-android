package pl.edu.uwb.mobiuwb.services.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.edu.uwb.mobiuwb.parsers.json.parser.JsonParser;
import pl.edu.uwb.mobiuwb.view.settings.SettingsPreferencesManager;

/**
 * Jest to zarządca współdzielonymi preferencjami
 * systemu operacyjnego Android.
 */
public class ServicePreferencesManager
{
    /**
     * Jest to standardowa nazwa preferencji w programie.
     */
    private static String SHARED_PREFERENCES_NAME;

    /**
     * Kontekst aplikacji bądź też widoku.
     */
    private Context context;

    /**
     * Preferencje zastosowane w tym zarządzaczu.
     */
    private SharedPreferences preferences;

    /**
     * Jedyna instancja zarządzacza.
     */
    private static ServicePreferencesManager instance;

    /**
     * Metoda pobierająca jedyną instancję zarządcy.
     * Metoda ta inicjalizuje pola w klasie.
     * @param context Kontekst aplikacji bądź też widoku.
     * @return Jedyna instancja zarządzacza.
     */
    public static ServicePreferencesManager getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new ServicePreferencesManager();
        }
        instance.context = context;
        SHARED_PREFERENCES_NAME = SettingsPreferencesManager.SHARED_PREFERENCES_NAME;
        instance.preferences = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return instance;
    }

    /**
     * Ten konstruktor jest prywatny ze względu na zablokowanie tworzenia więcej
     * niż jednej instancji tej klasy.
     */
    private ServicePreferencesManager()
    {
    }

    /**
     * Pobiera ostatnio zapisaną datę.
     * @param universityUnitName Nazwa jednostki.
     * @param sectionId ID sekcji.
     * @return Ostatnio zapisana data.
     */
    public Date getLastKnownDate(String universityUnitName, String sectionId)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JsonParser.DATE_FORMAT);
        Date defaultDate = new Date();
        String formattedDefaultDate = simpleDateFormat.format(defaultDate);
        String unformattedDate = preferences.getString(
                concatLastKnownDateId(
                        universityUnitName,
                        sectionId),
                formattedDefaultDate);
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

    /**
     * Konkatenuje ID tak, by było zależne od jednostki jak i też
     * od ID sekcji.
     * @param universityUnitName Nazwa jednostki.
     * @param sectionId ID sekcji.
     * @return Skonkatenowane ID.
     */
    private String concatLastKnownDateId(String universityUnitName, String sectionId)
    {
        return universityUnitName + '_' + sectionId;
    }

    /**
     * Nadaje ostatnio znaną datę.
     * @param universityUnitName Nazwa jednostki.
     * @param sectionId ID sekcji.
     * @param date Data do nadania.
     */
    public void setLastKnownDate(String universityUnitName, String sectionId, Date date)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(
                concatLastKnownDateId(
                    universityUnitName,
                    sectionId),
                date.toString());
        editor.commit();
    }
}
