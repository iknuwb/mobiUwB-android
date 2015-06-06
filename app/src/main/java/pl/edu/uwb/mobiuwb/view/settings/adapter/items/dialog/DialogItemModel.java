package pl.edu.uwb.mobiuwb.view.settings.adapter.items.dialog;

import android.os.Parcel;

import pl.edu.uwb.mobiuwb.controls.dialogs.DialogCreator;
import pl.edu.uwb.mobiuwb.interfaces.OnValueChangedListener;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.Item;
import pl.edu.uwb.mobiuwb.view.settings.adapter.items.ItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tunczyk on 2015-04-18.
 */
public abstract class DialogItemModel<T> extends ItemModel
{
    private int textControlId;

    public int getTextControlId()
    {
        return textControlId;
    }

    public void setTextControlId(int textControlId)
    {
        this.textControlId = textControlId;
    }

    protected T value;

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        if (valueChangeListeners != null && !this.value.equals(value))
        {
            for (OnValueChangedListener valueChange : valueChangeListeners)
            {
                valueChange.notifyChanged(this.value, value);
            }
        }
        this.value = value;
    }

    private String title;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public abstract String prettyPrintValue(T value);

    public String prettyPrintValue()
    {
        return prettyPrintValue(value);
    }

    private List<OnValueChangedListener> valueChangeListeners;

    public void setOnValueChangeListener(OnValueChangedListener valueChange)
    {
        if (valueChangeListeners == null)
        {
            valueChangeListeners = new ArrayList<OnValueChangedListener>();
        }
        valueChangeListeners.add(valueChange);
    }

    protected DialogCreator dialogCreationStrategy;

    public DialogItemModel(DialogCreator dialogCreationStrategy)
    {
        super();
        this.dialogCreationStrategy = dialogCreationStrategy;
        this.dialogCreationStrategy.setModel(this);
    }

    @Override
    public Item createStrategy()
    {
        return new DialogItem(this, dialogCreationStrategy);
    }

    @Override public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);
        dest.writeValue(value);
    }
}
