package pl.edu.uwb.mobiuwb.view.settings;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by sennajavie on 2015-05-09.
 */
public class FrequencyListAdapter extends ArrayAdapter<String>
{
    public FrequencyListAdapter(Context context, String[] objects)
    {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
}
