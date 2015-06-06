package pl.edu.uwb.mobiuwb.view.settings;

import android.widget.Toast;

import pl.edu.uwb.mobiuwb.MobiUwbApp;

import java.util.Date;

/**
 * Created by sennajavie on 2015-05-03.
 */
public class SettingsValidator
{
    public static boolean validateTime(
            Date newValueFrom,
            Date newValueTo)
    {
        Integer fromHours = newValueFrom.getHours();
        Integer toHours = newValueTo.getHours();
        Integer fromMinutes = newValueFrom.getMinutes();
        Integer toMinutes = newValueTo.getMinutes();

        int hourResult = fromHours.compareTo(toHours);
        int minutesResult = fromMinutes.compareTo(toMinutes);

        if (hourResult < 0)
        {
            return true;
        }
        else if (hourResult == 0)
        {
            if (minutesResult < 0)
            {
                return true;
            }
            else
            {
                Toast.makeText(
                        MobiUwbApp.getContext(),
                        "Zły przedział minut.",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else
        {
            Toast.makeText(
                    MobiUwbApp.getContext(),
                    "Zły przedział godzin.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
