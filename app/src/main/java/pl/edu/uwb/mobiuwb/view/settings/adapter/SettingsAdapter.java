package pl.edu.uwb.mobiuwb.view.settings.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import pl.edu.uwb.mobiuwb.view.settings.adapter.items.Item;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.ItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tunczyk on 2015-04-18.
 */
public class SettingsAdapter extends BaseExpandableListAdapter
{
    private List<ItemModel> data;

    public SettingsAdapter(List<ItemModel> data)
    {
        this.data = data;
        Log.d("MobiUwB", "DATA SIZE = " + data.size());
    }

    public SettingsAdapter(ItemModel data)
    {
        this.data = new ArrayList<ItemModel>(1);
        this.data.add(data);
        Log.d("MobiUwB", "DATA SIZE = " + this.data.size());
    }

    @Override
    public int getGroupCount()
    {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return data.get(groupPosition).nestedModels.size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return data.get(groupPosition).nestedModels.get(childPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent)
    {
        Item strategy = data.get(groupPosition).createStrategy();
        return strategy.getGroupView(groupPosition, convertView, parent);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent)
    {

        Item strategy =
                data.get(groupPosition).
                        nestedModels.get(childPosition).
                        createStrategy();

        return strategy.getChildView(convertView, parent);
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }
}
