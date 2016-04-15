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
 * Ten adapter reprezentuje kolekcję opcji w ekranie Opcji/Ustawień.
 * Lista na tym ekranie zawiera wiele takich adapterów, każdy zagnieżdżony.
 * Oznacza to, że adaptery te działają w efekcie rekurencyjnie.
 */
public class SettingsAdapter extends BaseExpandableListAdapter
{
    /**
     * Lista modeli elementów Opcji/Ustawień.
     */
    private List<ItemModel> data;

    /**
     * Nadaje zmienne, loguje rozmiar tych zmiennych.
     * @param data Lista modeli elementów Opcji/Ustawień.
     */
    public SettingsAdapter(List<ItemModel> data)
    {
        this.data = data;
        Log.d("MobiUwB", "DATA SIZE = " + data.size());
    }

    /**
     * Nadaje zmienne, loguje rozmiar tych zmiennych.
     * @param data Lista modeli elementów Opcji/Ustawień.
     */
    public SettingsAdapter(ItemModel data)
    {
        this.data = new ArrayList<ItemModel>(1);
        this.data.add(data);
        Log.d("MobiUwB", "DATA SIZE = " + this.data.size());
    }

    /**
     * Zwraca ilość modeli elementów w tym adapterze.
     * @return Ilość modeli elementów.
     */
    @Override
    public int getGroupCount()
    {
        return data.size();
    }

    /**
     * Pobiera liczbę elementów-dzieci.
     * @param groupPosition Grupa, która ma zawierać elementy-dzieci.
     * @return Ilość elementów-dzieci.
     */
    @Override
    public int getChildrenCount(int groupPosition)
    {
        return data.get(groupPosition).nestedModels.size();
    }

    /**
     * Pobiera grupę elementów-dzieci.
     * @param groupPosition Pozycja grupy.
     * @return Grupa.
     */
    @Override
    public Object getGroup(int groupPosition)
    {
        return data.get(groupPosition);
    }

    /**
     * Pobiera określone dziecko z listy dzieci.
     * @param groupPosition Pozycja grupy z jakiej ma pobrać owe dziecko.
     * @param childPosition Pozycja dziecka w liście dzieci.
     * @return Dziecko.
     */
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return data.get(groupPosition).nestedModels.get(childPosition);
    }

    /**
     * Pobiera zagnieżdżony widok.
     * @param groupPosition Pozycja grupy.
     * @param isExpanded Czy jest rozszerzona.
     * @param convertView Widok.
     * @param parent Rozdzic widoku.
     * @return Zanieżdżony widok.
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent)
    {
        Item strategy = data.get(groupPosition).createStrategy();
        return strategy.getGroupView(groupPosition, convertView, parent);
    }

    /**
     * Pobiera dziecko-widok.
     * @param groupPosition Pozycja grupy.
     * @param childPosition Pozycja dziecka do pobrania.
     * @param isLastChild Czy jest ostatnim dzieckiem.
     * @param convertView Widok.
     * @param parent Rozdzic widoku.
     * @return Zagnieżdżone dziecko-widok.
     */
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

    /**
     * Czy można zaznaczyć dane dziecko.
     * @param groupPosition Pozycja grupy.
     * @param childPosition Pozycja dziecka.
     * @return Czy można zaznaczyć dane dziecko.
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

    /**
     * Pobiera ID grupy.
     * @param groupPosition Pozycja grupy.
     * @return ID grupy.
     */
    @Override
    public long getGroupId(int groupPosition)
    {
        return 0;
    }

    /**
     * Pobiera ID dziecka w grupie.
     * @param groupPosition Pozycja grupy.
     * @param childPosition Pozycja dziecka.
     * @return ID dziecka.
     */
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return 0;
    }

    /**
     * Czy posiada ustabilizowane ID.
     * @return Czy posiada ustabilizowane ID.
     */
    @Override
    public boolean hasStableIds()
    {
        return false;
    }
}
