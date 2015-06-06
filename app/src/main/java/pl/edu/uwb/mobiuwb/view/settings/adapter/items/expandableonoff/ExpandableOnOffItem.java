package pl.edu.uwb.mobiuwb.view.settings.adapter.items.expandableonoff;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Switch;
import android.widget.TextView;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.utillities.Globals;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.Item;

/**
 * Created by Tunczyk on 2015-04-18.
 */


public class ExpandableOnOffItem
        extends Item<ExpandableOnOffItemModel>
        implements CompoundButton.OnCheckedChangeListener
{
    private ExpandableListView listView;

    public ExpandableOnOffItem(ExpandableOnOffItemModel model)
    {
        super(model);
    }

    @Override
    protected void configureGroupView(int groupPosition,
                                      final View convertView,
                                      final ViewGroup parent)
    {

        ViewHolder viewHolder;
        if (convertView.getTag() == null)
        {
            viewHolder = new ViewHolder();
            viewHolder.switchView = (Switch) convertView.findViewById(R.id.SwitchView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.TextView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (listView == null)
        {
            listView = (ExpandableListView) parent;
        }
        viewHolder.textView.setText(model.getText());
        viewHolder.switchView.setChecked(model.isExpanded());
        Log.d(Globals.MOBIUWB_TAG,
              "isExpanded w configureGroupView dla " + model.getText() + ": " + model.isExpanded());
        viewHolder.switchView.setOnCheckedChangeListener(this);
        viewHolder.switchView.setTag(groupPosition);

        setExpanded(model.isExpanded(), listView, groupPosition);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
    {
        Integer groupPosition = (Integer) compoundButton.getTag();
        setExpanded(isChecked, listView, groupPosition);
        model.setExpanded(isChecked);
        Log.d(Globals.MOBIUWB_TAG,
              "isExpanded w onCheckedChanged dla " + model.getText() + ": " + model.isExpanded());
    }

    private void setExpanded(boolean expanded, ExpandableListView listView, int groupPosition)
    {
        if (expanded)
        {
            listView.expandGroup(groupPosition, true);
        }
        else
        {
            listView.collapseGroup(groupPosition);
        }
    }

    private class ViewHolder
    {
        public TextView textView;
        public Switch switchView;
    }
}

