package pl.edu.uwb.mobiuwb.services.notification;

import android.content.Context;
import android.content.Intent;

/**
 * Created by sennajavie on 2015-06-04.
 */
public class ServiceManager
{
    public static final String REQUEST_FLAG = "nf";

    public static void configureNotificationService(
        Context context,
        Class clas,
        int flag)
    {
        Intent intent = new Intent(
                context,
                clas);
        intent.putExtra(REQUEST_FLAG, flag);
        context.startService(intent);
    }
}
