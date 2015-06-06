package pl.edu.uwb.mobiuwb.view.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sennajavie on 2015-05-09.
 */
public class ConnectionListAdapter extends ArrayAdapter<ConnectionListModel>
{
    public ConnectionListAdapter(Context context, List<ConnectionListModel> objects)
    {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ConnectionListModel item = getItem(position);
        viewHolder.itemView.setText(item.getDisplayText());

        convertView.setEnabled(item.isEnabled());

        return convertView;
    }

    @Override public boolean areAllItemsEnabled()
    {
        return false;
    }

    @Override public boolean isEnabled(int position)
    {
        ConnectionListModel item = getItem(position);
        return item.isEnabled();
    }

    private class ViewHolder
    {
        public TextView itemView;
    }
}
