package pl.edu.uwb.mobiuwb.controls.dialogs;

import android.app.DialogFragment;

import pl.edu.uwb.mobiuwb.interfaces.OnValueChangedListener;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog.DialogItemModel;


/**
 * Created by sennajavie on 2015-04-30.
 */
public abstract class DialogCreator<T>
{
    protected DialogItemModel model;

    public DialogItemModel getModel()
    {
        return model;
    }

    public void setModel(DialogItemModel model)
    {
        this.model = model;
    }

    OnValueChangedListener valueChange;

    public void setOnValueChangedListener(OnValueChangedListener valueChange)
    {
        this.valueChange = valueChange;
    }

    protected DialogCreator()
    {
    }

    public abstract DialogFragment create();
}
