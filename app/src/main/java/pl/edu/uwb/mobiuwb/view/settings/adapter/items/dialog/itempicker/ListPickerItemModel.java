package pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.itempicker;

import android.widget.ListAdapter;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.controls.dialogs.DialogCreator;
import pl.edu.uwb.mobiuwb.controls.dialogs.ListDialog;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.DialogItemModel;

/**
 * {@inheritDoc}
 * Tworzy model dialogu z listą.
 */
public class ListPickerItemModel extends DialogItemModel<Integer>
{
    /**
     * Adapter niniejszej listy w dialogu.
     */
    private ListAdapter adapter;

    /**
     * Inicjuje zmienne, pobiera adapter.
     * @param dialogCreationStrategy Określona strategia tworzenia dialogu.
     */
    public ListPickerItemModel(
            DialogCreator dialogCreationStrategy)
    {
        super(dialogCreationStrategy);
        ListDialog dialog = (ListDialog) dialogCreationStrategy;
        adapter = dialog.getAdapter();
    }


    /**
     * {@inheritDoc}
     */
    @Override public String prettyPrintValue(Integer value)
    {
        return adapter.getItem(value).toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected int initLayout()
    {
        return R.layout.settings_adapter_simple_item;
    }

}
