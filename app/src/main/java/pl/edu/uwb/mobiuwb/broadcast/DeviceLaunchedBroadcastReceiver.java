package pl.edu.uwb.mobiuwb.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pl.edu.uwb.mobiuwb.services.notification.NotificationService;
import pl.edu.uwb.mobiuwb.services.notification.ServiceManager;
import pl.edu.uwb.mobiuwb.utillities.Globals;

/**
 * Reprezentuje odbiornik powiadomienia generowanego przy starcie telefonu.
 */
public class DeviceLaunchedBroadcastReceiver extends BroadcastReceiver
{
    /**
     * Dzieje się gdy odbiornik odbierze powiadomienie.
     * Rozpoznaje konkretny typ powiadomienia,
     * po czym konfiguruje usługę notyfikacji.
     * @param context kontekst
     * @param intent intencja
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Log.d(Globals.MOBIUWB_TAG, "URUCHOMIONO TELEFON");
            ServiceManager.configureNotificationService(
                    context,
                    NotificationService.class,
                    NotificationService.START);
        }
    }
}
