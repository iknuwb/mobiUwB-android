package pl.edu.uwb.mobiuwb.view.settings;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Opisuje adapter częstości wysyłania powiadomień.
 */
public class FrequencyListAdapter extends ArrayAdapter<String>
{
    /**
     * Nadaje pola w klasie poprzez konstruktor bazowy.
     * @param context Kontekst aplikacji bądź kontrolki.
     * @param objects Obiekty do wyświetlania przez adapter.
     */
    public FrequencyListAdapter(Context context, String[] objects)
    {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
}
