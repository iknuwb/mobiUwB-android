package pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog;

import android.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.controls.dialogs.DialogCreator;
import pl.edu.uwb.mobiuwb.interfaces.OnValueChangedListener;
import pl.edu.uwb.mobiuwb.view.settings.SettingsActivity;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.Item;

/**
 * Created by Tunczyk on 2015-04-18.
 */
public class DialogItem extends Item<DialogItemModel>
        implements View.OnClickListener
{
    private DialogCreator dialog;

    public DialogItem(DialogItemModel model,
                      DialogCreator dialogCreationStrategy)
    {
        super(model);
        this.dialog = dialogCreationStrategy;
    }

    @Override
    protected void configureGroupView(int groupPosition, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder;
        if (convertView.getTag() == null)
        {
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.TextView);
            viewHolder.valueTextView = (TextView) convertView.findViewById(R.id.ValueTextView);
            convertView.setTag(viewHolder);
            this.model.setOnValueChangeListener(new OnValueChangedListener()
            {
                @Override
                public void notifyChanged(Object oldValue, Object newValue)
                {
                    viewHolder.valueTextView.setText(model.prettyPrintValue(newValue));
                }
            });
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(model.getText());
        viewHolder.valueTextView.setText(model.prettyPrintValue());

        convertView.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view)
    {
        DialogFragment dialog = this.dialog.create();
        dialog.show(SettingsActivity.settingsActivityFragmentManager, getClass().getName());
    }

    private class ViewHolder
    {
        public TextView textView;
        public TextView valueTextView;
    }
}
