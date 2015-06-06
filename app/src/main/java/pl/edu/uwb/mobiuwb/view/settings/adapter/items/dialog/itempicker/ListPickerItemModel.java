package pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.itempicker;

import android.widget.ListAdapter;

import pl.edu.uwb.mobiuwb.R;
import pl.edu.uwb.mobiuwb.controls.dialogs.DialogCreator;
import pl.edu.uwb.mobiuwb.controls.dialogs.ListDialog;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.DialogItemModel;

/**
 * Created by sennajavie on 2015-05-03.
 */
public class ListPickerItemModel extends DialogItemModel<Integer>
{
    private ListAdapter adapter;
    public ListPickerItemModel(
            DialogCreator dialogCreationStrategy)
    {
        super(dialogCreationStrategy);
        ListDialog dialog = (ListDialog) dialogCreationStrategy;
        adapter = dialog.getAdapter();
    }

    @Override public String prettyPrintValue(Integer value)
    {
        return adapter.getItem(value).toString();
    }

    @Override
    protected int initLayout()
    {
        return R.layout.settings_adapter_simple_item;
    }

}
