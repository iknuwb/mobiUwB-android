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
 * {@inheritDoc}
 * Tworzy element widoku Opcji/Ustawień bazujący na dialogu.
 */
public class DialogItem extends Item<DialogItemModel>
        implements View.OnClickListener
{
    /**
     * Sposób tworzenia dialoga.
     */
    private DialogCreator dialog;

    /**
     * Nadaje zmienne, wywołuje bazowy konstruktor.
     * @param model Model niniejszej kontrolki.
     * @param dialogCreationStrategy Sposób tworzenia dialoga.
     */
    public DialogItem(DialogItemModel model,
                      DialogCreator dialogCreationStrategy)
    {
        super(model);
        this.dialog = dialogCreationStrategy;
    }

    /**
     * {@inheritDoc}
     * Nadaje wydarzenia zmiany wartości dla tego dialoga.
     * Tworzy przycisk którego kliknięcie utworzy dialog.
     * Wykorzystuje wzorzec ViewHolder, który polega na zapisaniu referencji
     * i ograniczeniu przez to liczy utworzeń danej kontrolki.
     */
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

    /**
     * Kliknięcie tego przycisku tworzy dialog.
     * @param view Potomek dialoga.
     */
    @Override
    public void onClick(final View view)
    {
        DialogFragment dialog = this.dialog.create();
        dialog.show(SettingsActivity.settingsActivityFragmentManager, getClass().getName());
    }

    /**
     * Jest to element wzorca, który polega na zapisaniu referencji do
     * kontrolki, aby uniknąć jej nadmiernego tworzenia przez system.
     */
    private class ViewHolder
    {
        /**
         * Kontrolka z tekstem.
         */
        public TextView textView;

        /**
         * Kontrolka z tekstową reprezentacją wartości.
         */
        public TextView valueTextView;
    }
}
