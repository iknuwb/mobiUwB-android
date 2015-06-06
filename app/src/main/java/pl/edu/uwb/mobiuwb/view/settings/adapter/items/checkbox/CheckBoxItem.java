package pl.edu.uwb.mobiuwb.view.settings.adapter.items.checkbox;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.Item;

/**
 * Created by Tunczyk on 2015-04-18.
 */
public class CheckBoxItem
        extends Item<CheckBoxItemModel>
        implements CompoundButton.OnCheckedChangeListener
{
    public CheckBoxItem(CheckBoxItemModel model)
    {
        super(model);
    }

    @Override
    protected void configureGroupView(int groupPosition, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView.getTag() == null)
        {
            viewHolder = new ViewHolder();
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.CheckBox);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.TextView);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(model.getText());
        viewHolder.checkbox.setChecked(model.isChecked());
        viewHolder.checkbox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
    {
        model.setChecked(checked);
    }


    private class ViewHolder
    {
        public CheckBox checkbox;
        public TextView textView;

    }
}


