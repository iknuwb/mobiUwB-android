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
 * {@inheritDoc}
 * Ta kontrolka służy do otwierania oraz zamykania grupy kontrolek
 * na liście zagnieżdżonych grup.
 */
public class ExpandableOnOffItem
        extends Item<ExpandableOnOffItemModel>
        implements CompoundButton.OnCheckedChangeListener
{
    /**
     * Lista z rozszerzalnymi elementami.
     * Każdy element może posiadać swoją podgrupę
     */
    private ExpandableListView listView;

    /**
     * Inicjuje zmienne poprzez konstruktor powyżej.
     * @param model Model tej kontrolki.
     */
    public ExpandableOnOffItem(ExpandableOnOffItemModel model)
    {
        super(model);
    }

    /**
     * {@inheritDoc}
     * Nadaje wydarzenie zmiany expanded na true/false.
     * Korzysta z wzorca ViewHolder w celu redukcji ilości generacji danej kontrolki.
     * Nadaje tekst oraz kontrolkę Switch.
     */
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

    /**
     * Dzieje się gdy stan zaznaczenia zmienia się.
     * Nadaje wtedy stan rozszerzenia - czy dana grupa jest rozszerzona czy też nie.
     * @param compoundButton Przycisk.
     * @param isChecked Czy zaznaczono.
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
    {
        Integer groupPosition = (Integer) compoundButton.getTag();
        setExpanded(isChecked, listView, groupPosition);
        model.setExpanded(isChecked);
        Log.d(Globals.MOBIUWB_TAG,
              "isExpanded w onCheckedChanged dla " + model.getText() + ": " + model.isExpanded());
    }

    /**
     * Nadaje status rozszerzony/zwinięty dla określonej grupy.
     * @param expanded Czy rozszerzyć.
     * @param listView Lista, której dotyczy polecenie.
     * @param groupPosition Pozycja grupy.
     */
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

    /**
     * Klasa odpowiedzialna za wzorzec przechowywania widoku.
     * Dzięki temu nie jest on ciągle re-generowany.
     */
    private class ViewHolder
    {
        /**
         * Przechowuje kontrolkę checkboxa.
         */
        public TextView textView;

        /**
         * Przechowuje kontrolkę switcha.
         */
        public Switch switchView;
    }
}

