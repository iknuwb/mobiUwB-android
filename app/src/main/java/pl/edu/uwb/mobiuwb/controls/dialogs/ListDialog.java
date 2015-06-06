package pl.edu.uwb.mobiuwb.controls.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListAdapter;

import pl.edu.uwb.mobiuwb.interfaces.OnValueChangedListener;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.itempicker.ListPickerItemModel;

/**
 * Created by sennajavie on 2015-04-30.
 */
public class ListDialog extends DialogCreator<Integer>
{

    private ListAdapter listAdapter;
    public ListAdapter getAdapter()
    {
        return listAdapter;
    }

    public ListDialog(ListAdapter listAdapter)
    {
        super();
        this.listAdapter = listAdapter;
    }

    @Override public DialogFragment create()
    {
        DialogFragment dialogFragment =
                new SimpleDialogFragment((ListPickerItemModel) model, valueChange, listAdapter);
        return dialogFragment;
    }


    public static class SimpleDialogFragment extends DialogFragment
            implements DialogInterface.OnClickListener
    {
        private ListPickerItemModel model;
        private ListAdapter listAdapter;

        public ListPickerItemModel getModel()
        {
            return model;
        }

        private void setModel(ListPickerItemModel model)
        {
            this.model = model;
        }

        private OnValueChangedListener valueChange;

        public SimpleDialogFragment()
        {
        }

        public SimpleDialogFragment(ListPickerItemModel model,
                                    OnValueChangedListener valueChange,
                                    ListAdapter listAdapter)
        {
            this.model = model;
            this.valueChange = valueChange;
            this.listAdapter = listAdapter;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(model.getTitle())
                    .setAdapter(listAdapter,this);
            return builder.create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if (valueChange != null)
            {
                valueChange.notifyChanged(model.getValue(), which);
            }
        }
    }
}
