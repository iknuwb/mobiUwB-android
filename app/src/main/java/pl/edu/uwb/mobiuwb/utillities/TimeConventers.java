package pl.edu.uwb.mobiuwb.utillities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tunczyk on 2015-05-01.
 */
public class TimeConventers
{
    public static String getPrettyTime(int hours, int minutes)
    {
        Date date = new Date();
        date.setHours(hours);
        date.setMinutes(minutes);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String stringTime = simpleDateFormat.format(date);
        return stringTime;
    }
}
