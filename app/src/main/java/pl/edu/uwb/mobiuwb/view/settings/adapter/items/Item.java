package pl.edu.uwb.mobiuwb.view.settings.adapter.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.view.settings.adapter.SettingsAdapter;

/**
 * Jest to abstrakcyjna reprezentacja kontrolki na liście kontrolek w
 * ekranie Opcji/Ustawień.
 */
public abstract class Item<TItemModel extends ItemModel>
{
    /**
     * Model tej kontrolki.
     */
    public TItemModel model;

    /**
     * Nadaje zmienne.
     * @param model Model tej kontrolki.
     */
    protected Item(TItemModel model)
    {
        this.model = model;
    }

    /**
     * Wywoływane gdy kontrolka listy żąda utworzenia jej elementów.
     * Wywołuje metodę, która jest implementowana przez podklasy a polega
     * ona na skonfigurowaniu nowo generowanego elementu.
     * Wykorzystuje ona ID definicji widoku pobrane z modelu elementu.
     * @param groupPosition Pozycja grupy.
     * @param convertView Widok.
     * @param parent Rodzic kontrolki.
     * @return Utworzona kontrolka.
     */
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
    /**
     * Wywoływane gdy kontrolka listy żąda utworzenia jej elementu dziecka.
     * Dzieckiem jest tutaj grupa kontrolek, zagnieżdżonych rekurencyjnie.
     * Reprezentacją tych kontrolek jest właśnie ta klasa.
     * @param convertView Widok.
     * @param parent Rodzic kontrolki.
     * @return Utworzona kontrolka.
     */
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

    /**
     * Metoda ta polega na skonfigurowaniu nowo generowanego elementu
     * widoku.
     * @param groupPosition Pozycja grupy.
     * @param convertView Widok.
     * @param parent Rodzic kontrolki.
     */
    protected abstract void configureGroupView(int groupPosition, View convertView,
                                               ViewGroup parent);
}
