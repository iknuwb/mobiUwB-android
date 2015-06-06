package pl.edu.uwb.mobiuwb.view.settings.adapter.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.settings.adapter.SettingsAdapter;

/**
 * Created by Tunczyk on 2015-04-18.
 */
public abstract class Item<TItemModel extends ItemModel>
{
    public TItemModel model;

    protected Item(TItemModel model)
    {
        this.model = model;
    }

    public View getGroupView(int groupPosition, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(
                    parent.getContext()).inflate(
                    model.getLayout(), parent, false);
        }
        configureGroupView(groupPosition, convertView, parent);
        return convertView;
    }

    public View getChildView(View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(
                    parent.getContext()).inflate(
                    R.layout.settings_listview_child, parent, false);
            ExpandableListView viewExpandableListView =
                    (ExpandableListView) convertView.findViewById(R.id.ChildListView);
            SettingsAdapter settingsAdapter = new SettingsAdapter(model);
            //viewExpandableListView.setPadding(UnitConverter.toDpi(30), 0, 0, 0);
            viewExpandableListView.setAdapter(settingsAdapter);
        }
        return convertView;
    }


    protected abstract void configureGroupView(int groupPosition, View convertView,
                                               ViewGroup parent);
}
